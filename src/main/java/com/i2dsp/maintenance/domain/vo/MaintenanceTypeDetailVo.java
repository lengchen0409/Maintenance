package com.i2dsp.maintenance.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.i2dsp.maintenance.domain.MaintenanceContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * description 保养类型Vo类
 * date: 2021-07-02 09:16
 *
 * @author 林隆星
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MaintenanceTypeDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 保养类型id
     */
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
     * 保养类型已分配的保养产品名集合
     */
    @TableField(exist = false)
    private List<String> productNames;

    /**
     * 保养类型分配时间
     */
    @TableField(exist = false)
    private String allocationTime;

    /**
     * 保养内容
     */
    @TableField(exist = false)
    private List<MaintenanceContent> contents;
}
