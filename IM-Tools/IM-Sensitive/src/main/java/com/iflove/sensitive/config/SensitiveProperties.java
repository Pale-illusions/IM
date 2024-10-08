package com.iflove.sensitive.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 敏感词配置属性
 */
@Data
@ConfigurationProperties(prefix = "sensitive")
public class SensitiveProperties {
    /**
     * 是否开启
     */
    private Boolean enabled;

    /**
     * 过滤算法类型
     * @see FilterType
     */
    private SensitiveProperties type;
}
