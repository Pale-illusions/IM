package com.iflove.transaction.dao;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.transaction.domain.entity.SecureInvokeRecord;
import com.iflove.transaction.mapper.SecureInvokeRecordMapper;
import com.iflove.transaction.service.SecureInvokeService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class SecureInvokeRecordDao extends ServiceImpl<SecureInvokeRecordMapper, SecureInvokeRecord> {
    public List<SecureInvokeRecord> getWaitRetryRecords() {
        Date now = new Date();
        // 查找两分钟前的失败数据，避免刚入库的数据被查出来
        DateTime dateTime = DateUtil.offsetMinute(now, -(int) SecureInvokeService.RETRY_INTERVAL_MINUTES);
        return lambdaQuery()
                .eq(SecureInvokeRecord::getStatus, SecureInvokeRecord.STATUS_WAIT)
                .lt(SecureInvokeRecord::getNextRetryTime, new Date())
                .lt(SecureInvokeRecord::getCreateTime, dateTime)
                .list();
    }
}
