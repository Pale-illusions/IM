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
 * 群聊成员表
 * @TableName group_member
 */
@TableName(value ="group_member")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMember implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群主id
     */
    private Long groupId;

    /**
     * 成员uid
     */
    private Long userId;

    /**
     * 成员角色 1群主 2管理员 3普通成员
     */
    private Integer role;

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