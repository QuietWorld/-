package com.leyou.enums;


/**
 * 异常枚举
 * @author zc
 */
public enum ExceptionEnum {

    // 创建了一个枚举实例(构造方法赋值成员变量)
    PRICE_CANNOT_BE_NULL(400, "价格不能为空"),
    CATEGORY_NOT_FOUND(404,"商品分类信息查询失败"),
    BRAND_NOT_FOUND(404,"品牌信息没找到"),
    PID_CANNOT_BE_NULL(400,"PID不能为空"),
    ;
    /**
     * 响应状态码
     */
    private int statusCode;
    /**
     * 异常提示信息
     */
    private String msg;

    ExceptionEnum(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
