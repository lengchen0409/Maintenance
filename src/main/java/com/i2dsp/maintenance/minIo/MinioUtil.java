package com.i2dsp.maintenance.minIo;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 梁海聪
 * @since 2021/06/30 10:20
 */
@Component
@Slf4j
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 通过InputStream上传对象
     */
    @SneakyThrows
    public void putObject(MultipartFile multipartFile, String filename) {

        //创建头部信息
        Map<String, String> headers = new HashMap<>(10);
        //添加自定义内容类型
        headers.put("Content-Type", "application/octet-stream");
        if (filename.contains(".png")){
            headers.put("Content-Type", "image/png");
        }
        if (filename.contains(".jpg")){
            headers.put("Content-Type", "image/jpeg");
        }
        if (filename.contains(".gif")){
            headers.put("Content-Type", "image/gif");
        }
        //添加存储类
        headers.put("X-Amz-Storage-Class", "REDUCED_REDUNDANCY");
        InputStream stream = multipartFile.getInputStream();
        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(filename).stream(
                stream, stream.available(), -1)
                .contentType(multipartFile.getContentType())
                .headers(headers)
                .build());
    }
}
