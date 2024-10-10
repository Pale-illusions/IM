package com.iflove.api.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.user.domain.entity.OfflineMessage;
import com.iflove.api.user.mapper.OfflineMessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class OfflineMessageDao extends ServiceImpl<OfflineMessageMapper, OfflineMessage> {

    /**
     * 获取离线消息列表
     * @param uid
     * @return
     */
    public List<OfflineMessage> listByUserId(Long uid) {
        return lambdaQuery()
                .eq(OfflineMessage::getUserId, uid)
                .list();
    }
}
