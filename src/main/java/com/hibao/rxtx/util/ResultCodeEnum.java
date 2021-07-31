package com.hibao.rxtx.util;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    SUCCESS(true, 200, "成功"),
    UNKNOWN_ERROR(false, 500, "未知错误"),
    CODE_REPEAT(false, 20001, "编码重复"),
    OPERATE_SUCCESS(true, 200, "操作成功");

    // 响应是否成功
    private Boolean success;
    // 响应状态码
    private Integer code;
    // 响应信息
    private String message;

    ResultCodeEnum(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
