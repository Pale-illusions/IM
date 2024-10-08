package com.iflove.sensitive.config;

import com.iflove.sensitive.algorithm.sensitiveWord.filter.ACFilter;
import com.iflove.sensitive.algorithm.sensitiveWord.filter.DFAFilter;
import com.iflove.sensitive.algorithm.sensitiveWord.filter.SensitiveWordFilter;
import com.iflove.sensitive.service.SensitiveWordBs;
import com.iflove.sensitive.mapper.SensitiveWordMapper;
import com.iflove.sensitive.service.MyWordFactory;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(SensitiveProperties.class)
@MapperScan(basePackageClasses = SensitiveWordMapper.class)
public class SensitiveWordConfiguration {
    @Resource
    private MyWordFactory myWordFactory;

    /**
     * 初始化引导类
     * 禁用敏感词过滤
     * @return 初始化引导类
     */
    @Bean
    @ConditionalOnExpression("!${sensitive.enabled}")
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                .filterStrategy(new SensitiveWordFilter() {
                    @Override
                    public boolean hasSensitiveWord(String text) {
                        return false;
                    }

                    @Override
                    public String filter(String text) {
                        return text;
                    }

                    @Override
                    public void loadWord(List<String> words) {
                    }
                })
                .sensitiveWord(ArrayList::new)
                .init();
    }

    /**
     * 初始化引导类
     * 算法：DFA
     * @return 初始化引导类
     */
    @Bean
    @ConditionalOnExpression("${sensitive.enabled}")
    @ConditionalOnProperty(value = "sensitive.type", havingValue = "DFA")
    public SensitiveWordBs sensitiveWordBsDFA() {
        return SensitiveWordBs.newInstance()
                .filterStrategy(DFAFilter.getInstance())
                .sensitiveWord(myWordFactory)
                .init();
    }

    /**
     * 初始化引导类
     * 算法：AC自动机
     * @return 初始化引导类
     */
    @Bean
    @ConditionalOnExpression("${sensitive.enabled}")
    @ConditionalOnProperty(value = "sensitive.type", havingValue = "AC")
    public SensitiveWordBs sensitiveWordBsAC() {
        return SensitiveWordBs.newInstance()
                .filterStrategy(new ACFilter())
                .sensitiveWord(myWordFactory)
                .init();
    }
}