package geex.common.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * //TODO description.
 *
 * @author JuChen
 * @date 2019/2/18
 */
public class LockWorker implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(LockWorker.class);

    private int index;
    private PersonLockService personLockService;

    public LockWorker(int index) {
        this.index = index;
        personLockService = new PersonLockService();
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        log.info("current worker == {} ", index);
        personLockService.secondReentrant("201902181314");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info("lock success expire == " + (end - start));
    }
}
