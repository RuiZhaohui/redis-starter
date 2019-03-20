package geex.common.redis.annotation;

import geex.common.redis.common.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author GEEX581
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {
    /**
     * 锁类型
     *
     * @return LockType
     */
    LockType lockType() default LockType.REENTRANT;

    /**
     * 加锁时间，超过该时长自动解锁，默认单位为：秒
     *
     * @return leaseTime
     */
    long leaseTime() default -1;

    /**
     * 等待锁时间，默认单位：秒
     *
     * @return waitTime
     */
    long waitTime() default -1;

    /**
     * 锁时长单位
     *
     * @return timeUnit
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
