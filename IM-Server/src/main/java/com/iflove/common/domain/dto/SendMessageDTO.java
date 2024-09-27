package com.iflove.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息发送数据传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageDTO implements Serializable {
    private Long msgId;
}
