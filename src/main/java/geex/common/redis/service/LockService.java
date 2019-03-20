package geex.common.redis.service;

import geex.common.redis.core.LockKey;

/**
 * @author GEEX581
 */
public interface LockService {
    /**
     * 添加key
     *
     * @param lockKey lockKey
     */
    void setLockKey(LockKey lockKey);

    /**
     * 加锁
     *
     * @throws Exception e
     */
    void lock() throws Exception;

    /**
     * 解锁
     */
    void release();
}
