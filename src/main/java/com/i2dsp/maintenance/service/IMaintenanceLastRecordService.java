package com.i2dsp.maintenance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.i2dsp.maintenance.domain.MaintenanceLastRecord;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndTypeVo;

import java.util.List;

/**
 * <p>
 * 保养最新记录 服务类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-07-27
 */
public interface IMaintenanceLastRecordService extends IService<MaintenanceLastRecord> {

    /**
     * 条件查询最新记录信息
     * @param maintenanceRecordAndTypeVo
     * @return
     */
    List<MaintenanceRecordAndTypeVo> searchLastRecordAndType(MaintenanceRecordAndTypeVo maintenanceRecordAndTypeVo);

    /**
     * 条件查询该设备下最新的保养记录和保养类型
     * @param maintenanceRecordAndTypeVo
     * @return
     */
    List<MaintenanceRecordAndTypeVo> searchLastRecordAndTypeByDevice(MaintenanceRecordAndTypeVo maintenanceRecordAndTypeVo);

    /**
     * 条件查询所有最新保养记录和保养类型
     * @param maintenanceRecordAndTypeVo
     * @return
     */
    List<MaintenanceRecordAndTypeVo> searchLastRecordAndTypeByAll(MaintenanceRecordAndTypeVo maintenanceRecordAndTypeVo);
}
