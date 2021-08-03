package com.i2dsp.maintenance.controller;

import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.domain.dto.MaintenanceProductDto;
import com.i2dsp.maintenance.openfeign.I2dspEmgAdmin;
import com.i2dsp.maintenance.openfeign.I2dspEmgData;
import com.i2dsp.maintenance.service.impl.MaintenceProductServiceImpl;
import com.i2dsp.maintenance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * description 产品信息前端控制器
 * date: 2021-07-02 13:30
 *
 * @author 林隆星
 */
@RestController
public class MaintenanceProductController {

    @Autowired
    MaintenceProductServiceImpl maintenceProductService;
    @Autowired
    I2dspEmgData i2dspEmgData;
    @Autowired
    I2dspEmgAdmin i2dspEmgAdmin;

    /**
     *更改产品与保养类型关系
     * 由于远程调用查询产品信息接口，只能根据peoductName来进行查询，待设备产品数据库优化后再修改为根据productId来更改
     * @param maintenanceProductDto 更改内容
     * @return ResultVo
     */
    @PutMapping("/products")
    public ResultVo alterProductHasType(@Valid @RequestBody MaintenanceProductDto maintenanceProductDto){
        Boolean result = maintenceProductService.alterProductHasType(maintenanceProductDto);
        if (Boolean.TRUE.equals(result)) {
            return new ResultVo<>(ResultEnum.OK);
        }else {
            return new ResultVo<>(ResultEnum.ERROR);
        }
    }

    /**
     * 查询产品保养详情
     * @param productName 产品名
     * @return ResultVo
     */
    @GetMapping("/products/detail")
    public ResultVo getProductDetail(String productName, Long productId) {
        return new ResultVo<>(maintenceProductService.getProductDetail(productName, productId));
     }

    /**
     * 查询产品列表
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @param productName 产品名
     * @return String
     */
    @GetMapping("/products")
    public String getProducts(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "0") Integer pageSize, String productName) {
        return maintenceProductService.getProducts(pageNum, pageSize, productName);
     }

     /**
     * 查询用户拥有的产品
     * @param userId 用户Id
     * @return ResultVo
     */
    @GetMapping("/products/user")
    public ResultVo getProducts(@RequestHeader(value = "userId",defaultValue = "-1") Long userId) {
        return new ResultVo<>(maintenceProductService.getProducts(userId));
     }

}
