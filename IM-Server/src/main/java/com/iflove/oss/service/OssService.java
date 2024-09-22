package com.iflove.oss.service;

import com.iflove.oss.domain.OssResp;
import com.iflove.oss.domain.vo.request.UploadUrlReq;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface OssService {
    /**
     * 获取临时的上传链接
     */
    OssResp getUploadUrl(Long uid, UploadUrlReq req);
}
