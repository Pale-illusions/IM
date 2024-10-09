package com.iflove.api.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IpInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    // 国家
    private String country;
    // 省份
    private String region;
    // 城市
    private String city;
    // 互联网服务提供商
    private String isp;

    public static IpInfo init() {
        return IpInfo.builder()
                .city("未知")
                .country("未知")
                .city("未知")
                .isp("未知")
                .build();
    }

    public static IpInfo init(String cityInfo) {
        // IP解析为null
        if (Objects.isNull(cityInfo)) return IpInfo.init();
        // 映射为IPInfo
        List<String> collect = Arrays.stream(cityInfo.split("\\|"))
                .map(s -> Optional.ofNullable(s).filter(v -> !v.equals("0")).orElse("未知"))
                .toList();
        return IpInfo.builder()
                .country(collect.get(0))
                .region(collect.get(2))
                .city(collect.get(3))
                .isp(collect.get(4))
                .build();
    }
}