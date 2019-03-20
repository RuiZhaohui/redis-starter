package geex.common.redis.common;

/**
 * 部署模式
 *
 * @author GEEX581
 */
public enum DeployMode {
    /**
     * 集群模式
     */
    CLUSTER(0, "CLUSTER"),
    /**
     * 单节点模式
     */
    SINGLE(0, "SINGLE"),
    /**
     * 哨兵模式
     */
    SENTINEL(0, "SENTINEL"),
    /**
     * 主从模式
     */
    MASTERSLAVE(0, "MASTERSLAVE");
    private int index;
    private String action;

    DeployMode(int index, String action) {
        this.index = index;
        this.action = action;
    }

    public int getIndex() {
        return index;
    }

    public String getAction() {
        return action;
    }
}
