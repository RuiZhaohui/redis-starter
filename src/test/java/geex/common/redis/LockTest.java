package geex.common.redis;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import geex.common.redis.service.LockWorker;
import geex.common.redis.service.PersonLockService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

/**
 * //TODO description.
 *
 * @author JuChen
 * @date 2019/2/15
 */
public class LockTest extends TestBase {
    private static final Logger log = LoggerFactory.getLogger(LockTest.class);

    @Autowired
    private PersonLockService personLockService;
    private static ExecutorService executorServicePool;

    @Test
    public void testReenterLock() throws InterruptedException {
        initThread();
        for (int i = 0; i < 50; i++) {
            executorServicePool.execute(new LockWorker(i));
        }
        executorServicePool.shutdown();
        while (!executorServicePool.awaitTermination(1, TimeUnit.SECONDS)) {
            log.info("worker running");
        }
    }

    @Test
    public void testMultiKey() {
        personLockService.multiKey("", "oneKey", "twoKey", "threeKey");
    }

    public static void initThread() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("current-thread-%d").build();
        executorServicePool = new ThreadPoolExecutor(350, 350, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(200), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}
