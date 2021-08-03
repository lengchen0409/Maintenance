package com.i2dsp.maintenance.utils;

/**
 * Author: 梁海聪
 * DateTime: 2021/6/30  8:40
 * Description: TODO 检查页面参数是否为空
 */
public class CheckPageIsNull {
    /**
     * 校验页面参数是否合法
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static boolean checkPageParamsIsNull(Integer pageNum, Integer pageSize) {
        return pageNum != null && pageSize != null;

    }
}
