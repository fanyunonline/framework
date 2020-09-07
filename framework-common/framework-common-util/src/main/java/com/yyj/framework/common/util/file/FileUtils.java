package com.yyj.framework.common.util.file;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2018/5/14.
 */
public final class FileUtils {

    private static final Log logger = LogFactory.getLogger(FileUtils.class);

    public static Map<String, String> upload(MultipartFile file, String uploadPath, boolean overwrite) {
        String fileName = file.getOriginalFilename();
        String uploadFilePath = uploadPath + "/" + fileName;
        File dest = new File(uploadFilePath);
        if (dest.exists()) {
            if (overwrite) {
                dest.delete();
            } else {
                logger.error("file {0} already exist", fileName);
                return null;
            }
        }
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            //file.transferTo(dest);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(file.getInputStream(), dest);
            Map<String, String> result = new HashMap<>();
            result.put(fileName, uploadFilePath);
            return result;
        } catch (Exception e) {
            logger.error("upload file {0} error", e, fileName);
        }
        return null;
    }

    public static String upload(MultipartFile file, String uploadPath) {
        String fileName = file.getOriginalFilename();
        String uploadFilePath = uploadPath + "/" + fileName;
        File dest = new File(uploadFilePath);
        if (dest.exists()) {
            dest.delete();
        }
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            //file.transferTo(dest);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(file.getInputStream(), dest);
        } catch (Exception e) {
            logger.error("upload file {0} error", e, fileName);
        }
        return uploadFilePath;
    }

    public static String getProjectRootPath() {
        return System.getProperty("user.dir");
    }

    public static Map<String, String> multiUpload(List<MultipartFile> files, String uploadPath, boolean overwrite) {
        Map<String, String> result = new HashMap<String, String>();
        for (MultipartFile file : files) {
            result.putAll(upload(file, uploadPath, overwrite));
        }
        return result;
    }

    public static void download(HttpServletResponse response, String filePath) {
        File file = new File(filePath);
        System.out.println(file.getName());
        if (file.exists()) {
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                logger.error("download file {0} error", e, filePath);
            } finally {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error(e);
                }
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error(e);
                }
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
    }

    public static boolean delete(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        return true;
    }

    public static boolean deleteDir(String dir) {
        File file = new File(dir);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    if (f.listFiles().length == 0) {
                        f.delete();
                        continue;
                    }
                    deleteDir(f.getAbsolutePath());
                } else {
                    f.delete();
                }
            }
            file.delete();
        }
        return true;
    }

    public static void main(String[] args) {
        deleteDir("/Users/haizhi/upload/tag/companys-1");
    }
}
