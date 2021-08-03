package com.i2dsp.maintenance.service;

import com.i2dsp.maintenance.domain.MaintenanceRecordDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 存储保养记录的保养内容信息 服务类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
public interface IMaintenanceRecordDetailService extends IService<MaintenanceRecordDetail> {

    /**
     * 保存保养记录详情
     * @param maintenanceRecordDetail
     * @return
     * @throws Exception
     */
    Long saveMaintenanceRecordDetail(MaintenanceRecordDetail maintenanceRecordDetail) throws Exception;
}
