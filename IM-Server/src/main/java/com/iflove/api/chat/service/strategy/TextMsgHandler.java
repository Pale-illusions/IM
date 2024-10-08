package com.iflove.api.chat.service.strategy;

import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.entity.msg.MessageExtra;
import com.iflove.api.chat.domain.entity.msg.TextMsgDTO;
import com.iflove.api.chat.domain.enums.MessageTypeEnum;
import com.iflove.sensitive.service.SensitiveWordBs;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 文本消息处理
 */
@Component
public class TextMsgHandler extends AbstractMsgHandler<TextMsgDTO> {
    @Resource
    private MessageDao messageDao;
    @Resource
    private SensitiveWordBs sensitiveWordBs;

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.TEXT;
    }

    @Override
    protected void saveMsg(Message message, TextMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        Message update = new Message();
        update.setId(message.getId());
        update.setExtra(extra);
        // 敏感词过滤
        body.setContent(sensitiveWordBs.filter(body.getContent()));
        extra.setTextMsgDTO(body);
        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getTextMsgDTO();
    }

    @Override
    public String showContactMsg(Message msg) {
        return msg.getExtra().getTextMsgDTO().getContent();
    }
}
