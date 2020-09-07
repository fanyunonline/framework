package com.yyj.framework.common.util.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangyijun on 2017/12/15.
 */
public final class DateUtils {

    public static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String LOCAL_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    public static String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getCurrentDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String getYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(getDaysBefore(new Date(), 1));
    }

    public static Date getYearsBefore(Date d, int years) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) - years);
        return now.getTime();
    }

    public static Date getDaysBefore(Date d, int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - days);
        return now.getTime();
    }

    public static String formatLocal(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(LOCAL_FORMAT);
        return sdf.format(millis);
    }

    public static String formatLocal(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(LOCAL_FORMAT);
        return sdf.format(date);
    }

    public static String toUTC(String time) {
        return toUTC(toLocal(time));
    }

    public static String toUTC(Date date) {
        return DateFormatUtils.format(date, ISO_8601_FORMAT);
    }

    public static String utc2Local(String utcTime) {
        return utc2Local(utcTime, ISO_8601_FORMAT, LOCAL_FORMAT);
    }

    public static String utc2Local(String utcTime, String utcPatten, String localPatten) {
        Date utcDate = utc2Local(utcTime, utcPatten);
        if (utcDate == null) {
            return utcTime;
        }
        SimpleDateFormat local = new SimpleDateFormat(localPatten);
        local.setTimeZone(TimeZone.getDefault());
        String localTime = local.format(utcDate.getTime());
        return localTime;
    }

    public static Date utc2Local(String utcTime, String utcPatten) {
        SimpleDateFormat utc = new SimpleDateFormat(utcPatten);
        utc.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = utc.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcDate;
    }

    /**
     * Get local time millis(length=10).
     *
     * @param time
     * @return
     */
    public static long toLocalMillis(String time) {
        return toLocal(time).getTime() / 1000;
    }

    public static Date toLocal(String time) {
        if (StringUtils.isBlank(time)) {
            return new Date();
        }

        // UTC
        if (time.contains("T")) {
            return utc2Local(time, ISO_8601_FORMAT);
        }

        String format = "";
        time = time.replaceAll("[-|:| ]", "");
        if (time.length() == 8) {
            format = "yyyyMMdd";
        } else if (time.length() == 14) {
            format = "yyyyMMddHHmmss";
        }

        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    //适合带时区格式带日期，如：2016-08-15T16:00:00.000Z
    public static String format(String date, String inputFormat, String outFormat) {
        if (StringUtils.isBlank(date))
            return null;
        if (date.endsWith("Z"))
            date = date.replace("Z", " UTC");
        try {
            SimpleDateFormat parse = new SimpleDateFormat(inputFormat);
            Date d = parse.parse(date);
            SimpleDateFormat format = new SimpleDateFormat(outFormat);
            return format.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //自定义格式
    public static String format(Date date, String outFormat) {
        if (date == null)
            return null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(outFormat);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //适合带时区格式带日期，如：2016-08-15T16:00:00.000Z
    public static Date format(String date, String inputFormat) {
        if (StringUtils.isBlank(date))
            return null;
        if (date.endsWith("Z"))
            date = date.replace("Z", " UTC");
        try {
            SimpleDateFormat parse = new SimpleDateFormat(inputFormat);
            return parse.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean validDate(String strDate) {
        //Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Pattern pattern = Pattern.compile("^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$");
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }

    public static Date tryConvertDate(String value) {
        Date date = null;
        if (StringUtils.isBlank(value)) {
            return null;
        }
//        if (!DateUtils.validDate(value)) {
//            return date;
//        }
        try {
            SimpleDateFormat simpleDateFormat = null;
            if (value.length() == LOCAL_FORMAT.length()) {
                simpleDateFormat = new SimpleDateFormat(LOCAL_FORMAT);
            } else if (value.length() == LOCAL_DATE_FORMAT.length()) {
                simpleDateFormat = new SimpleDateFormat(LOCAL_DATE_FORMAT);
            }
            if (null == simpleDateFormat) {
                return null;
            }
            date = simpleDateFormat.parse(value);
        } catch (ParseException e) {

        }
        return date;

    }
}
