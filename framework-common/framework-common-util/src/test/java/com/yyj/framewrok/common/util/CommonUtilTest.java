package com.yyj.framewrok.common.util;

import com.yyj.framework.common.util.json.JsonUtils;
import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.common.util.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.yyj.framework.common.util.util.DateUtils.*;

/**
 * Created by yangyijun on 2018/12/22.
 */
public class CommonUtilTest {

    @Test
    public void textFormat() {
        System.out.println(MessageFormat.format("test message format name {0},age {1}", "yangyijun", "28"));
    }

    @Test
    public void print() {
        Response response = new Response();
        JsonUtils.print(response);
    }

    @Test
    public void dateUtils() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));//UTC为时间统一时间，北京时间比UTC快8小时；如UTC时间为2018-12-12 02:02:02 则北京时间为2018-12-12 10:02:02
        Date local = df.parse("2014-08-23T09:20:05Z");
        System.out.println(DateUtils.formatLocal(local));

        local = df.parse("2010-03-05T16:00:00Z");
        System.out.println(DateUtils.formatLocal(local));

        System.out.println(DateUtils.toUTC(new Date()));
        local = DateUtils.utc2Local("2010-03-05T16:05:01.000Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        System.out.println(DateUtils.formatLocal(local));
        System.out.println(DateUtils.utc2Local("2018-05-01T10:00:03.000Z",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", LOCAL_FORMAT));
        System.out.println(DateUtils.utc2Local("2018-05-01T10:00:03Z"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        System.out.println(sdf.format(new Date()));
        System.out.println(toUTC(getDaysBefore(new Date(), 3)));
        System.out.println(toUTC(getYearsBefore(new Date(), 3)));
        System.out.println(StringUtils.substringBefore(toUTC(new Date()), ":"));
        System.out.println(getYesterday());
        System.out.println(DateUtils.utc2Local(DateUtils.toUTC(new Date()), "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd HH:mm:ss"));

    }
}
