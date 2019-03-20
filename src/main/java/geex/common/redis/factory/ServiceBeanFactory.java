package geex.common.redis.factory;

import geex.common.redis.common.LockType;
import geex.common.redis.service.LockService;
import geex.common.redis.service.impl.*;
import geex.common.redis.utils.SpringUtil;

import javax.management.ServiceNotFoundException;
import java.util.EnumMap;

/**
 * @author GEEX581
 */
public class ServiceBeanFactory {
    private static EnumMap<LockType, Class<?>> serviceMap = new EnumMap<>(LockType.class);

    static {
        serviceMap.put(LockType.REENTRANT, ReentrantLockServiceImpl.class);
        serviceMap.put(LockType.FAIR, FairLockServiceImpl.class);
        serviceMap.put(LockType.MULTI, MultiLockServiceImpl.class);
        serviceMap.put(LockType.READ, ReadLockServiceImpl.class);
        serviceMap.put(LockType.RED, RedLockServiceImpl.class);
        serviceMap.put(LockType.WRITE, WriteLockServiceImpl.class);
    }

    /**
     * 根据锁类型获取相应的服务处理类
     *
     * @param lockType
     * @return
     * @throws ServiceNotFoundException
     */
    public LockService getService(LockType lockType) throws ServiceNotFoundException {
        LockService lockService = (LockService) SpringUtil.getBean(serviceMap.get(lockType));
        if (lockService == null) {
            throw new ServiceNotFoundException();
        }
        return lockService;
    }
}
