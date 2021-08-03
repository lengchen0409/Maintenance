package com.i2dsp.maintenance.mapper;

import com.i2dsp.maintenance.domain.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.i2dsp.maintenance.domain.dto.MaintenanceRecordDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndDetailAndPhotoVo;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndTypeVo;

import java.util.List;

/**
 * <p>
 * 存储保养记录的基本信息 Mapper 接口
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
public interface MaintenanceRecordMapper extends BaseMapper<MaintenanceRecord> {

    /**
     * 查询保养记录详情
     * @param maintenanceRecordDto
     * @return
     */
    List<MaintenanceRecordAndDetailAndPhotoVo> searchMaintenancesByAll(MaintenanceRecordDto maintenanceRecordDto);

    /**
     * 根据保养记录id查询关联的保养记录详情
     * @param recordId
     * @return
     */
    MaintenanceRecordDetail searchMaintenanceRecordDetailById(Long recordId);

    /**
     * 根据保养记录详情id查询关联的保养图片
     * @param recordDetailId
     * @return
     */
    MaintenancePhoto searchMaintenancePhotoById(Long recordDetailId);

    /**
     * 根据保养内容id查询保养内容信息
     * @param contentId
     * @return
     */
    MaintenanceContent searchMaintenanceContentById(Long contentId);

    /**
     * 根据保养类型id查询保养类型信息
     * @param typeId
     * @return
     */
    MaintenanceType searchMaintenanceTypeById(Long typeId);

    /**
     * 根据设备查找记录和保养类型
     * @param deviceId
     * @return
     */
    List<MaintenanceRecordAndTypeVo> searchRecordAndTypeByDeviceId(String deviceId);

    /**
     * 根据保养类型查询保养记录
     * @param typeId
     * @return
     */
    List<MaintenanceRecordAndTypeVo> searchRecordAndTypeByTypeId(Long typeId, String staffName);

}
