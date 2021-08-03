package com.i2dsp.maintenance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.i2dsp.maintenance.domain.MaintenanceLastRecord;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndTypeVo;

import java.util.List;

/**
 * <p>
 * 保养最新记录 Mapper 接口
 * </p>
 *
 * @author 梁海聪
 * @since 2021-07-27
 */
public interface MaintenanceLastRecordMapper extends BaseMapper<MaintenanceLastRecord> {

    /**
     * 条件查询最新记录表信息
     * @param maintenanceRecordAndTypeVo
     * @return
     */
    List<MaintenanceRecordAndTypeVo> searchLastRecordAndType(MaintenanceRecordAndTypeVo maintenanceRecordAndTypeVo);

    /**
     * 条件查询该设备下最新保养记录和保养类型
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
