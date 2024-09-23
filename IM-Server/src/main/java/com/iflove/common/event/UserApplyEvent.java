package com.iflove.common.event;

import com.iflove.api.user.domain.entity.UserApply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Getter
public class UserApplyEvent extends ApplicationEvent {
    private final UserApply userApply;
    public UserApplyEvent(Object source, UserApply userApply) {
        super(source);
        this.userApply = userApply;
    }
}
