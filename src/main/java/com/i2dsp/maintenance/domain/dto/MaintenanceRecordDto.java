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
 * 查询保养记录的详情信息封装类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceRecordDto implements Serializable {

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
    @Length(max = 100,message = "长度不能超过100字符")
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
     * 保养内容id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contentId;

    /**
     * 检查结果,0为未填写，1为表示是，2表示否
     */
    private Integer checkStatus;

    /**
     * 保养内容名
     */
    private String contentName;
}
