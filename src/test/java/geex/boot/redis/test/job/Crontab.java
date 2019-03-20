package geex.boot.redis.test.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Date: 2019-01-08
 * Time: 17:44
 * Description:
 *
 * @author Leon
 */
@Component
public class Crontab {

    private final static Logger log = LoggerFactory.getLogger(Crontab.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(fixedDelay = 5000)
    public void redisDelay() {
        this.redisTemplate.opsForValue().set("test", new Date());
        Date date = (Date) this.redisTemplate.opsForValue().get("test");
        log.info(date.toString());
        this.redisTemplate.delete("test");
    }
}
