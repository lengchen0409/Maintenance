package com.i2dsp.maintenance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.i2dsp.maintenance.domain.ProductHasType;
import com.i2dsp.maintenance.domain.vo.MaintenanceProductAndTypeVo;

import java.util.List;

/**
 * <p>
 * 产品类型和保养类型的关系管理中间表 服务类
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
public interface IProductHasTypeService extends IService<ProductHasType> {

    /**
     * 检验保养类型和保养产品是否存在关联关系
     * @param typeId
     * @return
     */
    Boolean hasRelevancy(Long typeId);

    /**
     * 条件查询保养类型和产品信息
     * @author : 梁海聪
     * @param maintenanceProductAndTypeVo
     * @return
     */
    List<MaintenanceProductAndTypeVo> searchProductAndType(MaintenanceProductAndTypeVo maintenanceProductAndTypeVo);
}
