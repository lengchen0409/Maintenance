package com.i2dsp.maintenance.service;

import com.github.pagehelper.PageInfo;
import com.i2dsp.maintenance.domain.dto.MaintenanceRecordDto;
import com.i2dsp.maintenance.domain.dto.MaintenanceRecords;
import com.i2dsp.maintenance.domain.MaintenanceRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndDetailAndPhotoVo;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndTypeVo;
import com.i2dsp.maintenance.utils.ResultVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 存储保养记录的基本信息 服务类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
public interface IMaintenanceRecordService extends IService<MaintenanceRecord> {

    /**
     * 填写保养记录
     * @param userId
     * @param maintenanceRecords
     * @param fileImages
     * @param fileNumber
     * @return
     */
    ResultVo<Integer> fillMaintenanceRecord(Long userId, MaintenanceRecords maintenanceRecords, MultipartFile[] fileImages, Integer[] fileNumber);

    /**
     * 保存保养记录
     * @param maintenanceRecord
     * @return
     * @throws Exception
     */
    Long saveMaintenanceRecord(MaintenanceRecord maintenanceRecord) throws Exception;

    /**
     * 查询保养记录详情
     * @param maintenanceRecordDto
     * @param userId
     * @return
     */
    List<MaintenanceRecordAndDetailAndPhotoVo> searchMaintenancesByAll(Long userId, MaintenanceRecordDto maintenanceRecordDto);

    /**
     * 根据设备查询记录和保养类型
     * @param deviceId
     * @return
     */
    List<MaintenanceRecordAndTypeVo> searchRecordAndTypeByDeviceId(String deviceId);

    /**
     * 根据保养类型id查询保养记录和保养类型
     * @param typeId
     * @return
     */
    List<MaintenanceRecordAndTypeVo> searchRecordAndTypeByTypeId(Long typeId, String staffName);

    /**
     * 获取设备保养记录excel文件
     * @param maintenanceRecordDto
     * @param userId
     * @param response
     * @author 林隆星
     */
    void getExcelFile(Long userId, MaintenanceRecordDto maintenanceRecordDto, HttpServletResponse response);

    /**
     * 检测是否存在该保养类型的保养记录
     * @param typeId
     * @return
     */
    Boolean checkRecord(Long typeId);
}
