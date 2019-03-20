package geex.common.redis.service;

import geex.common.redis.annotation.Key;
import geex.common.redis.annotation.Lock;
import geex.common.redis.common.LockType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * //TODO description.
 *
 * @author JuChen
 * @date 2019/2/15
 */
@Component
public class PersonLockService {
    private static final Logger log = LoggerFactory.getLogger(PersonLockService.class);


    @Lock(lockType = LockType.REENTRANT, waitTime = 20, leaseTime = 10)
    public void firstReentrant(String ces, @Key String orderNo) throws InterruptedException {
    }

    @Lock(lockType = LockType.REENTRANT, waitTime = 20, leaseTime = 10)
    public void secondReentrant(@Key String orderNo) {
        System.out.println("the second say hello");
    }

    @Lock(lockType = LockType.MULTI)
    public void multiKey(String param, @Key String key1, @Key String key2, @Key String key3) {
        log.info("test multi key");
    }
}
