package com.iflove.api.chat.consumer;

import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.dao.RoomDao;
import com.iflove.api.chat.dao.RoomFriendDao;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.entity.Room;
import com.iflove.api.chat.domain.entity.RoomFriend;
import com.iflove.api.chat.domain.vo.response.ChatMessageResp;
import com.iflove.api.user.domain.enums.WSRespTypeEnum;
import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.api.user.service.PushService;
import com.iflove.api.user.service.adapter.WSAdapter;
import com.iflove.common.constant.MQConstant;
import com.iflove.common.domain.dto.SendMessageDTO;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.iflove.common.constant.MQConstant.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 响应消息发送消息队列
 */

// TODO 消息发送消息队列
@RocketMQMessageListener(consumerGroup = SEND_MSG_GROUP, topic = SEND_MSG_TOPIC)
@Component
public class MsgSendConsumer implements RocketMQListener<SendMessageDTO> {
    @Resource
    MessageDao messageDao;
    @Resource
    RoomDao roomDao;
    @Resource
    RoomFriendDao roomFriendDao;
    @Resource
    PushService pushService;

    @Override
    public void onMessage(SendMessageDTO dto) {
        // TODO 消息所在房间相关业务逻辑

        // TODO 单聊群聊处理逻辑

        // TODO 使用message缓存查询

        // 初步测试：单聊处理
        Message message = messageDao.getById(dto.getMsgId());
        // TODO 使用room cache 缓存查询
        Room room = roomDao.getById(message.getRoomId());
        // 对单聊做测试
        RoomFriend roomFriend = roomFriendDao.getByRoomId(room.getId());
        List<Long> lst = List.of(roomFriend.getUserId1(), roomFriend.getUserId2());

        // TODO 更新会话时间

        // TODO 使用 ChatMessageResp 作为返回消息体
        // 发送消息
        WSBaseResp<Message> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.MESSAGE.getType());
        resp.setData(message);
        pushService.sendPushMsg(resp, lst);

    }
}
