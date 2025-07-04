package org.example.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Result;
import org.example.utils.AliyunOSSOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
public class UploadController {
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传:{}",file.getOriginalFilename());
        //将文件交给OSS存储管理
        String url=aliyunOSSOperator.upload(file.getBytes(),file.getOriginalFilename());
        log.info("文件上传成功,url:{}",url);
        return Result.success(url);
    }
}
