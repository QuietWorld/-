package com.leyou.convert;

import org.springframework.beans.factory.config.RuntimeBeanNameReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期和字符的类型转换
 *
 * @author
 */
public class DateConvert {

    public static String dateToString(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                return sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("date不能为空");
    }

    public static Date stringToDate(String dateStr, String pattern){
        if (dateStr != null && !dateStr.equals("")){
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("字符串不能为null或者空字符串");
    }
}
