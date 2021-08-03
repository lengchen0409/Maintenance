package com.i2dsp.maintenance.openfeign;


import com.alibaba.fastjson.JSONObject;
import com.i2dsp.maintenance.domain.dto.DeviceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : 梁海聪
 * @since : 2021/07/02
 *
 */
@FeignClient("i2dspEmgData")
@Component
public interface I2dspEmgData {

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @GetMapping("/user/get/info")
    JSONObject getUserInfo(@RequestHeader(defaultValue = "-1") Long userId);

    /**
     * 根据用户id查询该用户的产品类型名
     * @param userId
     * @return
     */
    @GetMapping("/user/device/count/byProd")
    JSONObject getProductsByUserId(@RequestHeader(defaultValue = "1") Long userId);

    /**
     * 根据用户id,产品类型名,设备编号数组查询设备信息
     * @param userId
     * @param productName
     * @param deviceIdArr
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @PostMapping("/user/device/get")
    JSONObject getDevice(@RequestHeader(defaultValue = "-1") Long userId, @RequestParam String productName, @RequestParam String[] deviceIdArr, @RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "1") Integer pageSize);

    /**
     * 根据用户id,产品类型名查询设备信息
     * @param userId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @PostMapping("/user/device/get/simple")
    JSONObject getDeviceSimple(@RequestHeader(defaultValue = "-1") Long userId, @SpringQueryMap DeviceDto deviceDto, @RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "1") Integer pageSize);
}
