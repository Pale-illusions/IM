package com.iflove.api.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IpDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    //注册时的ip
    private String ip;
    //最新登录的ip
    private String isp;
    private String isp_id;
    private String city;
    private String city_id;
    private String country;
    private String country_id;
    private String region;
    private String region_id;
}