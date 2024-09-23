package com.iflove.im;

import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.iflove.api.user.domain.entity.User;
import com.iflove.common.utils.LambdaUtils;
import com.iflove.oss.MinIOTemplate;
import com.iflove.oss.domain.OssReq;
import com.iflove.oss.domain.OssResp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SpringBootTest
class ImApplicationTests {

    @Test
    void contextLoads() {


    }

//    @Test
//    public void test1() {
//        String token = "";
//        String jwtId = (String) JWTUtil.parseToken(token).getPayload("jwt_id");
//        System.out.println(jwtId);
//    }

    @Autowired
    private MinIOTemplate minIOTemplate;

    @Test
    public void getUploadUrl() {
        OssReq ossReq = OssReq.builder()
                .fileName("test.jpeg")
                .filePath("/test")
                .autoPath(false)
                .build();
        OssResp preSignedObjectUrl = minIOTemplate.getPreSignedObjectUrl(ossReq);
        System.out.println(preSignedObjectUrl);
    }

    @Test
    public void test2() {
        SFunction<User, ?> function = User::getId;
        Class<?> functionReturnType = getFunctionReturnType(function);
        System.out.println(functionReturnType);
    }

    public static Class<?> getFunctionReturnType(SFunction<?, ?> function) {
        Class<?> funtionClass = function.getClass();
        Type genericSuperclass = funtionClass.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) genericSuperclass;
            Type returnType = type.getActualTypeArguments()[1];
            if (returnType instanceof Class) {
                return (Class<?>) returnType;
            }
        }
        return null;
    }


    @Test
    public void test3() {
        SFunction<User, ?> function = User::getId;
        Class<?> returnType = LambdaUtils.getReturnType(function);
        System.out.println(returnType);
    }
}
