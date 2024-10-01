package com.iflove.common.event.listener;

import com.iflove.common.constant.MQConstant;
import com.iflove.common.domain.dto.SendMessageDTO;
import com.iflove.common.event.MessageSendEvent;
import com.iflove.transaction.service.MQProducer;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class MessageSendListener {
    @Resource
    MQProducer mqProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Long msgId = event.getMsgId();
        // 发送安全消息
        mqProducer.sendSecureMsg(MQConstant.SEND_MSG_TOPIC, new SendMessageDTO(msgId), msgId);
    }
}
