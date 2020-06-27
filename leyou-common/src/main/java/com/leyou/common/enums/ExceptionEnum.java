package com.leyou.common.enums;


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
    CATEGORY_SAVE_ERROR(500,"商品分类保存失败"),
    BRAND_SAVE_ERROR(500,"品牌保存失败"),
    INVALID_FILE_TYPE(400,"无效的文件类型"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    ILLEGAL_FILE_CONTENT(400,"文件内容不合法"),
    BRANDID_CANNOT_BE_NULL(400,"商品id不能为空"),
    BRAND_UPDATE_ERROR(500,"商品更新失败"),
    SPEC_PARAM_NOT_FOUND(404,"参数找不到"),
    SPEC_GROUP_NOT_FOUND(404,"规格组找不到"),
    SPEC_GROUP_SAVE_ERROR(500,"规格组保存失败"),
    SPEC_GROUP_DELETE_ERROR(500,"规格组删除失败"),
    SPEC_GROUP_UPDATE_ERROR(500,"规格组更新失败"),
    SPEC_PARAM_SAVE_ERROR(500,"规格组类参数保存失败"),
    SPEC_PARAM_UPDATE_ERROR(500,"规格组内参数更新失败"),
    SPEC_PARAM_DELETE_ERROR(500,"规格组内参数删除失败"),
    GOODS_NOT_FOUND(404,"商品查询失败"),
    GOODS_SAVE_ERROR(500,"商品保存失败"),
    SPUDETAIL_NOT_FOUND(404,"商品详细找不到"),
    SKU_NOT_FOUND(404,"SKU找不到"),
    GOODS_UPDATE_ERROR(500,"商品更新失败"),
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
