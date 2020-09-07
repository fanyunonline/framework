package com.yyj.framework.rocketmq.core;

/**
 * @description RocketMQ的NameServer配置地址
 */
public class NameServerAddress {
    private String addresses;

    public String getAddresses() {
        return addresses;
    }

    public NameServerAddress(String addresses) {
        this.addresses = addresses;
    }
}
