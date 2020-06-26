package com.leyou.item.web;

import com.leyou.item.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 * @author zc
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 文件上传
     * 默认情况下，网关会对请求的参数进行缓存，文件上传不需要，所以需要通过在域名后面加上zuul前缀来告诉zuul网关不对本次的请求参数进行缓存
     * 文件会上传到专门的文件服务器中。在本项目中，将文件上传到分布式文件系统FastDFS的存储服务器中。
     * 在FastDFS中，请求首先打到tracker跟踪服务器，跟踪服务器返回可以存储的服务器的IP和端口号等信息，然后请求到存储服务器进行文件的保存，
     * 成功保存后存储服务器响应一个id给客户端，该id映射了文件在存储服务器上的保存位置。
     * @param file  SpringMVC将上传的文件封装成MultipartFile对象
     * @return 存储服务器响应的id（文件的保存路径）
     */
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        System.out.println("执行了,,,");
        if (file.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        String url = uploadService.uploadFile(file);
        return ResponseEntity.ok(url);
    }
}
