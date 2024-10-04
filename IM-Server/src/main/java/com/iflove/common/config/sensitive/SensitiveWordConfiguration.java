package com.iflove.common.config.sensitive;

import com.iflove.common.algorithm.sensitiveWord.DFAFilter;
import com.iflove.common.algorithm.sensitiveWord.SensitiveWordBs;
import com.iflove.sensitive.MyWordFactory;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SensitiveWordConfiguration {

    @Resource
    private MyWordFactory myWordFactory;

    /**
     * 初始化引导类
     *
     * @return 初始化引导类
     */
    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                .filterStrategy(DFAFilter.getInstance())
                .sensitiveWord(myWordFactory)
                .init();
    }

}