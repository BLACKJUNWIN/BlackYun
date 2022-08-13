package com.black.common.handle;

import com.black.common.pojo.responseCode;
import com.black.common.pojo.responseJson;
import com.black.common.utils.tokenUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class tokenInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if (tokenUtils.verify(request.getHeader("Authorization"))) {
            return true;
        }
        response.setCharacterEncoding("utf-8");//设置编码
        response.setContentType("application/json");//设置传递格式
        response.getWriter().write(new responseJson(responseCode.FAIL_APPROVE).toString());//手动转json
        return false;
    }
}
