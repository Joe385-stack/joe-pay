package com.ruoyi.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by M. on 2021/4/20.
 */
public class DateTimeUtils {

    private static final Logger log = LoggerFactory.getLogger(DateTimeUtils.class);

    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_PATTERN_ = "yyyyMMdd";
    public static final String DEFAULT_DATE_PATTERN__ = "yyyyMMddHHmmss";
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
    public static final String DEFAULT_DATE_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String SIMPLE_MONTH_DATE_PATTERN = "M月d日";
    public static final String DEFAULT_DATE_HOUR_PATTERN = "yyyy-MM-dd HH";
    public static final String SIMPLE_MONTH_DATE_PATTERN1 = "yyyy年MM月dd日";
    public static final String DATE_HOUR_PATTERN = "yyyyMMddHH";
    public static final String DATE_FORMAT_YEAR_MONTH = "yyyyMM";
    public static final String SIMPLE_MONTH_DATE_PATTERN2 = "MM月dd日 HH:mm:ss";
    public static final String MINUTE_PATTERN = "mm";


    //一小时的秒数
    private static final int HOUR_SECOND = 60 * 60;
    //一分钟的秒数
    private static final int MINUTE_SECOND = 60;

    private static final ZoneId zoneId = ZoneId.systemDefault();

    public static Date convertStrToDate(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            log.warn("convertDate fail, date is " + dateStr, e);
        }
        return null;
    }

    public static Date convertStrToDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.DEFAULT_DATETIME_PATTERN);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            log.warn("convertDate fail, date is " + dateStr, e);
        }
        return null;
    }

    public static String convertDateToStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date getDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return convertStrToDate(dateStr, "yyyy-MM-dd");
    }

    public static int compareDay(Date date1, Date date2) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date1);
        int day1 = cl.get(Calendar.DAY_OF_YEAR);
        int year1 = cl.get(Calendar.YEAR);
        cl.setTime(date2);
        int day2 = cl.get(Calendar.DAY_OF_YEAR);
        int year2 = cl.get(Calendar.YEAR);
        if (year1 == year2) {
            return day1 - day2;
        } else {
            date1 = getDate(date1);
            date2 = getDate(date2);
            return (int) ((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
        }
    }

    public static Date getBeginTimeOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }


    public static Date getEndTimeOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);

        return calendar.getTime();
    }

    /**
     * 获取n天之前的日期
     * @param day
     * @return
     */
    public static Date getLastDate(Date date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    public static Date getBeginTimeOfLastDate(Date date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndTimeOfLastDate(Date date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getBeginTimeOfDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndTimeOfDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static LocalDateTime getDateTime(String dateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN);
        return LocalDateTime.parse(dateTime, df);
    }

    public static LocalDateTime convertDate2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 把日期按照指定格式的转化成字符串
     * @param date 日期对象
     * @param formatStr 日期格式
     * @return 字符串式的日期,格式为：yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeToString(Date date,String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    public static long getDiffHour(Date date1, Date date2){
        long diff = date1.getTime() - date2.getTime();
        return diff/(60*60*1000);
    }
}
