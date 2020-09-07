package com.yyj.framework.common.util.file;


import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by yangyijun on 2018/7/28.
 */
public final class ZipUtils {

    private static final Log logger = LogFactory.getLogger((ZipUtils.class));
    private static final int CACHE_SIZE = 1024;

    public static void zip(String sourceFolder, String zipFilePath) {
        OutputStream os = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;
        try {
            File zipFile = new File(zipFilePath);
            if (!zipFile.getParentFile().exists()) {
                zipFile.getParentFile().mkdirs();
            }
            os = new FileOutputStream(zipFilePath);
            bos = new BufferedOutputStream(os);
            zos = new ZipOutputStream(bos);
            File file = new File(sourceFolder);
            String basePath = null;
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            closeStream(os, bos, zos);
        }
    }

    private static void closeStream(OutputStream os, BufferedOutputStream bos, ZipOutputStream zos) {
        if (zos != null) {
            try {
                zos.closeEntry();
            } catch (IOException e) {
                logger.error(e);
            }
            try {
                zos.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    private static void zipFile(File parentFile, String basePath, ZipOutputStream zos) throws Exception {
        File[] files = new File[0];
        if (parentFile.isDirectory()) {
            files = parentFile.listFiles();
        } else {
            files = new File[1];
            files[0] = parentFile;
        }
        String pathName;
        InputStream is;
        BufferedInputStream bis;
        byte[] cache = new byte[CACHE_SIZE];
        for (File file : files) {
            if (file.isDirectory()) {
                pathName = file.getPath().substring(basePath.length() + 1) + File.separator;
                zos.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length() + 1);
                is = new FileInputStream(file);
                bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));
                int nRead = 0;
                while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                    zos.write(cache, 0, nRead);
                }
                bis.close();
                is.close();
            }
        }
    }
}
