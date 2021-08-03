package com.i2dsp.maintenance.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 保养记录和保养类型封装类
 * @author : 梁海聪
 * @since : 2021/07/05
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MaintenanceRecordAndTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 保养记录id
     */
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
     * 保养类型名
     */
    private String typeName;

    /**
     * 周期时间单位（y：年、m：月、w：周、d:日）
     */
    private String periodScope;

    /**
     * 周期时间数值
     */
    private Integer periodNumber;

    /**
     * 提醒时间数值
     */
    private Integer remindNumber;

    /**
     * 保养周期时间戳
     */
    private String period;

    /**
     * 描述
     */
    private String description;

    /**
     * 逻辑删除字段，0代表未删除，1代表已删除
     */
    @JsonIgnore
    private Boolean isDeleted;

    /**
     * 创建人Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createUserId;


    private String typeGmtCreate;

    /**
     * 修改时间
     */
    private String typeGmtModified;

    /**
     * 产品名
     */
    private String productName;
}
