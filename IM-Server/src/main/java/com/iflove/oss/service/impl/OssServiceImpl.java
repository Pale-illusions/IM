package com.iflove.oss.service.impl;

import com.iflove.oss.MinIOTemplate;
import com.iflove.oss.domain.OssReq;
import com.iflove.oss.domain.OssResp;
import com.iflove.oss.domain.enums.OssSceneEnum;
import com.iflove.oss.domain.vo.request.UploadUrlReq;
import com.iflove.oss.service.OssService;
import jakarta.annotation.Resource;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class OssServiceImpl implements OssService {
    @Resource
    MinIOTemplate minIOTemplate;

    @Override
    public OssResp getUploadUrl(Long uid, UploadUrlReq req) {
        OssSceneEnum sceneEnum = OssSceneEnum.of(req.getScene());
        if (Objects.isNull(sceneEnum)) {
            throw new ValidationException("场景有误");
        }
        OssReq ossReq = OssReq.builder()
                .fileName(req.getFileName())
                .filePath(sceneEnum.getPath())
                .uid(uid)
                .build();
        return minIOTemplate.getPreSignedObjectUrl(ossReq);
    }
}
