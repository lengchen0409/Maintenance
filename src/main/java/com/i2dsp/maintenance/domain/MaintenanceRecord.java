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
 * 存储保养记录的基本信息
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    public MaintenanceRecord() {

    }

    /**
     * 整合保养记录新建的对象
     * @param maintenanceRecords
     */
    public MaintenanceRecord(MaintenanceRecords maintenanceRecords) {
        this.recordId = maintenanceRecords.getRecordId();
        this.deviceId = maintenanceRecords.getDeviceId();
        this.typeId = maintenanceRecords.getTypeId();
        this.isAbnormal = maintenanceRecords.getIsAbnormal();
        this.remark = maintenanceRecords.getRemark();
        this.staffName = maintenanceRecords.getStaffName();
        this.staffPhone = maintenanceRecords.getStaffPhone();
    }


    /**
     * 保养记录id
     */
    @TableId(value = "record_id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long recordId;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 保养类型
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long typeId;

    /**
     * 设备是否异常，0为正常，1为异常
     */
    private Boolean isAbnormal;

    /**
     * 备注
     */
    private String remark;

    /**
     * 保养员工姓名
     */
    private String staffName;

    /**
     * 保养员工手机号码
     */
    private String staffPhone;

    /**
     * 记录创建人
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createUser;

    /**
     * 创建时间
     */
    private String gmtCreate;


}
