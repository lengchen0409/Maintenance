package com.i2dsp.maintenance.domain.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 存储保养记录的详情信息封装类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceRecords implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 保养记录id
     */
    @TableId(value = "maintain_staff_id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long recordId;

    /**
     * 设备id
     */
    @NotNull(message = "设备id不能为空")
    private String deviceId;

    /**
     * 保养类型id
     */
    @NotNull(message = "保养类型不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long typeId;

    /**
     * 设备是否异常，0为正常，1为异常
     */
    @NotNull(message = "设备状态不能为空")
    private Boolean isAbnormal;

    /**
     * 备注
     */
    @NotNull(message = "备注信息不能为空")
    @Length(max = 100,message = "长度不能超过100字符")
    private String remark;

    /**
     * 保养员工姓名
     */
    @NotNull(message = "员工姓名不能为空")
    private String staffName;

    /**
     * 保养员工电话
     */
    @NotNull(message = "员工电话不能为空")
    private String staffPhone;

    /**
     * 保养内容id
     */
    private String[] contentId;

    /**
     * 检查结果,0为未填写,1为表示是,2为表示否
     */
    private Integer[] checkStatus;

    /**
     * 备注
     */
    private String remarks;

}
