package com.iflove.transaction.config;

import cn.hutool.core.collection.CollectionUtil;
import com.iflove.transaction.annotation.SecureInvokeConfigurer;
import com.iflove.transaction.aspect.SecureInvokeAspect;
import com.iflove.transaction.dao.SecureInvokeRecordDao;
import com.iflove.transaction.mapper.SecureInvokeRecordMapper;
import com.iflove.transaction.service.MQProducer;
import com.iflove.transaction.service.SecureInvokeService;
import jakarta.annotation.Nullable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.function.SingletonSupplier;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 安全执行配置
 */
@Configuration
@EnableScheduling
@MapperScan(basePackageClasses = SecureInvokeRecordMapper.class)
@Import({SecureInvokeAspect.class, SecureInvokeRecordDao.class})
public class TransactionAutoConfiguration {
    @Nullable
    protected Executor executor;

    /**
     * Collect any {@link AsyncConfigurer} beans through autowiring.
     */
    @Autowired
    void setConfigurers(ObjectProvider<SecureInvokeConfigurer> configurers) {
        Supplier<SecureInvokeConfigurer> configurer = SingletonSupplier.of(() -> {
            List<SecureInvokeConfigurer> candidates = configurers.stream().collect(Collectors.toList());
            if (CollectionUtil.isEmpty(candidates)) {
                return null;
            }
            if (candidates.size() > 1) {
                throw new IllegalStateException("Only one SecureInvokeConfigurer may exist");
            }
            return candidates.get(0);
        });
        executor = Optional.ofNullable(configurer.get()).map(SecureInvokeConfigurer::getSecureInvokeExecutor).orElse(ForkJoinPool.commonPool());
    }

    @Bean
    public SecureInvokeService getSecureInvokeService(SecureInvokeRecordDao dao) {
        return new SecureInvokeService(dao, executor);
    }

    @Bean
    public MQProducer getMQProducer() {
        return new MQProducer();
    }
}
