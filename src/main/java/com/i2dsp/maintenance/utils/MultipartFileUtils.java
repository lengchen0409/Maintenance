package com.i2dsp.maintenance.utils;

import com.i2dsp.maintenance.minIo.MinioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


/**
 * @author 梁海聪
 * @since 2021/06/30 10:20
 * Description: TODO  文件统一处理
 */
@Component
public class MultipartFileUtils {

    private static final Logger logger = LoggerFactory.getLogger(MultipartFileUtils.class);

    @Value("${upload.photo.maxsize}")
    private long uploadPhotoMaxSize;

    @Autowired
    MinioUtil minioUtil;

    /**
     * 图片统一上传
     * @param fileImage
     * @param userId
     * @param recordDetailId
     * @return
     */
    public List<String> handleFile(MultipartFile[] fileImage, Long userId, String recordDetailId) {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : fileImage) {
            if (!file.isEmpty()){
                String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                String fileName = uuid + suffix;
                // 生成保存文件
                String path = "fillMaintenance/"  +
                        userId +
                        "/" + recordDetailId +
                        "/" + fileName;
                minioUtil.putObject(file, path);
                fileNames.add(path);
            }
        }
        return fileNames;
    }
}
