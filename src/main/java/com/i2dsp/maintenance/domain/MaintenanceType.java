package com.i2dsp.maintenance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.config.exception.GlobalException;
import com.i2dsp.maintenance.domain.dto.MaintenanceTypeDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndTypeVo;
import com.i2dsp.maintenance.utils.TimestampUtils;
import com.i2dsp.maintenance.utils.TypeMapUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 存储保养类型信息
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Accessors(chain = true)
public class MaintenanceType implements Serializable {

    private static final long serialVersionUID = 1L;
    //一天的时间戳
    private static final Long  DAYTIME = 86400000L;

    /**
     * 保养类型id
     */
    @TableId(value = "type_id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long typeId;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer periodNumber;

    /**
     * 保养周期时间戳
     */
    private String period;

    /**
     * 提醒时间数值
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer remindNumber;

    /**
     * 描述
     */
    private String description;

    /**
     * 逻辑删除字段，0代表未删除，1代表已删除
     */
    @JsonIgnore
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Boolean isDeleted;

    /**
     * 创建人Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createUserId;


    /**
     * 创建时间
     */
    private String gmtCreate;

    /**
     * 修改时间
     */
    private String gmtModified;

    /**
     * 保养类型是否可编辑，1为可编辑，0为不可编辑
     */
    @TableField(exist = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer editStatus;

    /**
     * 保养类型已分配的保养产品名集合
     */
    @TableField(exist = false)
    private List<String> productNames;

    /**
     * 保养类型已分配的保养产品名集合
     */
    @TableField(exist = false)
    private String allocationTime;


    public MaintenanceType(MaintenanceTypeDto maintenanceTypeDto, Long userId) {
        this.typeName = maintenanceTypeDto.getTypeName();
        this.periodScope = maintenanceTypeDto.getPeriodScope();
        this.periodNumber = maintenanceTypeDto.getPeriodNumber();
        this.remindNumber = maintenanceTypeDto.getRemindNumber();
        this.description = maintenanceTypeDto.getDescription();
        this.createUserId = userId;
        this.gmtCreate = TimestampUtils.getCurrentTimestamp();
        this.gmtModified = gmtCreate;
    }

    //赋值
    public MaintenanceType(MaintenanceRecordAndTypeVo maintenanceRecordAndTypeVo) {
        this.typeId = maintenanceRecordAndTypeVo.getTypeId();
        this.typeName = maintenanceRecordAndTypeVo.getTypeName();
        this.periodScope = maintenanceRecordAndTypeVo.getPeriodScope();
        this.periodNumber = maintenanceRecordAndTypeVo.getPeriodNumber();
        this.remindNumber = maintenanceRecordAndTypeVo.getRemindNumber();
        this.description = maintenanceRecordAndTypeVo.getDescription();
        this.isDeleted = maintenanceRecordAndTypeVo.getIsDeleted();
        this.createUserId = maintenanceRecordAndTypeVo.getCreateUserId();
        this.gmtCreate = maintenanceRecordAndTypeVo.getTypeGmtCreate();
        this.gmtModified = maintenanceRecordAndTypeVo.getTypeGmtModified();
    }

    //计算保养周期时间戳
    public void findPeriod(){
        Map<String, Integer> typeConstants = TypeMapUtils.typeConstants;
        Integer dayNum = typeConstants.get(this.periodScope);
        Long newPeriod = (dayNum - this.remindNumber) * DAYTIME;
        this.period = String.valueOf(newPeriod);
        if (newPeriod < 0) {
            throw new GlobalException(ResultEnum.ERROR,"提前提醒时间过早");
        }
    }
}

