#   一、模块规划说明：
    |-framework
        |-framework-common
            |-framework-common-util       公共核心模块，工具类
            |-framework-common-core       公共的逻辑处理模块
        |-framework-es
            |-framework-es-admin          elasticsarch管理模块
            |-framework-es-core           elasticsarch连接管理模块
            |-framework-es-search         elasticsarch搜索模块
        |-framework-hbase
            |-framework-hbase-admin       hbase管理模块
            |-framework-hbase-core        hbase数据库连接管理模块
            |-framework-hbase-search      hbase搜索模块
        |-framework-kafka
            |-framework-kafka-admin       kafka的topic管理模块
            |-framework-kafka-core        kafka的通用配置管理模块
            |-framework-kafka-producer    kafka的生产者管理模块
            |-framework-kafka-consumer    kafka的消费者管理模块
        |-framework-task                  任务调度模块
        |-framework-eureka                spring cloud服务注册中心
        |-framework-zuul                  spring cloud网关

# 二、端口规划说明：
    |-framework-arango
        |-framework-arango-admin          8080
        |-framework-arango-search         8081
    |-framework-es
        |-framework-es-admin              8082
        |-framework-es-search             8083
    |-framework-hbase
        |-framework-hbase-admin           8084
        |-framework-hbase-search          8085
    |-framework-kafka
        |-framework-kafka-admin           8086
        |-framework-kafka-producer        8087
        |-framework-kafka-consumer        8088

# 三、项目打包命令：
    #打包项目
    mvn clean package -Pdeploy -Dmaven.test.skip=true
    
    #部署所有依赖包到maven私服
    mvn clean deploy -Pdeploy -Dmaven.test.skip=true
    
    #当使用此插件在父Maven项目时，运行如下命令将更新全部项目的版本号，包括子项目之间的依赖也都同步更新
    mvn versions:set -DnewVersion=1.0.0
    
    #当进入到子Maven项目时，运行如下命令将更新全部项目对项目引用的版本号
    mvn versions:set -DnewVersion=1.0.0
    
    #当更改版本号时有问题，可以通过以下命令进行版本号回滚
    mvn versions:revert
    
    #如果一切都没有问题，那就直接提交版本号
    mvn versions:commit

# 四、模块错误码规划：
    |-framework-common-util      11000~12000
    |-framework-common-core      12001~13000
    |-framework-es-core          13001~14000
    |-framework-es-admin         14001~15000
    |-framework-es-search        15001~16000
    |-framework-hbase-core       16001~17000
    |-framework-hbase-admin      17001~18000
    |-framework-hbase-search     18001~19000
    |-framework-kafka-core       19001~20000
    |-framework-kafka-admin      20001~21000
    |-framework-kafka-producer   21001~22000
    |-framework-kafka-producer   22001~23000
    |-framework-task             28001~29000
    |-framework-zookeeper        29001~30000

# 五、swagger地址规划：
    |-framework-es
        |-framework-es-admin
            http://localhost:8082/es/admin/swagger-ui.html
        |-framework-es-search
            http://localhost:8083/es/search/swagger-ui.html
    |-framework-hbase
        |-framework-hbase-admin
            http://localhost:8084/hbase/admin/swagger-ui.html
        |-framework-hbase-search
            http://localhost:8085/hbase/search/swagger-ui.html
    |-framework-kafka
        |-framework-kafka-admin
        |-framework-kafka-producer
        |-framework-kafka-consumer

# 六.GIT版本管理规范
## 6.1.版本结构说明：A.B.C
    A：大功能变更；
    B：添加新特性；
    C：BUG修复；
## 6.2.样例
    |-master                           
    |-1.0.0.release                    
    |-develop                          
    |-feature/yangyijun(git用户名称)    
    |-hotfix                           

    # master
        最新稳定版本，只能由管理员进行合并最新release分枝到master分枝；
    # release
        发行稳定版本，每次发行版本需要合并到master分枝；
    # develop
        当前开发版本，各成员基于此分枝进行clone出各自的开发feature分枝，此分枝只允许合并操作，不允许直接在此分枝上进行开发；
    # feature
        各成员开发功能基于develop分枝创建各自的feature分枝，单元测试完成后合并到develop分枝；
    # hotfix    
        紧急bug修复分支，在最新的release分支上创建。