package com.iflove.api.chat.service.strategy;

import cn.hutool.core.bean.BeanUtil;
import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.enums.MessageTypeEnum;
import com.iflove.api.chat.domain.vo.request.msg.ChatMessageReq;
import com.iflove.api.chat.service.adapter.MessageAdapter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息处理器抽象类
 */
public abstract class AbstractMsgHandler<Req> {
    @Resource
    private MessageDao messageDao;
    private Class<Req> bodyClass;
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 初始化，工厂注册处理器
     */
    @PostConstruct
    private void init() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.bodyClass = (Class<Req>) genericSuperclass.getActualTypeArguments()[0];
        MsgHandlerFactory.register(getMsgTypeEnum().getType(), this);
    }

    /**
     * 获取消息类型
     * @return 消息类型
     */
    abstract MessageTypeEnum getMsgTypeEnum();

    @Transactional
    public Long checkAndSaveMsg(ChatMessageReq req, Long uid) {
        Req body = BeanUtil.toBean(req.getBody(), bodyClass);
        // 校验参数合法性
        checkValidateThrow(body);
        // 构造保存体
        Message insert = MessageAdapter.buildMsgSave(req, uid);
        //统一保存
        messageDao.save(insert);
        //子类扩展保存
        saveMsg(insert, body);
        return insert.getId();
    }

    /**
     * 注解验证参数
     * @param body 待校验请求体
     */
    private void checkValidateThrow(Req body) {
        Set<ConstraintViolation<Req>> constraintViolations =  validator.validate(body);
        if (!constraintViolations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder();
            Iterator<ConstraintViolation<Req>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<Req> violation = iterator.next();
                //拼接异常信息
                errorMsg.append(violation.getPropertyPath().toString()).append(":").append(violation.getMessage()).append(",");
            }
            //去掉最后一个逗号
            throw new ValidationException(errorMsg.substring(0, errorMsg.length() - 1));
        }
    }

    /**
     * 保存消息extra
     * @param message 消息
     * @param body 扩展信息
     */
    protected abstract void saveMsg(Message message, Req body);

    /**
     * 展示消息
     */
    public abstract Object showMsg(Message msg);

    /**
     * 会话列表——展示的消息
     */
    public abstract String showContactMsg(Message msg);
}
