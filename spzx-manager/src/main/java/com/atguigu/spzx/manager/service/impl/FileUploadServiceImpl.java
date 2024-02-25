package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.common.exp.GuiguException;
import com.atguigu.spzx.manager.properties.SpzxMinioProperties;
import com.atguigu.spzx.manager.service.FileUploadService;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    SpzxMinioProperties spzxMinioProperties;

    @Override
    public String upload(MultipartFile file) {

        try {
            String originalFilename = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            if(!originalFilename.endsWith(".jpg") && !originalFilename.endsWith(".png")){
                throw new GuiguException(ResultCodeEnum.FILE_TYPE_ERROR);
            }
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(spzxMinioProperties.getEndpoint())
                            .credentials(spzxMinioProperties.getUsername(),spzxMinioProperties.getPassword())
                            .build();

            String objectName = new DateTime().toString("yyyy/MM/dd") + "/" + UUID.randomUUID().toString().replaceAll("-","") + originalFilename;

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(spzxMinioProperties.getBucketName())
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(originalFilename.endsWith(".png") ? "image/png" : "image/jpeg")
                            .build());

            String url = spzxMinioProperties.getEndpoint() + "/" + spzxMinioProperties.getBucketName() + "/" + objectName;
            return url;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
