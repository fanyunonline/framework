package com.yyj.framework.common.util.file;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.yyj.framework.common.util.constant.CharSet;
import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yangyijun on 2018/5/24.
 */
public class CsvUtils {

    private static final Log logger = LogFactory.getLogger(CsvUtils.class);

    public static List<String[]> read(String filePath) {
        return read(filePath, CharSet.UTF8);
    }

    public static List<String[]> read(String filePath, CharSet charset) {
        List<String[]> datas = new LinkedList<>();
        CsvReader reader = null;
        try {
            reader = new CsvReader(filePath, ',', Charset.forName(charset.getCode()));
            while (reader.readRecord()) {
                datas.add(reader.getValues());
            }
        } catch (IOException e) {
            logger.error("read csv file [" + filePath + "] error ！", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                logger.error("read csv file [" + filePath + "] error ！", e);
            }
        }
        return datas;
    }

    public static boolean write(String filePath, List<String[]> contents, String[] headers) {
        return write(filePath, contents, headers, CharSet.UTF8);
    }

    public static boolean write(String filePath, List<String[]> contents, String[] headers, CharSet charSet) {
        CsvWriter csvWriter = null;
        try {
            csvWriter = new CsvWriter(filePath, ',', Charset.forName(charSet.getCode()));
            if (headers != null && headers.length > 0) {
                csvWriter.writeRecord(headers);
            }
            for (String[] line : contents) {
                csvWriter.writeRecord(line);
            }
            return true;
        } catch (IOException e) {
            logger.error("write csv file [" + filePath + "] error ！", e);
        } finally {
            try {
                if (csvWriter != null) {
                    csvWriter.close();
                }
            } catch (Exception e) {
                logger.error("write csv file [" + filePath + "] error ！", e);
            }
        }
        return false;
    }

    public static boolean append(String filePath, List<String[]> contents) {
        return append(filePath, contents, CharSet.UTF8);
    }

    public static boolean append(String filePath, List<String[]> contents, CharSet charSet) {
        CsvWriter csvWriter = null;
        boolean result = true;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (!file.isDirectory()) {
                    FileOutputStream fos = new FileOutputStream(file, true);
                    OutputStreamWriter os = new OutputStreamWriter(fos, charSet.getCode());
                    BufferedWriter out = new BufferedWriter(os, 1024);
                    csvWriter = new CsvWriter(out, ',');
                    for (String[] line : contents) {
                        csvWriter.writeRecord(line);
                    }
                } else {
                    logger.error("file [" + filePath + "] is a invalid csv file ！");
                    result = false;
                }
            } else {
                logger.error(" csv file [" + filePath + "] not found ！");
                result = false;
            }
        } catch (IOException e) {
            logger.error("write csv file [" + filePath + "] error ！", e);
            result = false;
        } finally {
            try {
                if (csvWriter != null) {
                    csvWriter.close();
                }
            } catch (Exception e) {
                logger.error("write csv file [" + filePath + "] error ！", e);
            }
        }
        return result;
    }
}
