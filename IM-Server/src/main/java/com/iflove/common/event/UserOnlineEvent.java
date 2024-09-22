package com.iflove.common.event;

import com.iflove.api.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 用户登录事件
 */
@Getter
public class UserOnlineEvent extends ApplicationEvent {
    private final User user;
    public UserOnlineEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
