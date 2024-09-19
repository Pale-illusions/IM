package com.iflove.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 单聊表
 * @TableName room_friend
 */
@TableName(value ="room_friend")
@Data
public class RoomFriend implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * uid1（更小的uid）
     */
    private Long userId1;

    /**
     * uid2（更大的uid）
     */
    private Long userId2;

    /**
     * 房间key由两个uid拼接，先做排序uid1_uid2
     */
    private String roomKey;

    /**
     * 房间状态 0正常 1禁用(删好友了禁用)
     */
    private Integer status;

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
}