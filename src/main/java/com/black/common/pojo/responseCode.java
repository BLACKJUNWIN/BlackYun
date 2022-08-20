package com.black.common.pojo;

import lombok.ToString;

@ToString
public enum responseCode {
    SUCCESS("操作成功", 200),
    FAIL("服务器层出错,缺少参数或者类型不对应", 500),
    FAIL_APPROVE("用户未登录", 403),
    NO_MONEY("余额不足",411),
    LONGIN_ERROR("用户名或密码错误",412),
    USER_EXIST("用户名已经存在",420),
    USER_NO_AUTHORITY("用户权限不够",421),
    NO_USER("操作对象不存在",422),
    FILE_FAIL("文件操作失败",423),
    FILE_NULL("文件不存在",425),
    FILE_EXIST("文件已存在",424),
    NAME_REPEAT("名字重复",426),
    OBJECT_EXIST("对象已存在,请勿重复添加",425);
    final String message;//状态
    final int code;//状态码

    responseCode(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getStatus() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
