package com.iflove.im;

import cn.hutool.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test1() {
        String token = "";
        String jwtId = (String) JWTUtil.parseToken(token).getPayload("jwt_id");
        System.out.println(jwtId);
    }
}
