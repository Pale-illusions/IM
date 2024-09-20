package com.iflove.common.domain.dto;

import com.iflove.user.domain.enums.WSPushTypeEnum;
import com.iflove.user.domain.vo.response.ws.WSBaseResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息推送对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushMessageDTO implements Serializable {
    // 推送的 ws 消息
    private WSBaseResp<?> wsBaseResp;
    // 推送的uid
    private List<Long> uidList;
    /**
     * 推送类型 1 个人 / 2 全体
     *
     * @see com.iflove.user.domain.enums.WSPushTypeEnum
     */
    private Integer pushType;

    public PushMessageDTO(WSBaseResp<?> wsBaseResp, Long uid) {
        this.wsBaseResp = wsBaseResp;
        this.uidList = Collections.singletonList(uid);
        this.pushType = WSPushTypeEnum.USER.getType();
    }

    public PushMessageDTO(WSBaseResp<?> wsBaseResp, List<Long> uidList) {
        this.wsBaseResp = wsBaseResp;
        this.uidList = uidList;
        this.pushType = WSPushTypeEnum.USER.getType();
    }

    public PushMessageDTO(WSBaseResp<?> wsBaseResp) {
        this.wsBaseResp = wsBaseResp;
        this.pushType = WSPushTypeEnum.ALL.getType();
    }
}
