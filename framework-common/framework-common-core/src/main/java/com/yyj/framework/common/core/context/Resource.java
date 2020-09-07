package com.yyj.framework.common.core.context;

import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.text.MessageFormat;

/**
 * Created by yangyijun on 2017/11/07.
 */
public class Resource {

    public static String getActiveProfile() {
        Environment env = SpringContext.getEnv();
        String activeProfile = env.getActiveProfiles()[0];
        return MessageFormat.format("application-{0}.properties", activeProfile);
    }

    public static String getPath(String resourceLocation){
        try {
            return ResourceUtils.getURL("classpath:" + resourceLocation).getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
