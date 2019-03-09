package io.four.config;

/**
 * Base config
 */
public class BaseConfig {
    protected String serviceId;
    protected String alias;
    protected String timeout;

    public String getServiceId() {
        return serviceId;
    }

    public BaseConfig setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public BaseConfig setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getTimeout() {
        return timeout;
    }

    public BaseConfig setTimeout(String timeout) {
        this.timeout = timeout;
        return this;
    }

    public static BaseConfig CONSUMER = new BaseConfig().setAlias("alias");
}
