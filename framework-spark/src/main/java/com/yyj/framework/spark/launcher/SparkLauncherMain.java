package com.yyj.framework.spark.launcher;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyijun on 2019/5/20.
 * 提交spark算法入口类
 */
public class SparkLauncherMain {

    public static void main(String[] args) {
        System.out.println("starting...");
        String confPath = "/Users/yyj/workspace/alg/src/main/resources";
        System.out.println("confPath=" + confPath);

        //开始构建提交spark时依赖的jars
        String rootPath = "/Users/yyj/workspace/alg/lib/";
        File file = new File(rootPath);
        StringBuilder sb = new StringBuilder();
        String[] files = file.list();
        for (String s : files) {
            if (s.endsWith(".jar")) {
                sb.append("hdfs://hadoop01.xxx.xxx.com:8020/user/alg/jars/");
                sb.append(s);
                sb.append(",");
            }
        }
        String jars = sb.toString();
        jars = jars.substring(0, jars.length() - 1);

        Map<String, String> conf = new HashMap<>();
        conf.put(SparkConfig.DEBUG, "false");
        conf.put(SparkConfig.APP_RESOURCE, "hdfs://hadoop01.xxx.xxx.com:8020/user/alg/jars/alg-gs-offline-1.0.0.jar");
        conf.put(SparkConfig.MAIN_CLASS, "com.yyj.alg.gs.offline.StartGraphSearchTest");
        conf.put(SparkConfig.MASTER, "yarn");
        //如果是提交到spark的standalone集群则采用下面的master
        //conf.put(SparkConfig.MASTER, "spark://hadoop01.xxx.xxx.com:7077");
        conf.put(SparkConfig.APP_NAME, "offline-graph-search");
        conf.put(SparkConfig.DEPLOY_MODE, "client");
        conf.put(SparkConfig.JARS, jars);
        conf.put(SparkConfig.HADOOP_CONF_DIR, confPath);
        conf.put(SparkConfig.YARN_CONF_DIR, confPath);
        conf.put(SparkConfig.SPARK_HOME, "/Users/yyj/spark2");
        conf.put(SparkConfig.DRIVER_MEMORY, "2g");
        conf.put(SparkConfig.EXECUTOR_CORES, "2");
        conf.put(SparkConfig.EXECUTOR_MEMORY, "2g");
        conf.put(SparkConfig.SPARK_YARN_JARS, "hdfs://hadoop01.xxx.xxx.com:8020/user/alg/jars/*.jar");
        conf.put(SparkConfig.APP_ARGS, "params");
        SparkActionLauncher launcher = new SparkActionLauncher(conf);
        boolean result = launcher.waitForCompletion();
        System.out.println("============result=" + result);
    }
}
