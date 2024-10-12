package com.iflove.api.video.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签表
 * @TableName tag
 */
@TableName(value ="tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标签名称
     */
    @TableField(value = "tag_name")
    private String tagName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static Tag init(String tagName) {
        Tag tag = new Tag();
        tag.setTagName(tagName);
        return tag;
    }
}