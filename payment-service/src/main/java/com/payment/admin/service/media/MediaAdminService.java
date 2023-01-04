package com.payment.admin.service.media;

import com.payment.admin.entity.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by M. on 2021/3/30.
 */
public interface MediaAdminService {

    public List<Media> getMediaList(List<Integer> mediaIds);

    public Media saveMedia(MultipartFile file, Integer mediaType) throws Exception;

    public String getMediaUrl(String fileName);
}
