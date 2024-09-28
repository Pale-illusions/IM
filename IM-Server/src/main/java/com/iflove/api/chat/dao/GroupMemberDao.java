package com.iflove.api.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.chat.domain.entity.GroupMember;
import com.iflove.api.chat.mapper.GroupMemberMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author IFLOVE
* @description 针对表【group_member(群聊成员表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class GroupMemberDao extends ServiceImpl<GroupMemberMapper, GroupMember> {

    public List<Long> getMemberUidList(Long roomId) {
        return lambdaQuery()
                .eq(GroupMember::getGroupId, roomId)
                .select(GroupMember::getUserId)
                .list()
                .stream()
                .map(GroupMember::getUserId)
                .collect(Collectors.toList());
    }
}




