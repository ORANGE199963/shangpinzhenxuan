
package com.atguigu.spzx.manager;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileUploader {
  public static void main(String[] args)
      throws IOException, NoSuchAlgorithmException, InvalidKeyException {
    try {
      // Create a minioClient with the MinIO server playground, its access key and secret key.
      MinioClient minioClient =
          MinioClient.builder()
              .endpoint("http://192.168.126.128:9000")
              .credentials("admin", "admin123456")
              .build();

      // Make 'asiatrip' bucket if not exist.
      boolean found =
          minioClient.bucketExists(BucketExistsArgs.builder().bucket("spzx-bucket").build());
      if (!found) {
        // Make a new bucket called 'asiatrip'.
        minioClient.makeBucket(MakeBucketArgs.builder().bucket("spzx-bucket").build());
      }

      // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
      // 'asiatrip'.
//      minioClient.uploadObject(
//          UploadObjectArgs.builder()
//              .bucket("asiatrip")
//              .object("asiaphotos-2015.zip")
//              .filename("/home/user/Photos/asiaphotos.zip")
//              .build());
//      System.out.println(
//          "'/home/user/Photos/asiaphotos.zip' is successfully uploaded as "
//              + "object 'asiaphotos-2015.zip' to bucket 'asiatrip'.");


      InputStream inputStream = new FileInputStream("D:\\backup\\OneDrive\\图片\\minio\\1.png");
      minioClient.putObject(
              PutObjectArgs.builder().bucket("spzx-bucket")
                      .object("2024/02/24/1.png")
                      .stream(inputStream, inputStream.available(), -1)
                      .contentType("image/png")
                      .build());

      String url = "http://192.168.126.128:9000/spzx-bucket/2024/02/24/1.png";
      System.out.println(url);
    } catch (MinioException e) {
      System.out.println("Error occurred: " + e);
      System.out.println("HTTP trace: " + e.httpTrace());
    }
  }
}