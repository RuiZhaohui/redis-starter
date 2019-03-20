package geex.boot.redis.test.config;

import geex.common.redis.annotation.EnableGeexRedisson;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created with IntelliJ IDEA.
 * Date: 2019-01-29
 * Time: 14:15
 * Description:
 *
 * @author Leon
 */
@Configuration
@EnableGeexRedisson
@EnableScheduling
public class CustomizeConfig {
}
