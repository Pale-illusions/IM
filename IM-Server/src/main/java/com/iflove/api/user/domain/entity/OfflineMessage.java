package com.iflove.api.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.iflove.api.user.domain.vo.response.ws.OfflineMessageExtra;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@TableName(value = "offline_message", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OfflineMessage implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息发送对象id
     */
    private Long userId;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 消息内容
     * @see OfflineMessageExtra
     */
    @TableField(value = "data", typeHandler = JacksonTypeHandler.class)
    private OfflineMessageExtra data;

    /**
     * 逻辑删除 0 正常 / 1 删除
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
}
