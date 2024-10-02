package com.iflove.api.chat.service.strategy;

import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.entity.msg.MessageExtra;
import com.iflove.api.chat.domain.entity.msg.SystemMsgDTO;
import com.iflove.api.chat.domain.enums.MessageTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 系统消息处理
 */
@Component
public class SystemMsgHandler extends AbstractMsgHandler<SystemMsgDTO> {
    @Resource
    private MessageDao messageDao;

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.SYSTEM;
    }

    @Override
    protected void saveMsg(Message message, SystemMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        Message update = new Message();
        update.setId(message.getId());
        update.setExtra(extra);
        extra.setSystemMsgDTO(body);
        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getSystemMsgDTO();
    }

    @Override
    public String showContactMsg(Message msg) {
        return msg.getExtra().getSystemMsgDTO().getContent();
    }
}
