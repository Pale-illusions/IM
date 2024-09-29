package com.iflove.api.chat.service.strategy;

import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.entity.msg.MessageExtra;
import com.iflove.api.chat.domain.entity.msg.VideoMsgDTO;
import com.iflove.api.chat.domain.enums.MessageTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 视频消息处理
 */
@Component
public class VideoMsgHandler extends AbstractMsgHandler<VideoMsgDTO> {
    @Resource
    private MessageDao messageDao;

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.VIDEO;
    }

    @Override
    protected void saveMsg(Message message, VideoMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        Message update = new Message();
        update.setId(message.getId());
        update.setExtra(extra);
        extra.setVideoMsgDTO(body);
        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getFileMsgDTO();
    }

    @Override
    public String showContactMsg(Message msg) {
        return "[视频]";
    }
}
