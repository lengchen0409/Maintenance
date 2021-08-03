package com.i2dsp.maintenance.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeviceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 设备编号集合
     */
    private List<String> deviceIdArr;

    /**
     * 产品名
     */
    private String productName;

    /**
     * 产品名数组
     */
    private List<String> productNames;

    /**
     * 省
     */
    private Long provinceCode;

    /**
     * 市
     */
    private Long cityCode;

    /**
     * 城
     */
    private Long countyCode;

    /**
     * 区/镇
     */
    private Long townCode;

    /**
     * 工程项目id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectId;
}
