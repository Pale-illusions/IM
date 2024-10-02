package com.iflove.api.chat.consumer;

import com.iflove.api.chat.dao.ContactDao;
import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.dao.RoomDao;
import com.iflove.api.chat.dao.RoomFriendDao;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.entity.Room;
import com.iflove.api.chat.domain.entity.RoomFriend;
import com.iflove.api.chat.domain.enums.RoomTypeEnum;
import com.iflove.api.chat.domain.vo.response.ChatMessageResp;
import com.iflove.api.chat.service.ChatService;
import com.iflove.api.chat.service.cache.GroupMemberCache;
import com.iflove.api.chat.service.cache.RoomCache;
import com.iflove.api.user.domain.enums.WSRespTypeEnum;
import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.api.user.service.PushService;
import com.iflove.api.user.service.adapter.WSAdapter;
import com.iflove.common.constant.MQConstant;
import com.iflove.common.domain.dto.SendMessageDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.iflove.common.constant.MQConstant.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 响应消息发送消息队列
 */
@RocketMQMessageListener(consumerGroup = SEND_MSG_GROUP, topic = SEND_MSG_TOPIC)
@Component
@Slf4j
public class MsgSendConsumer implements RocketMQListener<SendMessageDTO> {
    @Resource
    private MessageDao messageDao;
    @Resource
    private RoomDao roomDao;
    @Resource
    private RoomCache roomCache;
    @Resource
    private RoomFriendDao roomFriendDao;
    @Resource
    private PushService pushService;
    @Resource
    private ChatService chatService;
    @Resource
    private GroupMemberCache groupMemberCache;
    @Resource
    private ContactDao contactDao;

    @Override
    public void onMessage(SendMessageDTO dto) {
        Message message = messageDao.getById(dto.getMsgId());
        Room room = roomCache.get(message.getRoomId());
        ChatMessageResp msgResp = chatService.getMsgResp(message);
        // 消息所在会话更新最新消息
        roomDao.refreshActiveTime(room.getId(), message.getId(), message.getCreateTime());
        roomCache.delete(room.getId());
        // 单聊群聊处理逻辑
        List<Long> memberUidList = new ArrayList<>();
        // 群聊推送所有群成员
        if (Objects.equals(room.getType(), RoomTypeEnum.GROUP.getType())) {
            memberUidList = groupMemberCache.getMemberUidList(room.getId());
        }
        // 单聊推送单人
        else if (Objects.equals(room.getType(), RoomTypeEnum.FRIEND.getType())) {
            RoomFriend roomFriend = roomFriendDao.getByRoomId(room.getId());
            memberUidList = List.of(roomFriend.getUserId1(), roomFriend.getUserId2());
        }
        // 更新所有群成员的会话时间
        contactDao.refreshOrCreateActiveTime(room.getId(), memberUidList, message.getId(), message.getCreateTime());
        // 推送消息
        pushService.sendPushMsg(WSAdapter.buildMsgSend(msgResp), memberUidList);
    }
}
