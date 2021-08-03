package com.i2dsp.maintenance.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.config.exception.GlobalException;
import com.i2dsp.maintenance.domain.MaintenanceProduct;
import com.i2dsp.maintenance.domain.MaintenanceType;
import com.i2dsp.maintenance.domain.ProductHasType;
import com.i2dsp.maintenance.domain.dto.MaintenanceProductDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceProductDetail;
import com.i2dsp.maintenance.mapper.MaintenanceProductMapper;
import com.i2dsp.maintenance.openfeign.I2dspEmgAdmin;
import com.i2dsp.maintenance.openfeign.I2dspEmgData;
import com.i2dsp.maintenance.service.IMaintenceProductService;
import com.i2dsp.maintenance.utils.TimestampUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description 产品服务实现类
 * date: 2021-07-02 15:19
 *
 * @author 林隆星
 */
@Service
public class MaintenceProductServiceImpl implements IMaintenceProductService {

    @Autowired
    MaintenanceProductMapper maintenanceProductMapper;
    @Autowired
    ProductHasTypeServiceImpl productHasTypeService;
    @Autowired
    MaintenanceTypeServiceImpl maintenanceTypeService;
    @Autowired
    I2dspEmgAdmin i2dspEmgAdmin;
    @Autowired
    I2dspEmgData i2dspEmgData;

    /**
     * 根据产品名获取产品详情信息
     * @param productName 产品名
     * @return productDetail
     */
    @Override
    public MaintenanceProductDetail getProductDetail(String productName, Long productId) {
        //根据productName远程调用接口获取产品信息
        JSONObject products = getProductJsonObject(productName);
        //根据产品名获取产品详情信息
        MaintenanceProductDetail productDetail = maintenanceProductMapper.getProductDetail(productName, productId);
        if (productDetail == null) {
            throw new GlobalException("该产品暂无保养信息");
        }
        //封装产品信息
        JSONObject jsonObject = products.getJSONObject("data").getJSONArray("list").getJSONObject(0);
        MaintenanceProduct maintenanceProduct = JSON.toJavaObject(jsonObject,MaintenanceProduct.class);
        productDetail.setMaintenanceProduct(maintenanceProduct);
        return productDetail;
    }

    /**
     * 根据productName远程调用接口获取产品信息
     * @param productName 产品名
     * @return products 远程调用的接口返回内容
     */
    private JSONObject getProductJsonObject(String productName) {
        try {
            //获取产品信息
            JSONObject products = i2dspEmgAdmin.getProducts(1, 0, productName);
            //验证产品是否存在
            if (products.getJSONObject("data").getInteger("total") == 0) {
                throw new GlobalException(ResultEnum.OK,"该产品不存在");
            }
            return products;
        }catch (Exception e) {
            throw new GlobalException(ResultEnum.OPENFEIGN_ERROR);
        }
    }

    /**
     * 更改产品与保养类型关系
     * @param maintenanceProductDto 更改内容
     * @return Boolean 是否成功
     */
    @Override
    @Transactional
    public Boolean alterProductHasType(MaintenanceProductDto maintenanceProductDto) {
        //根据productName远程调用接口验证该产品是否存在
        JSONObject products = getProductJsonObject(maintenanceProductDto.getProductName());
        //封装产品信息
        JSONObject jsonObject = products.getJSONObject("data").getJSONArray("list").getJSONObject(0);
        Long productId = jsonObject.getLong("productId");
        //清空该产品与保养类型关系
        QueryWrapper<ProductHasType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_name",maintenanceProductDto.getProductName());
        productHasTypeService.remove(queryWrapper);
        //检查保养类型Id是否全部存在
        if (maintenanceProductDto.getTypeIds().size() != maintenanceTypeService.getTypeByTypeIdList(maintenanceProductDto.getTypeIds()).size()) {
            throw new GlobalException("保养类型不存在！");
        }
        //添加产品与保养类型关系
        List<ProductHasType> productHasTypes = new ArrayList<>();
        for (Long typeId : maintenanceProductDto.getTypeIds()) {
            ProductHasType productHasType = new ProductHasType();
            productHasType.setProductName(maintenanceProductDto.getProductName());
            productHasType.setProductId(productId);
            productHasType.setTypeId(typeId);
            productHasType.setGmtCreate(TimestampUtils.getCurrentTimestamp());
            productHasType.setGmtModified(TimestampUtils.getCurrentTimestamp());
            productHasTypes.add(productHasType);
        }
        productHasTypeService.saveBatch(productHasTypes);
        return true;
    }

    /**
     * 查询产品列表
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @param productName 产品名
     * @return String 结果的json数据
     */
    @Override
    public String getProducts(Integer pageNum, Integer pageSize, String productName) {
        //获取产品信息
        JSONObject products = i2dspEmgAdmin.getProducts(pageNum, pageSize, productName);
        //验证产品是否存在
        if (products.getJSONObject("data").getInteger("total") == 0) {
            return products.toString();
        }
        //向响应数据中添加已分配的保养类型数据。
        List<MaintenanceProduct> maintenanceProducts = JSON.parseArray(products.getJSONObject("data").getJSONArray("list").toString(), MaintenanceProduct.class);
        for (int i = 0; i < maintenanceProducts.size(); i++) {
            MaintenanceProduct productDetail = maintenanceProducts.get(i);
            List<MaintenanceType> maintenanceTypes = maintenanceTypeService.getTypeByProductName(productDetail.getProductName());
            productDetail.setTypes(maintenanceTypes.stream().map(tmp->tmp.getTypeName()).collect(Collectors.toList()));
        }
        products.getJSONObject("data").replace("list",maintenanceProducts);
        return products.toString();
    }

    /**
     * 查询用户拥有的产品
     * @param userId 用户Id
     * @return productNames
     */
    @Override
    public List<String> getProducts(Long userId) {
        //产品名接收对象
        List<String> productNames = new ArrayList<>();
        try {
            //获取用户产品信息
            JSONObject responseJson = i2dspEmgData.getProductsByUserId(userId);
            //判断是否查询成功
            if (responseJson.getInteger("code") == 20000) {
                JSONArray devices = responseJson.getJSONArray("data");
                for (int i = 0; i < devices.size(); i++) {
                    //封装产品名
                    String productName = devices.getJSONObject(i).getString("name");
                    productNames.add(productName);
                }
            }
        }catch (Exception e) {
            throw new GlobalException(ResultEnum.OPENFEIGN_ERROR);
        }
        return productNames;
    }
}
