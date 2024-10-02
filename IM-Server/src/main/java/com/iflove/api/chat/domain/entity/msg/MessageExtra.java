package com.iflove.api.chat.domain.entity.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息扩展属性
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageExtra implements Serializable {
    private static final long serialVersionUID = 1L;

    // 文本消息
    private TextMsgDTO textMsgDTO;
    //文件消息
    private FileMsgDTO fileMsgDTO;
    //图片消息
    private ImgMsgDTO imgMsgDTO;
    //文件消息
    private VideoMsgDTO videoMsgDTO;
    // 系统消息
    private SystemMsgDTO systemMsgDTO;
}
