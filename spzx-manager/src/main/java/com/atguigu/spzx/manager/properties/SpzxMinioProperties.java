package com.atguigu.spzx.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "minio")
public class SpzxMinioProperties {
    String endpoint;
    String username;
    String password;
    String bucketName;
}
