package com.iflove.user.domain.vo.response.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 聊天成员基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WSChatMemberResp {
    private Long uid;

    /**
     * 0 在线 / 1 离线
     *
     * @see com.iflove.user.domain.enums.ChatActiveStatusEnum
     */
    private Integer activeStatus;

    // 最后一次上下线时间
    private Date lastOptTime;
}
