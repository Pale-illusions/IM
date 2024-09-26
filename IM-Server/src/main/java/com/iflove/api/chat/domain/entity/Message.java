package com.iflove.api.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

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
     * 消息内容
     */
    private String content;

    /**
     * 回复的消息id
     */
    private Long replyMsgId;

    /**
     * 消息状态 0正常 1删除
     */
    private Integer status;

    /**
     * 与回复的消息间隔多少条
     */
    private Integer gapCount;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 扩展信息
     */
    private Object extra;

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