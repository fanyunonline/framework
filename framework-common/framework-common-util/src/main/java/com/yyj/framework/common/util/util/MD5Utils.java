package com.yyj.framework.common.util.util;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yangyijun on 2017/12/12.
 */
public final class MD5Utils {

    private static Log logger = LogFactory.getLogger(MD5Utils.class);

    private MD5Utils() {
    }

    public static String md5(String source) {
        if (StringUtils.isBlank(source)) {
            return "";
        }
        StringBuffer sign = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes("utf-8"));
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 0) {
                    bt += 256;
                }
                if (bt < 16) {
                    sign.append(0);
                }
                sign.append(Integer.toHexString(bt));
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
        return sign.toString().toUpperCase();
    }

}
