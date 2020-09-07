package com.yyj.framework.common.util.status;

/**
 * Created by yangyijun on 2018/5/15.
 */
public interface Status {
    /**
     * 各模块错误码定义范围
     * framework-common-util      11000~12000
     * framework-common-core      12001~13000
     * framework-es-core          13001~14000
     * framework-es-admin         14001~15000
     * framework-es-search        15001~16000
     * framework-hbase-core       16001~17000
     * framework-hbase-admin      17001~18000
     * framework-hbase-search     18001~19000
     * framework-kafka-core       19001~20000
     * framework-kafka-admin      20001~21000
     * framework-kafka-producer   21001~22000
     * framework-kafka-consumer   22001~23000
     * framework-arango-core      23001~24000
     * framework-arango-admin     24001~25000
     * framework-arango-search    25001~26000
     * framework-data-jpa         26001~27000
     * framework-data-mybatis     27001~28000
     * framework-task             28001~29000
     * framework-gateway          29001~30000
     * framework-register         30001~31000
     * framework-rocketmq         31001~32000
     * framework-zookeeper        32001~33000
     */

    int getCode();

    String getDesc();
}
