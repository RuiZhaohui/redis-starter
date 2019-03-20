package geex.common.redis.core;

import geex.common.redis.annotation.Key;
import geex.common.redis.annotation.Lock;
import geex.common.redis.factory.ServiceBeanFactory;
import geex.common.redis.service.LockService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author GEEX581
 */
@Aspect
@Component
public class LockInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LockInterceptor.class);
    private final ServiceBeanFactory serviceBeanFactory;

    @Autowired
    public LockInterceptor(ServiceBeanFactory serviceBeanFactory) {
        this.serviceBeanFactory = serviceBeanFactory;
    }

    @Around(value = "@annotation(geex.common.redis.annotation.Lock)")
    public Object lockHandle(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), targetMethod.getParameterTypes());

        Lock lock = realMethod.getAnnotation(Lock.class);

        LockKey.Builder keyBuilder = LockKey.newBuilder()
                .leaseTime(lock.leaseTime())
                .waitTime(lock.waitTime())
                .timeUnit(lock.timeUnit());

        Object[] args = joinPoint.getArgs();

        Annotation[][] methodParameterAnnotations = realMethod.getParameterAnnotations();
        for (int i=0;i<methodParameterAnnotations.length;i++) {
            Annotation[] annotations = methodParameterAnnotations[i];
            if (annotations.length > 0) {
                if (annotations[0] instanceof Key) {
                    keyBuilder.appendKey(args[i].toString());
                }
            }
        }

        if (keyBuilder.isEmptyKeys()) {
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = methodSignature.getName();
            keyBuilder.appendKey(className + "." + methodName);
        }
        LockKey lockKey = keyBuilder.build();

        LockService lockService = serviceBeanFactory.getService(lock.lockType());
        lockService.setLockKey(lockKey);
        Object proceedObj = null;
        try {
            lockService.lock();
            proceedObj = joinPoint.proceed();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            lockService.release();
        }

        return proceedObj;
    }
}
