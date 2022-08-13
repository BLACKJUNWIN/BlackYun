package com.black.common.pojo;

import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class responseJson{
    int code;
    String message;
    Object data;

    //失败反馈
    public responseJson(responseCode code){
        this.message=code.message;
        this.code=code.code;
        if(code==responseCode.SUCCESS){
            data="success";
        }else {
            data="error";
        }

    }

    public <T> responseJson(T data){
        this.message=responseCode.SUCCESS.message;
        this.code=responseCode.SUCCESS.code;
        this.data=data;
    }


}
