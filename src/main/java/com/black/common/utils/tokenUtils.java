package com.black.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

public class tokenUtils {
    //过期时间毫秒
    private static final long EXPIRE_TIME = 1000*60*60*7;

    private static final String TOKEN_SECRET="BlackJun.cn-681324134125.xxxxtwA";

    public static String getToken(String userId){
        //设置过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        //token的密钥算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("type","JWT");
        header.put("alg","HMAC256");
        //携带用户id,并生成token
        return JWT.create()
                .withHeader(header)
                .withClaim("userId",userId)
                .withExpiresAt(date)
                .sign(algorithm);
    }
    //验证token
    public static boolean verify(String token){
        try {
            JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build().verify(moveHeader(token));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //移除Bearer
    private static String moveHeader(String token){
        if(token.contains("Bearer")){
            return token.substring("Bearer ".length(),token.length());
        }
        return token;
    }
    //通过token获取用户id
    public static Long getId(String token){
        DecodedJWT jwt = JWT.decode(moveHeader(token));
        return Long.valueOf(jwt.getClaim("userId").asString());
    }
    //通过request获取用户id
    public static Long requestGetId(HttpServletRequest request){
        return getId(request.getHeader("Authorization"));
    }
}
