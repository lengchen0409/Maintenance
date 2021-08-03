package com.i2dsp.maintenance.minIo;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author 梁海聪
 * @since 2021/06/30 10:20
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    //endpoint是一个URL,域名,IPv4或者IPv6地址
    private String endpoint;

    //accessKey类似于用户id,唯一标识的用户账号
    private String accessKey;

    //secretKey是用户的密码
    private String secretKey;

    //如果是true,则用的是https而不是http,默认为true
    private Boolean secure;

    //默认存储桶
    private String bucketName;

    @Bean
    public MinioClient getMinioClient() throws Exception {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
