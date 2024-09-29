package com.iflove.api.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.iflove.api.chat.domain.entity.msg.MessageExtra;
import lombok.*;

/**
 * 消息表
 * @TableName message
 */
@TableName(value ="message", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话表id
     */
    private Long roomId;

    /**
     * 消息发送者uid
     */
    private Long fromUid;

    /**
     * 消息状态 0正常 1删除
     * @see com.iflove.api.chat.domain.enums.MessageStatusEnum
     */
    private Integer status;

    /**
     * 消息类型
     * @see com.iflove.api.chat.domain.enums.MessageTypeEnum
     */
    private Integer type;

    /**
     * 扩展信息
     * @see com.iflove.api.chat.domain.entity.msg.MessageExtra
     */
    @TableField(value = "extra", typeHandler = JacksonTypeHandler.class)
    private MessageExtra extra;

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