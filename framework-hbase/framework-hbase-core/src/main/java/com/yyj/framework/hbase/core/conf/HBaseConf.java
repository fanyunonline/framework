package com.yyj.framework.hbase.core.conf;

import com.yyj.framework.common.core.context.Resource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 * Created by yangyijun on 2017/10/24.
 */
public class HBaseConf {

    public static Configuration create() {
        Configuration conf = HBaseConfiguration.create();
        conf.addResource(new Path(Resource.getPath("core-site.xml")));
        conf.addResource(new Path(Resource.getPath("hdfs-site.xml")));
        conf.addResource(new Path(Resource.getPath("hbase-site.xml")));
        return conf;
    }
}