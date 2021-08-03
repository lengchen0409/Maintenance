package com.i2dsp.maintenance.service.impl;

import com.i2dsp.maintenance.domain.MaintenancePhoto;
import com.i2dsp.maintenance.mapper.MaintenancePhotoMapper;
import com.i2dsp.maintenance.service.IMaintenancePhotoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 存储保养记录的图片信息 服务实现类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
@Service
public class MaintenancePhotoServiceImpl extends ServiceImpl<MaintenancePhotoMapper, MaintenancePhoto> implements IMaintenancePhotoService {

    @Value("${upload.photo.sufix}")
    private String uploadPhotoSufix;

    @Autowired
    private MaintenancePhotoMapper maintenancePhotoMapper;

    /**
     * 保存图片
     * @param recordDatilId
     * @param fileNames
     */
    @Override
    public void saveMaintenancePhoto(String recordDatilId, List<String> fileNames) {
        List<MaintenancePhoto> maintenancePhotoList = new ArrayList<>();
        int i = 0;
        for (String fileName : fileNames) {
            i++;
            if (i > 7) {           //控制图片数量小于等于7张
                break;
            }
            String photoType = "";
            String suffix = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf(".") + 1);    //获取图片后缀
            if (uploadPhotoSufix.contains(suffix)) {        //判断是否为图片类型
                photoType = "images";
            }
            maintenancePhotoList.add(
                    new MaintenancePhoto()
                            .setPhotoType(photoType)
                            .setPhotoPath(fileName)
                            .setRecordDetailId(Long.valueOf(recordDatilId))
                            .setGmtCreate(String.valueOf(System.currentTimeMillis()))
            );

        }
        saveBatch(maintenancePhotoList);
    }
}
