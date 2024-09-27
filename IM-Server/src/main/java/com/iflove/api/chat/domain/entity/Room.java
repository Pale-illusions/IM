package com.iflove.api.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

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