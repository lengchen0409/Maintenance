package com.i2dsp.maintenance.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 设备和产品类型封装类
 * @author : 梁海聪
 * @since : 2021/07/13 10:50
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 产品类型名
     */
    private String productName;
}
