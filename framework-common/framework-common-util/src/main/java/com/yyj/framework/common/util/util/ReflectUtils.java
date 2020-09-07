package com.yyj.framework.common.util.util;

/**
 * Created by yangyijun on 2018/5/24.
 */

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public final class ReflectUtils {

    public static <T> T invokeConstructor(Class<T> clazz, Class<?>[] argTypes, Object[] args)
            throws NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Constructor<T> constructor = clazz.getConstructor(argTypes);
        return constructor.newInstance(args);
    }

    public static <T> Object invokeGetter(T target, String fieldName)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        String methodName = "get" + StringUtils.capitalize(fieldName);
        Method method = target.getClass().getMethod(methodName);
        return method.invoke(target);
    }


    public static <T> void invokeSetter(T target, String fieldName, Object args)
            throws NoSuchFieldException, SecurityException,
            NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        String methodName = "set" + StringUtils.capitalize(fieldName);
        Class<?> clazz = target.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        Method method = clazz.getMethod(methodName, field.getType());
        method.invoke(target, args);
    }

    public static <T> boolean isDateType(Class<T> clazz, String fieldName) {
        boolean flag = false;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            Object typeObj = field.getType().newInstance();
            flag = typeObj instanceof Date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static Object parseType(String value, Class<?> type) {
        Object result = null;
        try {
            if (Boolean.TYPE == type) {
                result = Boolean.parseBoolean(value);
            } else if (Byte.TYPE == type) {
                result = Byte.parseByte(value);
            } else if (Short.TYPE == type) {
                result = Short.parseShort(value);
            } else if (Integer.TYPE == type) {
                result = Integer.parseInt(value);
            } else if (Long.TYPE == type) {
                result = Long.parseLong(value);
            } else if (Float.TYPE == type) {
                result = Float.parseFloat(value);
            } else if (Double.TYPE == type) {
                result = Double.parseDouble(value);
            } else {
                result = value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
