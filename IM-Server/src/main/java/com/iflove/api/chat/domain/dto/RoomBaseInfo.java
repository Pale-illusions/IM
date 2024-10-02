package com.iflove.api.chat.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 房间基本信息
 */
@Data
@Builder
public class RoomBaseInfo {
    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 会话名称
     */
    private String name;

    /**
     * 会话头像
     */
    private String avatar;

    /**
     * 房间类型 1群聊 2单聊
     */
    private Integer type;

    /**
     * 群最后消息的更新时间
     */
    private Date activeTime;

    /**
     * 最后一条消息id
     */
    private Long lastMsgId;
}
