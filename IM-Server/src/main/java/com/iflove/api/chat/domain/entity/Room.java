package com.iflove.api.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iflove.api.chat.domain.enums.RoomTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 房间表
 * @TableName room
 */
@TableName(value ="room")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话类型 1 群聊 2 单聊
     */
    private Integer type;

    /**
     * 最后活跃时间-排序
     */
    private Date activeTime;

    /**
     * 最后一条消息id
     */
    private Long lastMsgId;

    /**
     * 逻辑删除(0-正常,1-删除)
     */
    @TableField("delete_status")
    @TableLogic(value = "0", delval = "1")
    private Integer deleteStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    public boolean isRoomFriend() {
        return RoomTypeEnum.of(type).equals(RoomTypeEnum.FRIEND);
    }

    @JsonIgnore
    public boolean isRoomGroup() {
        return RoomTypeEnum.of(type).equals(RoomTypeEnum.GROUP);
    }
}