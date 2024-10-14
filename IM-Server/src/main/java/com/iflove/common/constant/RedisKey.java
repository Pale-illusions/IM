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
    public static final String USER_INFO_STRING = "userInfo:uid_%d";
    // 房间信息
    public static final String ROOM_INFO_STRING = "roomInfo:roomId_%d";
    // 单聊房间信息
    public static final String ROOM_FRIEND_STRING = "room:roomFriend:roomId_%d";
    // 群聊房间信息
    public static final String ROOM_GROUP_STRING = "room:roomGroup:roomId_%d";
    // 视频信息
    public static final String VIDEO_INFO_STRING = "video:info:videoId_%d";
    // 视频点击量
    public static final String VIDEO_CLICK_COUNT ="video:clickCount";
    // 视频点赞量
    public static final String VIDEO_LIKE_COUNT ="video:likeCount";
    // 视频点踩量
    public static final String VIDEO_DISLIKE_COUNT ="video:dislikeCount";
    // 用户 - 视频 点赞关系映射  格式 222222::333333
    public static final String VIDEO_LIKE_RELATION = "video:like:relation:userId_%d";
    // 用户 - 视频 点踩关系映射  格式 222222::333333
    public static final String VIDEO_DISLIKE_RELATION = "video:dislike:relation:userId_%d";
    // 视频分数计算
    public static final String VIDEO_SCORE_COMPUTEWAIT = "video:score:computeWait";


    public static String getKey(String key, Object... objects) {
        return BASE_KEY + String.format(key, objects);
    }
}
