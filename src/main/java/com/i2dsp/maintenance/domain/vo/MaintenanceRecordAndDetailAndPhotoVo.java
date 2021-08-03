package com.i2dsp.maintenance.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.i2dsp.maintenance.domain.MaintenancePhoto;
import com.i2dsp.maintenance.domain.MaintenanceRecordDetail;
import com.i2dsp.maintenance.domain.MaintenanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 查询保养记录的详情信息封装类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceRecordAndDetailAndPhotoVo implements Serializable {

    private static final long serialVersionUID = 1L;
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

    /**
     * 保养类型列表
     */
    private List<MaintenanceType> maintenanceTypeList;

    /**
     * 保养记录详情列表
     */
    private List<MaintenanceRecordDetail> maintenanceRecordDetailList;




}
