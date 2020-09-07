package com.yyj.framework.spark.launcher;

import com.alibaba.fastjson.JSON;
import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by yangyijun on 2019/5/20.
 */
public class SparkActionLauncher extends ActionLauncher {

    private Log logger = LogFactory.getLogger(SparkActionLauncher.class);

    private Map<String, String> conf = new HashMap<>();
    private SparkAppHandle appMonitor;
    private boolean debug;

    public SparkActionLauncher(Map<String, String> actionConfig) {
        this.argumentValidator(actionConfig);
        this.conf.putAll(actionConfig);
    }

    /**
     * Submit spark application to hadoop cluster and wait for completion.
     *
     * @return
     */
    public boolean waitForCompletion() {
        boolean success = false;
        try {
            SparkLauncher launcher = this.createSparkLauncher();
            if (true) {//如果是debug模式，则会在控制台打印详细的提交信息
                Process process = launcher.launch();
                // Get Spark driver log
                new Thread(new ISRRunnable(process.getErrorStream())).start();
                new Thread(new ISRRunnable(process.getInputStream())).start();
                logger.info("****************************");
                logger.info("提交spark请求");
                int exitCode = process.waitFor();
                logger.info("请求结果状态码：" + exitCode);
                success = exitCode == 0 ? true : false;
            } else {
                appMonitor = launcher.setVerbose(true).startApplication();
                success = applicationMonitor();
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return success;
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private boolean applicationMonitor() {
        appMonitor.addListener(new SparkAppHandle.Listener() {
            @Override
            public void stateChanged(SparkAppHandle handle) {
                logger.info("****************************");
                logger.info("State Changed [state=" + handle.getState() + "]");
                logger.info("AppId=" + handle.getAppId());
            }

            @Override
            public void infoChanged(SparkAppHandle handle) {
            }
        });
        while (!isCompleted(appMonitor.getState())) {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        boolean success = appMonitor.getState() == SparkAppHandle.State.FINISHED;
        return success;
    }

    private boolean isCompleted(SparkAppHandle.State state) {
        switch (state) {
            case FINISHED:
                return true;
            case FAILED:
                return true;
            case KILLED:
                return true;
            case LOST:
                return true;
        }
        return false;
    }

    private SparkLauncher createSparkLauncher() {
        logger.info("actionConfig:\n" + JSON.toJSONString(conf, true));
        this.debug = Boolean.parseBoolean(conf.get(SparkConfig.DEBUG));
        Map<String, String> env = new HashMap<>();
        //配置hadoop的xml文件本地路径
        env.put(SparkConfig.HADOOP_CONF_DIR, conf.get(SparkConfig.HADOOP_CONF_DIR));
        //配置yarn的xml文件本地路径
        env.put(SparkConfig.YARN_CONF_DIR, conf.get(SparkConfig.HADOOP_CONF_DIR));
        SparkLauncher launcher = new SparkLauncher(env);
        //设置算法入口类所在的jar包本地路径
        launcher.setAppResource(conf.get(SparkConfig.APP_RESOURCE));
        //设置算法入口类保证包名称及类名，例：com.yyj.train.spark.launcher.TestSparkLauncher
        launcher.setMainClass(conf.get(SparkConfig.MAIN_CLASS));
        //设置集群的master地址：yarn／spark standalone的master地址，例：spark://hadoop01.xxx.xxx.com:7077
        launcher.setMaster(conf.get(SparkConfig.MASTER));
        //设置部署模式：cluster（集群模式）／client（客户端模式）
        launcher.setDeployMode(conf.get(SparkConfig.DEPLOY_MODE));
        //设置算法依赖的包的本地路径，多个jar包用逗号","隔开，如果是spark on yarn只需要把核心算法包放这里即可，
        // spark相关的依赖包可以预先上传到hdfs并通过 spark.yarn.jars参数指定；
        // 如果是spark standalone则需要把所有依赖的jar全部放在这里
        launcher.addJar(conf.get(SparkConfig.JARS));
        //设置应用的名称
        launcher.setAppName(conf.get(SparkConfig.APP_NAME));
        //设置spark客户端安装包的home目录，提交算法时需要借助bin目录下的spark-submit脚本
        launcher.setSparkHome(conf.get(SparkConfig.SPARK_HOME));
        //driver的内存设置
        launcher.addSparkArg(SparkConfig.DRIVER_MEMORY, conf.getOrDefault(SparkConfig.DRIVER_MEMORY, "4g"));
        //driver的CPU核数设置
        launcher.addSparkArg(SparkConfig.DRIVER_CORES, conf.getOrDefault(SparkConfig.DRIVER_CORES, "2"));
        //启动executor个数
        launcher.addSparkArg(SparkConfig.NUM_EXECUTOR, conf.getOrDefault(SparkConfig.NUM_EXECUTOR, "30"));
        //每个executor的CPU核数
        launcher.addSparkArg(SparkConfig.EXECUTOR_CORES, conf.getOrDefault(SparkConfig.EXECUTOR_CORES, "4"));
        //每个executor的内存大小
        launcher.addSparkArg(SparkConfig.EXECUTOR_MEMORY, conf.getOrDefault(SparkConfig.EXECUTOR_MEMORY, "4g"));
        String sparkYarnJars = conf.get(SparkConfig.SPARK_YARN_JARS);
        if (StringUtils.isNotBlank(sparkYarnJars)) {
            //如果是yarn的cluster模式需要通过此参数指定算法所有依赖包在hdfs上的路径
            launcher.setConf(SparkConfig.SPARK_YARN_JARS, conf.get(SparkConfig.SPARK_YARN_JARS));
        }
        //设置算法入口参数
        launcher.addAppArgs(new String[]{conf.get(SparkConfig.APP_ARGS)});
        return launcher;
    }

    private void argumentValidator(Map<String, String> conf) {
        Objects.requireNonNull(conf, "conf is null");
    }
}