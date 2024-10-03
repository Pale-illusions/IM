package com.iflove.api.user.service.strategy;

import cn.hutool.core.bean.BeanUtil;
import com.iflove.api.user.dao.OfflineMessageDao;
import com.iflove.api.user.domain.entity.OfflineMessage;
import com.iflove.api.user.domain.enums.WSRespTypeEnum;
import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.api.user.service.PushService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public abstract class AbstractOfflineMsgHandler<Req> {
    @Resource
    private OfflineMessageDao offlineMessageDao;
    private Class<Req> bodyClass;

    /**
     * 初始化，工厂注册处理器
     */
    @PostConstruct
    private void init() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.bodyClass = (Class<Req>) genericSuperclass.getActualTypeArguments()[0];
        OfflineMsgHandlerFactory.register(getOfflineMsgTypeEnum().getType(), this);
    }

    /**
     * 获取消息类型
     * @return 消息类型
     */
    abstract WSRespTypeEnum getOfflineMsgTypeEnum();

    @Transactional
    public Long saveOfflineMsg(WSBaseResp<Req> req, Long uid) {
        Req body = BeanUtil.toBean(req.getData(), bodyClass);
        // 构造保存体
        OfflineMessage insert =  OfflineMessage.builder()
                .userId(uid)
                .type(req.getType())
                .build();
        //统一保存
        offlineMessageDao.save(insert);
        //子类扩展保存
        saveOfflineMsgExtra(insert, body);
        return insert.getId();
    }

    /**
     * 保存消息extra
     * @param message 消息
     * @param body 扩展信息
     */
    protected abstract void saveOfflineMsgExtra(OfflineMessage message, Req body);
}
