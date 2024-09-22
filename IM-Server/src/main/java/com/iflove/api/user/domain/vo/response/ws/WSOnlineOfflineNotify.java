package com.iflove.api.user.domain.vo.response.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WSOnlineOfflineNotify {
    // 新的上下线用户
    private WSChatMemberResp change;
    // 在线人数
    private Long onlineNum;
}
