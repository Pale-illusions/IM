package com.iflove.im;

import com.iflove.api.user.domain.entity.IpInfo;
import com.iflove.common.utils.IPUtils;
import org.junit.jupiter.api.Test;

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
public class IPTest {

    @Test
    public void testIP() {
        String ip = "142.123.123.123";
        String cityInfo = IPUtils.getCityInfo(ip);
        List<String> collect = Arrays.stream(cityInfo.split("\\|"))
                .map(s -> Optional.ofNullable(s).filter(v -> !v.equals("0")).orElse("未知"))
                .collect(Collectors.toList());
        IpInfo ipInfo = IpInfo.builder()
                        .country(collect.get(0))
                        .region(collect.get(2))
                        .city(collect.get(3))
                        .isp(collect.get(4))
                        .build();
        System.out.println(ipInfo);
    }

}
