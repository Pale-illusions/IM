package com.iflove.im;

import com.iflove.common.constant.RedisKey;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import utils.RedisUtil;

import java.util.Set;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@SpringBootTest
public class RedisTest {

    @Test
    public void test1() {
        Double v = RedisUtil.zIncrementScore("test", "test", -1);
        System.out.println(v);

        Boolean set = RedisUtil.set("test2", "test2");
        System.out.println(set);

        Boolean test2 = RedisUtil.del("test2");
        System.out.println(test2);


    }

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void test2() {
        // FIXME 为什么？
        RedisUtil.sSet(RedisKey.getKey(RedisKey.VIDEO_LIKE_RELATION), "2::8");
        boolean b2 = RedisUtil.sHasKey(RedisKey.getKey(RedisKey.VIDEO_LIKE_RELATION), "2::8");
        System.out.println(b2);

//        Set<String> relationKey = RedisUtil.sGet(RedisKey.getKey(RedisKey.VIDEO_LIKE_RELATION));
//        relationKey.forEach(System.out::println);

        Long relationKey1 = RedisUtil.setRemove(RedisKey.getKey(RedisKey.VIDEO_LIKE_RELATION), "2::8");
        System.out.println(relationKey1);
    }
}
