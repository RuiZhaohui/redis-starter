package geex.common.redis;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import geex.common.redis.annotation.EnableGeexRedisson;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

import java.io.IOException;

/**
 * //TODO description.
 *
 * @author JuChen
 * @date 2019/2/14
 */
@EnableGeexRedisson
@EnableCaching
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableApolloConfig(value = {"application", "architecture-group.redis"})
public class Main {
    public static void main(String[] args) throws IOException {

        new SpringApplicationBuilder(Main.class).run(args);
    }
}
