package com.yyj.framework.hdfs;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by yangyijun on 2019/5/20.
 */
@Component
public class HDFSClient {
    private final static Log logger = LogFactory.getLogger(HDFSClient.class);

    @Value("${hadoop.user:dig}")
    private String hadoopUser;

    @Value("${hadoop.namenode:'hdfs://hadoop01.sz.haizhi.com:8020'}")
    private String nameNode;

    public void write(String file, String data) {
        write(nameNode, hadoopUser, file, data, false);
    }

    public void write(String file, String data, boolean overwrite) {
        write(nameNode, hadoopUser, file, data, overwrite);
    }

    public void write(String nameNode, String hadoopUser, String file, String data, boolean overwrite) {
        FileSystem fs = null;
        FSDataOutputStream out = null;
        try {
            fs = FileSystem.get(new URI(nameNode + file), HDFSConfigHelper.getConfig(), hadoopUser);
            Path path = new Path(nameNode + file);
            if (overwrite) {
                fs.deleteOnExit(path);
                out = fs.create(path);
            } else {
                if (fs.exists(path)) {
                    out = fs.append(path, 1024);
                } else {
                    out = fs.create(path);
                }
            }
            out.write(data.getBytes("UTF-8"));
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public void readAndPrint(String nameNode, String hadoopUser, String file) {
        FileSystem fs = null;
        FSDataInputStream in = null;
        try {
            fs = FileSystem.get(new URI(nameNode + file), HDFSConfigHelper.getConfig(), hadoopUser);
            Path path = new Path(file);
            in = fs.open(path);
            IOUtils.copyBytes(in, System.out, 4096, true);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public BufferedReader read(String nameNode, String hadoopUser, String file) {
        try {
            FileSystem fs = FileSystem.get(new URI(nameNode + file), HDFSConfigHelper.getConfig(), hadoopUser);
            Path path = new Path(file);
            FSDataInputStream in = fs.open(path);
            return new BufferedReader(new InputStreamReader(in));
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public FSDataInputStream readAndReturnInputStream(String nameNode, String hadoopUser, String file) {
        try {
            FileSystem fs = FileSystem.get(new URI(nameNode + file), HDFSConfigHelper.getConfig(), hadoopUser);
            Path path = new Path(file);
            return fs.open(path);
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public void delete(String nameNode, String hadoopUser, String file) {
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(nameNode + file), HDFSConfigHelper.getConfig(), hadoopUser);
            Path path = new Path(file);
            fs.delete(path, true);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public void create(String nameNode, String hadoopUser, String file, boolean overwrite) {
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(nameNode), HDFSConfigHelper.getConfig(), hadoopUser);
            Path path = new Path(file);
            fs.create(path, overwrite);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String nameNode = "hdfs://xxx:8020";
        String hadoopUser = "haizhi";
//        String file = "/test";
//        String data = "aa中国人民币恭贺够哦\n";
//        new HDFSClient().write(nameNode, hadoopUser, file, data, false);
//        BufferedReader br = new HDFSClient().read(nameNode, hadoopUser, file);
//        String line = null;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
//        new HDFSClient().readAndPrint(nameNode, hadoopUser, file);
//        new HDFSClient().delete(nameNode, hadoopUser, "/user/cmb_gp");

    }


}