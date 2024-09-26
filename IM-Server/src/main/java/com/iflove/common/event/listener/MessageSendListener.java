package com.iflove.common.event.listener;

import com.iflove.common.event.MessageSendEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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

//    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class)
//    public void onME
}
