package com.i2dsp.maintenance.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 查询待保养设备的封装类
 * </p>
 *
 * @author : 梁海聪
 * @since : 2021/07/02  16:00
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MaintenancePendingDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 产品类型
     */
    private String productName;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 建筑名
     */
    private String buildingName;

    /**
     * 楼层名
     */
    private String floorName;

    /**
     * 具体地址
     */
    private String place;

    /**
     * 上次记录时间
     */
    private String lastRecordedTime;

    /**
     * 上一次保养人
     */
    private String lastStaffName;

    /**
     * 保养类型名
     */
    private String typeName;

}
