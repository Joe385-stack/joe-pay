package com.payment.admin.service.media.impl;

import com.payment.admin.manager.QiniuManager;
import com.payment.admin.service.media.MediaAdminService;
import com.payment.admin.entity.Media;
import com.payment.admin.mapper.MediaMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by M. on 2021/3/30.
 */
@Service
public class MediaAdminServiceImpl implements MediaAdminService {

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private QiniuManager qiniuManager;

    @Override
    public List<Media> getMediaList(List<Integer> mediaIds) {
        List<Media> list = mediaMapper.selectBatchIds(mediaIds);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        for (Media media: list) {
            String mediaUrl = this.getMediaUrl(media.getMediaUrl());
            media.setMediaUrl(mediaUrl);
        }
        return list;
    }

    @Override
    public Media saveMedia(MultipartFile file, Integer mediaType) throws Exception {
        // 远端文件名
        String fileName = qiniuManager.uploadByStream(file.getInputStream());
        Media media = new Media();
        media.setMediaUrl(fileName);
        media.setMediaType(mediaType);
        media.setCreateTime(LocalDateTime.now());
        mediaMapper.insert(media);
        media.setMediaUrl(getMediaUrl(fileName));
        return media;
    }

    @Override
    public String getMediaUrl(String fileName) {
        return qiniuManager.mergeUrlAndSlim(fileName);
    }
}
