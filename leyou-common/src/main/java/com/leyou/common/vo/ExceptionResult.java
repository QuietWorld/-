package com.leyou.common.vo;


import com.leyou.common.convert.DateConvert;
import com.leyou.common.enums.ExceptionEnum;

import java.util.Date;

/**
 * 封装异常信息的实体类
 * @author zc
 */
public class ExceptionResult {

    private int statusCode;
    private String msg;
    private String time;


    public ExceptionResult(ExceptionEnum em) {
        this.statusCode = em.getStatusCode();
        this.msg = em.getMsg();
        Date date = new Date();
        this.time =  DateConvert.dateToString(date, "yyyy-MM-dd hh:mm:ss");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
