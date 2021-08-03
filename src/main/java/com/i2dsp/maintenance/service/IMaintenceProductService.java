package com.i2dsp.maintenance.service;

import com.i2dsp.maintenance.domain.dto.MaintenanceProductDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceProductDetail;

import java.util.List;

/**
 * description 产品服务接口
 * date: 2021-07-02 15:17
 *
 * @author 林隆星
 */
public interface IMaintenceProductService {
    /**
     * 根据产品名获取产品详情信息
     * @param productName
     * @return
     */
    MaintenanceProductDetail getProductDetail(String productName, Long productId);

    /**
     * 更改产品与保养类型关系
     * @param maintenanceProductDto
     * @return
     */
    Boolean alterProductHasType(MaintenanceProductDto maintenanceProductDto);

    /**
     * 查询产品列表
     * @param pageNum
     * @param pageSize
     * @param productName
     * @return
     */
    String getProducts(Integer pageNum, Integer pageSize, String productName);

    /**
     * 查询用户拥有的产品
     * @param userId 用户Id
     * @return
     */
    List<String> getProducts(Long userId);
}
