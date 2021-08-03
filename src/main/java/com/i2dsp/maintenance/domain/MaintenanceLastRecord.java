package com.i2dsp.maintenance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.i2dsp.maintenance.domain.dto.MaintenanceRecords;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保养最新记录
 * </p>
 *
 * @author 梁海聪
 * @since 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@ApiModel(value="MaintenanceLastRecord对象", description="保养最新记录")
public class MaintenanceLastRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    public MaintenanceLastRecord(){}

    public MaintenanceLastRecord(MaintenanceRecords maintenanceRecords) {
        this.deviceId = maintenanceRecords.getDeviceId();
        this.typeId = maintenanceRecords.getTypeId();
        this.staffName = maintenanceRecords.getStaffName();
    }

    //@ApiModelProperty(value = "最新保养记录id")
    @TableId(value = "last_record_id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long lastRecordId;

    //@ApiModelProperty(value = "设备编号")
    private String deviceId;

    //@ApiModelProperty(value = "保养类型id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long typeId;

    //@ApiModelProperty(value = "保养人")
    private String staffName;

    //@ApiModelProperty(value = "上一次保养记录时间")
    private String lastRecordTime;



}
