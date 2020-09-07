package com.yyj.framework.common.util.file;

/**
 * Created by yangyijun on 2018/5/14.
 */

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

public final class ExcelUtils {
    public static <T> List<T> readExcel(Class<T> clazz, String filePath, String[] fieldNames, int sheetNo, boolean hasTitle) throws Exception {
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("file path not be empty");
        }
        File file = new File(filePath);
        if (file.exists()) {
            return ExcelHandler.readExcel(clazz, filePath, fieldNames, sheetNo, hasTitle);
        } else {
            throw new RuntimeException("file not exist");
        }
    }

    public static <T> List<T> readExcel(Class<T> clazz, String filePath, String[] fieldNames, boolean hasTitle) throws Exception {
        return readExcel(clazz, filePath, fieldNames, 0, hasTitle);
    }

    public static <T> List<T> readExcel(Class<T> clazz, String filePath, String[] fieldNames) throws Exception {
        return readExcel(clazz, filePath, fieldNames, 0, true);
    }

    public static List<Map<Integer, Object>> readExcel(String filePath, Class<?>... columnTypes) throws Exception {
        return readExcel(filePath, 0, 1, columnTypes);
    }

    public static List<Map<Integer, Object>> readExcel(String filePath, int sheetNo, int rowNo, Class<?>... columnTypes) throws Exception {
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("file path not be empty");
        }
        File file = new File(filePath);
        if (file.exists()) {
            return ExcelHandler.readExcel(filePath, sheetNo, rowNo, columnTypes);
        } else {
            throw new RuntimeException("file not exist");
        }
    }

    public static <T> void writeExcel(List<T> dataModels, String[] fieldNames, String[] titles, String filePath) throws Exception {
        ExcelHandler.writeExcel(dataModels, fieldNames, titles, filePath);
    }
}
