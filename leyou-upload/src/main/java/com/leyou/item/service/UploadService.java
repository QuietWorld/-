package com.leyou.item.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    /**
     * 文件上传
     * @param file
     * @return
     */
    String uploadFile(MultipartFile file);
}
