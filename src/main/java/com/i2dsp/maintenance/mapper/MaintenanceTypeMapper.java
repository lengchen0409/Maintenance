package com.i2dsp.maintenance.mapper;


import com.i2dsp.maintenance.domain.MaintenanceContent;
import com.i2dsp.maintenance.domain.MaintenanceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.i2dsp.maintenance.domain.vo.MaintenanceTypeDetailVo;
import com.i2dsp.maintenance.domain.vo.MaintenanceTypeVo;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.Pattern;
import java.util.List;


/**
 * <p>
 * 存储保养类型信息 Mapper 接口
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
public interface MaintenanceTypeMapper extends BaseMapper<MaintenanceType> {

    /**
     * 保存保养类型
     * @param maintenanceType
     * @return
     */
    Boolean saveOne(MaintenanceType maintenanceType);

    /**
     * 根据id查询保养类型详情
     * @param typeId
     */
    MaintenanceTypeDetailVo selectTypeById(Long typeId);

    /**
     * 根据id查询保养条目
     * @param typeId
     */
    MaintenanceContent selectContentByTypeId(Long typeId);

    /**
     *根据产品名查询保养类型
     * @param productName 产品名
     * @return
     */
    List<MaintenanceType> getTypeByProductName(String productName);

    /**
     * 查询保养类型和已分配的产品
     * @param typeName
     * @return
     */
    List<MaintenanceType> queryTypesAndProducts(MaintenanceTypeVo maintenanceTypeVo);

    /**
     * 根据保养类型查询产品名
     * @param typeId
     * @return
     */
    List<String> queryProductNameByTypeId(Long typeId);


}
