package com.leyou.item.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.item.config.UploadProperties;
import com.leyou.item.enums.ExceptionEnum;
import com.leyou.item.exception.LeyouException;
import com.leyou.item.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * 文件上传
 *
 * @Author zc
 */
@Service
@EnableConfigurationProperties(value = UploadProperties.class)
public class UploadServiceImpl implements UploadService {

    private static final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);
    @Autowired
    private UploadProperties uploadProperties;
    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file) {
        // 文件类型校验
        String contentType = file.getContentType();
        if (!uploadProperties.getAllowedType().contains(contentType)) {
            log.info("文件类型不合法：{}", contentType);
            throw new LeyouException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        try {
            // 文件内容校验
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null || image.getHeight() == 0 || image.getWidth() == 0) {
                log.info("文件内容不合法");
                throw new LeyouException(ExceptionEnum.ILLEGAL_FILE_CONTENT);
            }
            // 上传文件
            // 文件后缀名获取
            String suffix = StringUtils.substringAfter(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), suffix, null);
            return "http://image.leyou.com/" + storePath.getFullPath();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new LeyouException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }
}
