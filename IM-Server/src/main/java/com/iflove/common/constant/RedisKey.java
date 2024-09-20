package com.iflove.common.constant;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class RedisKey {
    private static final String BASE_KEY = "IM:";

    // Token 白名单
    public static final String JWT_BLACK_LIST = "jwt:blacklist:%s";
    // Token 黑名单
    public static final String JWT_WHITE_LIST = "jwt:whitelist:%s";
    // 在线用户列表
    public static final String ONLINE_UID_ZSET = "online";
    // 离线用户列表
    public static final String OFFLINE_UID_ZSET = "offline";
    // 用户信息
    public static final String USER_INFO_STRING = "userInfo:%d";

    public static String getKey(String key, Object... objects) {
        return BASE_KEY + String.format(key, objects);
    }
}
