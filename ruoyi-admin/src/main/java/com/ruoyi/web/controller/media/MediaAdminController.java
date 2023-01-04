package com.ruoyi.web.controller.media;

import com.payment.admin.service.media.MediaAdminService;
import com.ruoyi.web.entity.ApiResponse;
import com.payment.admin.entity.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by M. on 2021/4/1.
 */
@RestController
@RequestMapping("/admin/media")
public class MediaAdminController {

    @Autowired
    private MediaAdminService mediaAdminService;

    @PostMapping("/upload")
    public ApiResponse saveMedia(@RequestParam("file") MultipartFile file, Integer mediaType) throws Exception {
        Media media = this.mediaAdminService.saveMedia(file, mediaType);
        return ApiResponse.newSuccessInstance(media);
    }
}
