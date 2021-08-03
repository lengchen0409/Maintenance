package com.i2dsp.maintenance.mapper;

import com.i2dsp.maintenance.domain.MaintenanceContent;
import com.i2dsp.maintenance.domain.MaintenanceType;
import com.i2dsp.maintenance.domain.vo.MaintenanceProductDetail;
import com.i2dsp.maintenance.domain.vo.MaintenanceTypeDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description
 * date: 2021-07-05 14:34
 *
 * @author 林隆星
 */
public interface MaintenanceProductMapper {


    /**
     * 根据产品名查询产品的保养详情信息
     * @param productName
     * @return
     */
    MaintenanceProductDetail getProductDetail(@Param("productName") String productName,@Param("productId") Long productId);


    /**
     * 根据productName查询保养类型详情
     * @param productName
     * @return
     */
    List<MaintenanceTypeDetailVo> getTypeDetailByProductName(String productName);

    /**
     * 根据typeId查询保养内容详情
     * @param typeId
     * @return
     */
    List<MaintenanceContent> getContentByTypeId(long typeId);
}
