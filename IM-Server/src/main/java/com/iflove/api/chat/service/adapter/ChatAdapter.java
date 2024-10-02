package com.iflove.api.chat.service.adapter;

import com.iflove.api.chat.domain.entity.Room;
import com.iflove.api.chat.domain.entity.RoomFriend;
import com.iflove.api.chat.domain.entity.RoomGroup;
import com.iflove.api.chat.domain.enums.RoomTypeEnum;
import com.iflove.api.user.domain.entity.User;
import com.iflove.common.domain.enums.NormalOrNoEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class ChatAdapter {
    private static final String SEPARATOR = ",";

    public static String generateRoomKey(List<Long> uidList) {
        return uidList
                .stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(SEPARATOR));
    }

    public static Room buildRoom(RoomTypeEnum roomTypeEnum) {
        return Room
                .builder()
                .type(roomTypeEnum.getType())
                .build();
    }

    public static RoomFriend buildRoomFriend(Long roomId, List<Long> uidList) {
        List<Long> collect = uidList.stream().sorted().collect(Collectors.toList());
        return RoomFriend
                .builder()
                .roomId(roomId)
                .userId1(collect.get(0))
                .userId2(collect.get(1))
                .roomKey(generateRoomKey(collect))
                .status(NormalOrNoEnum.NORMAL.getStatus())
                .build();
    }

    public static RoomGroup buildRoomGroup(User user, Long id) {
        return RoomGroup.builder()
                .name(user.getName() + "的群组")
                .avatar(user.getAvatar())
                .roomId(id)
                .build();
    }
}
