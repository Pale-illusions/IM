package com.iflove.im;

import cn.hutool.jwt.JWTUtil;
import com.iflove.oss.MinIOTemplate;
import com.iflove.oss.domain.OssReq;
import com.iflove.oss.domain.OssResp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
