package com.iflove.api.user.domain.enums;

import com.iflove.api.user.domain.vo.response.ws.WSFriendApply;
import com.iflove.api.user.domain.vo.response.ws.WSLoginSuccess;
import com.iflove.api.user.domain.vo.response.ws.WSMessage;
import com.iflove.api.user.domain.vo.response.ws.WSOnlineOfflineNotify;
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
    ONLINE_OFFLINE_NOTIFY(4, "上下限通知", WSOnlineOfflineNotify.class),
    FRIEND_APPLY(5, "好友申请", WSFriendApply.class),
    ;

    private final Integer type;
    private final String desc;
    private final Class<?> dataClass;

}
