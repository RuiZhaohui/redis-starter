package geex.common.redis.common;

public interface LockCommonConstant {
    /**
     * 默认客户端名字
     */
    String LOCK = "Lock";
    /**
     * 默认SSL实现方式：JDK
     */
    String JDK = "JDK";
    /**
     * 逗号
     */
    String COMMA = ",";
    /**
     * 冒号
     */
    String COLON = ":";
    /**
     * 分号
     */
    String SEMICOLON = ";";
    /**
     * redis默认URL前缀
     */
    String REDIS_URL_PREFIX = "redis://";
    /**
     * 锁的前缀
     */
    String KEY_PREFIX="lock:key:";
}
