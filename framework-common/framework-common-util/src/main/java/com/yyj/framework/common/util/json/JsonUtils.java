package com.yyj.framework.common.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yyj.framework.common.util.http.RequestWrapper;
import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * Created by yangyijun on 2018/4/12.
 */
public class JsonUtils {

    private static final Log logger = LogFactory.getLogger(RequestWrapper.class);

    public static <T> Map<String, Object> toMap(T bean) {
        if (null == bean) {
            return new HashMap<>();
        }
        String text = toJSONString(bean);
        return JSON.parseObject(text, new TypeReference<Map<String, Object>>() {
        });
    }

    public static <T> List<Map<String, Object>> toListMap(List<T> beans) {
        String text = toJSONString(beans);
        return JSON.parseObject(text, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    public static Map<String, Object> jsonToMap(String json) {
        return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
        });
    }

    public static List<Map<String, Object>> jsonToListMap(String json) {
        return JSON.parseObject(json, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    public static List<Map<String, Object>> toListMap(Object obj) {
        if (null == obj) {
            return new ArrayList<>();
        }
        String json = toJSONString(obj);
        return JSON.parseObject(json, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    public static final void print(Object obj) {
        logger.info(JSON.toJSONString(obj, true));
    }

    public static Object toBean(String text) {
        return JSON.parse(text);
    }

    public static <T> T toBean(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    public static <T> T toBean(byte[] text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    public static <T> T toBean(String text, Type type) {
        return JSON.parseObject(text, type);
    }

    public static <T> T toBean(String text, TypeReference<T> type) {
        return JSON.parseObject(text, type);
    }

    public static Object[] toArray(String text) {
        return toArray(text, null);
    }

    public static <T> T[] toArray(String text, Class<T> clazz) {
        return (T[]) toList(text, clazz).toArray();
    }

    public static final <T> List<T> toList(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        return JSONArray.parseArray(obj.toString(), clazz);
    }

    public static final String toString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static final String toString(Object obj, SerializerFeature... feature) {
        return JSON.toJSONString(obj, feature);
    }

    public static final String toStringWithNull(Object obj) {
        return JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);
    }

    public static final String toStringPretty(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.PrettyFormat);
    }

}
