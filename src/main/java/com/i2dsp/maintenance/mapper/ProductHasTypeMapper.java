package com.i2dsp.maintenance.mapper;

import com.i2dsp.maintenance.domain.ProductHasType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.i2dsp.maintenance.domain.vo.MaintenanceProductAndTypeVo;

import java.util.List;

/**
 * <p>
 * 产品类型和保养类型的关系管理中间表 Mapper 接口
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
public interface ProductHasTypeMapper extends BaseMapper<ProductHasType> {

    /**
     * 条件查询保养类型和产品信息
     * @param maintenanceProductAndTypeVo
     * @return
     */
    List<MaintenanceProductAndTypeVo> searchProductAndType(MaintenanceProductAndTypeVo maintenanceProductAndTypeVo);
}
