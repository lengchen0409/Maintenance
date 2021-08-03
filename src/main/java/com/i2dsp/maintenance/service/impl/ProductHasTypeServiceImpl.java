package com.i2dsp.maintenance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.i2dsp.maintenance.domain.ProductHasType;
import com.i2dsp.maintenance.domain.vo.MaintenanceProductAndTypeVo;
import com.i2dsp.maintenance.mapper.ProductHasTypeMapper;
import com.i2dsp.maintenance.service.IProductHasTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 产品类型和保养类型的关系管理中间表 服务实现类
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
@Service
public class ProductHasTypeServiceImpl extends ServiceImpl<ProductHasTypeMapper, ProductHasType> implements IProductHasTypeService {

    @Autowired
    private ProductHasTypeMapper productHasTypeMapper;
    /**
     * 检验保养类型和保养产品是否存在关联关系
     *
     * @param typeId
     * @return
     */
    @Override
    public Boolean hasRelevancy(Long typeId) {
        QueryWrapper<ProductHasType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId);
        List<ProductHasType> list = this.list(queryWrapper);
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 条件查询保养类型和产品信息
     * @param maintenanceProductAndTypeVo
     * @return
     */
    @Override
    public List<MaintenanceProductAndTypeVo> searchProductAndType(MaintenanceProductAndTypeVo maintenanceProductAndTypeVo) {
        return productHasTypeMapper.searchProductAndType(maintenanceProductAndTypeVo);
    }
}
