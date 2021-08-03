package com.i2dsp.maintenance.service;

import com.i2dsp.maintenance.domain.MaintenancePhoto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 存储保养记录的图片信息 服务类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
public interface IMaintenancePhotoService extends IService<MaintenancePhoto> {

    /**
     * 保存图片
     * @param recordDatilId
     * @param fileNames
     */
    void saveMaintenancePhoto(String recordDatilId, List<String> fileNames);
}
