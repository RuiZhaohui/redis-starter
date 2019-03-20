package geex.common.redis.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * @author GEEX581
 */
@EnableApolloConfig(value = {"application", "architecture-group.redis"})
@Component("redissonConfig")
public class RedissonConfig {
    @Value("${geex.redisson.ttl}")
    private Long ttl;
    @Value("${geex.redisson.maxIdleTime}")
    private Long maxIdleTime;
    @Value("${geex.redisson.singleServerAddress:redis://localhost:6379}")
    private String singleServerAddress;
    @Value("${geex.redisson.clusterServerAddress:redis://localhost:6379}")
    private String[] clusterServerAddress;
    @Value("${geex.redisson.sentinelAddress:redis://localhost:6379}")
    private String[] sentinelAddress;
    @Value("${geex.redisson.slaveAddress:redis://localhost:6379}")
    private String[] slaveAddress;
    @Value("${geex.redisson.scanInterval:1000}")
    private int scanInterval;
    @Value("${geex.redisson.readMode:SLAVE}")
    private String readMode;
    @Value("${geex.redisson.connectionMinimumIdleSize:10}")
    private int connectionMinimumIdleSize;
    @Value("${geex.redisson.idleConnectionTimeout:10000}")
    private int idleConnectionTimeout;
    @Value("${geex.redisson.pingTimeout:1000}")
    private int pingTimeout;
    @Value("${geex.redisson.connectTimeout:10000}")
    private int connectTimeout;
    @Value("${geex.redisson.timeout:3000}")
    private int timeout;
    @Value("${geex.redisson.retryAttempts:3}")
    private int retryAttempts;
    @Value("${geex.redisson.retryInterval:1500}")
    private int retryInterval;
    @Value("${geex.redisson.failedSlaveReconnectionInterval:3000}")
    private int failedSlaveReconnectionInterval;
    @Value("${geex.redisson.failedSlaveCheckInterval:3}")
    private int failedSlaveCheckInterval;
    @Value("${geex.redisson.subscriptionsPerConnection:5}")
    private int subscriptionsPerConnection;
    @Value("${geex.redisson.subscriptionConnectionMinimumIdleSize:1}")
    private int subscriptionConnectionMinimumIdleSize;
    @Value("${geex.redisson.subscriptionConnectionPoolSize:50}")
    private int subscriptionConnectionPoolSize;
    @Value("${geex.redisson.connectionPoolSize:64}")
    private int connectionPoolSize;
    @Value("${geex.redisson.dnsMonitoringInterval:5000}")
    private int dnsMonitoringInterval;
    @Value("${geex.redisson.threads}")
    private int threads;
    @Value("${geex.redisson.codec:org.redisson.codec.JsonJacksonCodec}")
    private String codec;
    /**
     * 部署模式：
     * CLUSTER:集群模式
     * SINGLE：单节点模式
     * SENTINEL:哨兵模式
     * MASTERSLAVE:主从模式
     */
    @Value("${geex.redisson.deployMode}")
    private String deployMode;
    private Config config;

    public Config getClusterConfig() {
        config = new Config();
        ReadMode mode = getReadMode();
        config.useClusterServers().addNodeAddress(clusterServerAddress)
                .setScanInterval(scanInterval)
                .setReadMode(mode)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setMasterConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setSlaveConnectionPoolSize(connectionPoolSize)
                .setSubscriptionsPerConnection(subscriptionsPerConnection)
                .setFailedSlaveCheckInterval(failedSlaveCheckInterval)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setFailedSlaveReconnectionInterval(failedSlaveReconnectionInterval)
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPingTimeout(pingTimeout);
        initConfig();
        return config;
    }

    public Config getSentinelConfig() {
        config = new Config();
        config.useSentinelServers().setScanInterval(scanInterval).addSentinelAddress(sentinelAddress)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setMasterConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setSlaveConnectionPoolSize(connectionPoolSize)
                .setDnsMonitoringInterval(dnsMonitoringInterval)
                .setSubscriptionsPerConnection(subscriptionsPerConnection)
                .setFailedSlaveCheckInterval(failedSlaveCheckInterval)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setFailedSlaveReconnectionInterval(failedSlaveReconnectionInterval)
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPingTimeout(pingTimeout);
        initConfig();
        return config;
    }

    public Config getMasterSlaveConfig() {
        config = new Config();
        config.useMasterSlaveServers().addSlaveAddress(slaveAddress)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setMasterConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setSlaveConnectionPoolSize(connectionPoolSize)
                .setDnsMonitoringInterval(dnsMonitoringInterval)
                .setSubscriptionsPerConnection(subscriptionsPerConnection)
                .setFailedSlaveCheckInterval(failedSlaveCheckInterval)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setFailedSlaveReconnectionInterval(failedSlaveReconnectionInterval)
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPingTimeout(pingTimeout);
        initConfig();
        return config;
    }

    public Config getSingleConfig() {
        config = new Config();
        config.useSingleServer().setAddress(singleServerAddress).setDatabase(1);
        initConfig();
        return config;
    }

    private void initConfig() {
        Codec aCodec = null;
        try {
            aCodec = (Codec) ClassUtils.forName(codec, ClassUtils.getDefaultClassLoader()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        config.setCodec(aCodec);
        threads = threads <= 0 ? defaultThreads() * 2 : threads;
        config.setThreads(threads);
        config.setEventLoopGroup(new NioEventLoopGroup());
        config.setTransportMode(TransportMode.NIO);
    }

    private ReadMode getReadMode() {
        ReadMode mode;
        switch (readMode.toUpperCase()) {
            case "MASTER":
                mode = ReadMode.MASTER;
                break;
            case "MASTER_SLAVE":
                mode = ReadMode.MASTER_SLAVE;
                break;
            default:
                mode = ReadMode.SLAVE;
                break;
        }
        return mode;
    }

    public String getDeployMode() {
        return this.deployMode;
    }

    public Long getTtl() {
        return this.ttl;
    }

    public Long getMaxIdleTime() {
        return this.maxIdleTime;
    }

    private int defaultThreads() {
        return Runtime.getRuntime().availableProcessors() * 2;
    }
}
