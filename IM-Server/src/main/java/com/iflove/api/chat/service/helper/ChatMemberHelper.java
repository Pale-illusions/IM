package com.iflove.api.chat.service.helper;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.iflove.api.user.domain.enums.ChatActiveStatusEnum;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 成员列表工具类
 */
public class ChatMemberHelper {
    private static final String SEPARATOR = "_";

    /**
     * 1. cursor 为 null
     *      第1次查询, 认为需要查询的为在线列表, 默认 activeStatusEnum 为 Online, timeCursor 为 null
     * 2. cursor 不为 null
     *      第n次查询, 认为需要查询的信息保存在cursor中, 解析cursor, 获得 activeStatusEnum 和 timeCursor
     */
    public static Pair<ChatActiveStatusEnum, String> getCursorPair(String cursor) {
        ChatActiveStatusEnum activeStatusEnum = ChatActiveStatusEnum.ONLINE;
        String timeCursor = null;
        if (StrUtil.isNotBlank(cursor)) {
            String activeStr = cursor.split(SEPARATOR)[0];
            String timeStr = cursor.split(SEPARATOR)[1];
            activeStatusEnum = ChatActiveStatusEnum.of(Integer.parseInt(activeStr));
            timeCursor = timeStr;
        }
        return Pair.of(activeStatusEnum, timeCursor);
    }

    /**
     * 生成 cursor 返回前端
     * 格式为: { activeStatusEnum_timeCursor }
     */
    public static String generateCursor(ChatActiveStatusEnum activeStatusEnum, String timeCursor) {
        return activeStatusEnum.getStatus() + SEPARATOR + timeCursor;
    }
}
