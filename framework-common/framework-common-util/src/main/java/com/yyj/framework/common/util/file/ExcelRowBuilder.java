package com.yyj.framework.common.util.file;

import com.alibaba.fastjson.JSON;
import com.yyj.framework.common.util.util.ReflectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangyijun on 2018/5/25.
 */
public final class ExcelRowBuilder {
    public static <T> T build(Class<T> clazz, String[] fieldNames, Row row, int fieldCnt) throws InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        T target = clazz.newInstance();
        for (int j = 0; j < fieldCnt; j++) {
            String fieldName = fieldNames[j];
            if (fieldName == null) {
                continue;
            }
            Cell cell = row.getCell(j);
            if (cell == null) {
                continue;
            }
            Class<?> fieldType = clazz.getDeclaredField(fieldName).getType();
            if (ReflectUtils.isDateType(clazz, fieldName)) {
                ReflectUtils.invokeSetter(target, fieldName, cell.getDateCellValue());
            } else if (Boolean.TYPE == fieldType) {
                ReflectUtils.invokeSetter(target, fieldName, cell.getBooleanCellValue());
            } else if (Byte.TYPE == fieldType || Short.TYPE == fieldType || Integer.TYPE == fieldType || Long.TYPE == fieldType) {
                String value = String.valueOf((long) cell.getNumericCellValue());
                ReflectUtils.invokeSetter(target, fieldName, ReflectUtils.parseType(value, fieldType));
            } else {
                ReflectUtils.invokeSetter(target, fieldName, cell.getStringCellValue());
            }
        }
        return target;
    }

    public static Map<Integer, Object> build(Row row, Class<?>... columnTypes) throws InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        Map<Integer, Object> result = new LinkedHashMap<>();
        for (int i = 0; i < columnTypes.length; i++) {
            Class<?> columnType = columnTypes[i];
            Cell cell = row.getCell(i);
            if (cell == null) {
                result.put(i, cell);
                continue;
            }
            Object value;
            if (columnType == String.class) {
                value = cell.toString();
            } else {
                value = JSON.parseObject(cell.toString(), columnType);
            }
            result.put(i, value);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(JSON.parseObject("haaa", String.class));
    }
}
