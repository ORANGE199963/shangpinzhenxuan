package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.FileUploadService;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件上传")
@RestController
@RequestMapping("/admin/system/upload")
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

    @Operation(summary = "上传图片")
    @PostMapping("upload")
    public Result upload(MultipartFile file){
        String url = fileUploadService.upload(file);
        return Result.build(url, ResultCodeEnum.SUCCESS);
    }
}
