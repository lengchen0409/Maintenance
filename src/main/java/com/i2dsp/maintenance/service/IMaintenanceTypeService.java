package com.i2dsp.maintenance.service;

import com.i2dsp.maintenance.domain.MaintenanceType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.i2dsp.maintenance.domain.dto.MaintenanceTypeDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceTypeDetailVo;
import com.i2dsp.maintenance.domain.vo.MaintenanceTypeVo;
import com.i2dsp.maintenance.utils.ResultVo;

import java.util.List;

/**
 * <p>
 * 存储保养类型信息 服务类
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
public interface IMaintenanceTypeService extends IService<MaintenanceType> {

    /**
     * 查询保养类型
     * @param maintenanceTypeVo
     * @param userId
     * @return
     */
    ResultVo getType(MaintenanceTypeVo maintenanceTypeVo, Long userId);

    /**
     * 新增保养类型
     * @param maintenanceTypeDto
     * @param userId
     * @return
     */
    ResultVo insertType(MaintenanceTypeDto maintenanceTypeDto, Long userId);

    /**
     * 根据id查询保养类型详情
     * @param typeId
     * @return
     */
    MaintenanceTypeDetailVo getTypeById(Long typeId);

    /**
     * 根据typeId查询
     * @param typeId
     * @return
     */
    MaintenanceType getTypeByTypeId(Long typeId);

    /**
     * 删除保养类型
     * @param typeId
     * @return
     */
    boolean deleteType(Long typeId);


    /**
     *根据产品名查询保养类型
     * @param productName 产品名
     * @return
     */
    List<MaintenanceType> getTypeByProductName(String productName);

    /**
     * 根据保养类型id集合获得保养类型集合
     * @param typeIdList
     * @return
     */
    List<MaintenanceType> getTypeByTypeIdList(List<Long> typeIdList);

    /**
     * 修改保养类型
     * @param maintenanceTypeDto
     * @param userId
     * @return
     */
    boolean alterType(MaintenanceTypeDto maintenanceTypeDto, Long userId);
}
