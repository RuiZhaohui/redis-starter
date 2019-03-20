package geex.common.redis.cache;

import geex.common.redis.config.RedissonConfig;
import geex.common.redis.core.LockInterceptor;
import geex.common.redis.factory.ServiceBeanFactory;
import geex.common.redis.service.impl.FairLockServiceImpl;
import geex.common.redis.service.impl.MultiLockServiceImpl;
import geex.common.redis.service.impl.ReadLockServiceImpl;
import geex.common.redis.service.impl.RedLockServiceImpl;
import geex.common.redis.service.impl.ReentrantLockServiceImpl;
import geex.common.redis.service.impl.WriteLockServiceImpl;
import geex.common.redis.utils.SpringUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static geex.common.redis.common.DeployMode.CLUSTER;
import static geex.common.redis.common.DeployMode.MASTERSLAVE;
import static geex.common.redis.common.DeployMode.SENTINEL;
import static geex.common.redis.common.DeployMode.SINGLE;

/**
 * @author GEEX581
 */
@EnableCaching
@SpringBootConfiguration
@Import(LockInterceptor.class)
public class RedissonCacheConfig extends CachingConfigurerSupport {
    private static final Logger log = LoggerFactory.getLogger(RedissonCacheConfig.class);
    @Autowired
    private RedissonConfig redissonConfig;

    @Bean
    CacheManager cacheManager(@Qualifier("lockRedissonClient") RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>(1);
        config.put("redissonCache", new CacheConfig(this.redissonConfig.getTtl(), this.redissonConfig.getMaxIdleTime()));
        RedissonSpringCacheManager redissonSpringCacheManager = new RedissonSpringCacheManager(redissonClient, config);
        log.info("Redisson cache manager: {}", redissonSpringCacheManager);
        return redissonSpringCacheManager;
    }


    @Primary
    @Bean(name = "lockRedissonClient", destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        String deployMode = this.redissonConfig.getDeployMode();
        Objects.requireNonNull(deployMode, "deploy model can not be null, Please check config in Apollo!");
        Config config = null;
        if (deployMode.toUpperCase().equals(CLUSTER.getAction())) {
            log.info("use cluster mode");
            config = redissonConfig.getClusterConfig();
        }
        if (deployMode.toUpperCase().equals(SINGLE.getAction())) {
            log.warn("use single mode on product");
            config = redissonConfig.getSingleConfig();
        } else if (deployMode.toUpperCase().equals(SENTINEL.getAction())) {
            log.info("use sentinel mode");
            config = redissonConfig.getSentinelConfig();
        } else if (deployMode.toUpperCase().equals(MASTERSLAVE.getAction())) {
            log.info("use master slave mode");
            config = redissonConfig.getMasterSlaveConfig();
        }
        if (deployMode.toUpperCase().equals(SENTINEL.getAction())) {
            log.info("use sentinel mode");
            config = redissonConfig.getSentinelConfig();
        }
        if (deployMode.toUpperCase().equals(MASTERSLAVE.getAction())) {
            log.info("use master slave mode");
            config = redissonConfig.getMasterSlaveConfig();
        }
        Objects.requireNonNull(config, "error creating config.");
        return Redisson.create(config);
    }

    @Bean
    public ServiceBeanFactory serviceBeanFactory() {
        return new ServiceBeanFactory();
    }

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    public RedissonConfig redissonConfig() {
        return new RedissonConfig();
    }

    @Bean
    @Scope("prototype")
    public ReentrantLockServiceImpl reentrantLockServiceImpl() {
        return new ReentrantLockServiceImpl();
    }

    @Bean
    @Scope("prototype")
    public FairLockServiceImpl fairLockServiceImpl() {
        return new FairLockServiceImpl();
    }

    @Bean
    @Scope("prototype")
    public MultiLockServiceImpl multiLockServiceImpl() {
        return new MultiLockServiceImpl();
    }

    @Bean
    @Scope("prototype")
    public RedLockServiceImpl redLockServiceImpl() {
        return new RedLockServiceImpl();
    }

    @Bean
    @Scope("prototype")
    public ReadLockServiceImpl readLockServiceImpl() {
        return new ReadLockServiceImpl();
    }

    @Bean
    @Scope("prototype")
    public WriteLockServiceImpl writeLockServiceImpl() {
        return new WriteLockServiceImpl();
    }
}
