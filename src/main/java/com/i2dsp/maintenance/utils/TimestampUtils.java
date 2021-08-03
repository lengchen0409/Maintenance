package com.i2dsp.maintenance.utils;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * description 时间工具类
 * date: 2021-07-01 10:22
 *
 * @author 林隆星
 */
public class TimestampUtils{
    /**
     * 获取String类型当前时间戳
     * @return
     */
    public static String getCurrentTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 获取当前时间的年份
     * @author: 梁海聪
     * @return
     */
    public static String getCurrentYear() {
        Calendar data = Calendar.getInstance();
        String year = String.valueOf(data.get(Calendar.YEAR));
        return year;
    }

    /**
     * 获取当前年份1月1日的时间戳
     * @author: 梁海聪
     * @return
     */
    public static Long getAppointTimestamp() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), 0, 1,0,0,0);
        return cal.getTimeInMillis();
    }

}
