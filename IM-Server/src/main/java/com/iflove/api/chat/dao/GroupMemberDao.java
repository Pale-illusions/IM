package com.iflove.api.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.chat.domain.entity.GroupMember;
import com.iflove.api.chat.domain.enums.GroupRoleEnum;
import com.iflove.api.chat.mapper.GroupMemberMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author IFLOVE
* @description 针对表【group_member(群聊成员表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class GroupMemberDao extends ServiceImpl<GroupMemberMapper, GroupMember> {

    public List<Long> getMemberUidList(Long groupId) {
        return lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .select(GroupMember::getUserId)
                .list()
                .stream()
                .map(GroupMember::getUserId)
                .collect(Collectors.toList());
    }

    public GroupMember getMember(Long groupId, Long uid) {
        return lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getUserId, uid)
                .one();
    }

    public Map<Long, Integer> getMemberMapRole(Long groupId, List<Long> uidList) {
        List<GroupMember> list = lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .in(GroupMember::getUserId, uidList)
                .select(GroupMember::getUserId, GroupMember::getRole)
                .list();
        return list.stream().collect(Collectors.toMap(GroupMember::getUserId, GroupMember::getRole));
    }

    public void addAdmin(Long groupId, List<Long> uidList) {
        List<Long> distinctUidList = uidList.stream().distinct().collect(Collectors.toList());;
        lambdaUpdate()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getRole, GroupRoleEnum.MEMBER.getType())
                .in(GroupMember::getUserId, distinctUidList)
                .set(GroupMember::getRole, GroupRoleEnum.MANAGER.getType())
                .update();
    }

    public void revokeAdmin(Long groupId, List<Long> uidList) {
        List<Long> distinctUidList = uidList.stream().distinct().collect(Collectors.toList());;
        lambdaUpdate()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getRole, GroupRoleEnum.MANAGER.getType())
                .in(GroupMember::getUserId, distinctUidList)
                .set(GroupMember::getRole, GroupRoleEnum.MEMBER.getType())
                .update();
    }
}




