package edu.fzu.qujing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    /**
     * 仅显示年月日，例如 2015-08-11.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 显示年月日时分秒，例如 2015-08-11 09:51:53.
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String HMS_FORMAT = "HH:mm:ss.SSS";

    public static String getDate() {
        Calendar calendar = new GregorianCalendar();
        Date date = new Date();
        calendar.setTime(date);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(calendar.get(Calendar.YEAR));
        stringBuilder.append(calendar.get(Calendar.MONTH) + 1);
        stringBuilder.append(calendar.get(Calendar.DATE));
        stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY));
        stringBuilder.append(calendar.get(Calendar.MINUTE));
        stringBuilder.append(calendar.get(Calendar.SECOND));
        return stringBuilder.toString();


    }

    public static String formatDateTime(long time, String fomat) {

        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(fomat);
        String str = sdf.format(date);
        return str;
    }

    public static Date stringToDate(String date, String fomat) {
        SimpleDateFormat sdf = new SimpleDateFormat(fomat);
        Date parse = null;
        try {
            parse = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static String getYMD(String time) {
        String trim = time.trim();
        String[] s = trim.split(" ");
        return s[0];
    }

    public static void main(String[] args) {
        String[] split = "yuo to 2014-12-34 12:54:66".split("to");
        System.out.println(split[1]);
    }
}
