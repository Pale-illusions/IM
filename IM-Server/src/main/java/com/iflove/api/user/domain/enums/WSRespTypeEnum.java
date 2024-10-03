package com.iflove.api.user.domain.enums;

import com.iflove.api.user.domain.vo.response.ws.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote ws 后端返回类型枚举
 */
@AllArgsConstructor
@Getter
public enum WSRespTypeEnum {
    LOGIN_SUCCESS(1, "登录成功", WSLoginSuccess.class),
    MESSAGE(2, "新消息", WSMessage.class),
    INVALID_TOKEN(3, "非法Token, 登录失败", null),
    ONLINE_OFFLINE_NOTIFY(4, "上下线通知", WSOnlineOfflineNotify.class),
    FRIEND_APPLY(5, "好友申请", WSFriendApply.class),
    MEMBER_CHANGE(6, "群成员变动", WSMemberChange.class),
    GROUP_DISMISSED(7, "群聊解散", WSGroupDismissedResp.class),
    ;

    private final Integer type;
    private final String desc;
    private final Class<?> dataClass;

}
