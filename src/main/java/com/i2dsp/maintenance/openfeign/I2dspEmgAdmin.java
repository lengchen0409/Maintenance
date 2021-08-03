package com.i2dsp.maintenance.openfeign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * description
 * date: 2021-07-05 13:03
 *
 * @author 林隆星
 */
@Component
@FeignClient("i2dspEmgAdmin")
public interface I2dspEmgAdmin {

    /**
     * 查询产品信息
     * @param pageNumber
     * @param pageSize
     * @param productName
     * @return
     */
    @GetMapping(value = "/product/get")
    JSONObject getProducts(@RequestParam(value = "pageNumber",defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize",defaultValue = "0") Integer pageSize,@RequestParam("productName") String productName);

}
