package com.iflove.im;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.enums.MessageStatusEnum;
import com.iflove.api.chat.service.adapter.MessageAdapter;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.vo.request.friend.FriendCheckReq;
import com.iflove.api.user.domain.vo.response.friend.FriendCheckResp;
import com.iflove.api.user.domain.vo.response.friend.FriendInfoResp;
import com.iflove.api.user.service.FriendService;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.utils.IPUtils;
import com.iflove.common.utils.LambdaUtils;
import com.iflove.oss.MinIOTemplate;
import com.iflove.oss.domain.OssReq;
import com.iflove.oss.domain.OssResp;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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



    @Resource
    FriendService friendService;


    @Test
    public void test4() {
        FriendCheckReq friendCheckReq = new FriendCheckReq(List.of(1L, 2L));
        RestBean<FriendCheckResp> check = friendService.check(1L, friendCheckReq);
        System.out.println();
        System.out.println(check);
    }

    @Resource
    MessageDao messageDao;

    @Test
    public void test5() {
        Message message = Message
                .builder()
                .fromUid(1L)
                .roomId(1L)
                .type(0)
                .status(MessageStatusEnum.NORMAL.getStatus())
                .build();
        messageDao.save(message);
        System.out.println(message);
    }

    @Test
    public void test6() {
        Object a = "123";
        if (String.class.isAssignableFrom(a.getClass())) {
            System.out.println((String) a);
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
        Object b = new Message();
        if (Message.class.isAssignableFrom(b.getClass())) {
            System.out.println((Message) b);
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }


}
