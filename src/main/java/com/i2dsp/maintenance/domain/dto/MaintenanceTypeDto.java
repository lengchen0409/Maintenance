package com.i2dsp.maintenance.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * description 保养类型数据传输对象
 * date: 2021-07-01 09:56
 *
 * @author 林隆星
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceTypeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 保养类型id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long typeId;

    /**
     * 保养类型名
     */
    @NotNull(message = "保养类型名不能为空")
    @Length(min = 4, max = 50,message = "保养类型名至少要4个字符")
    private String typeName;

    /**
     * 周期时间单位（y：年、m：月、w：周、d:日）
     */
    @NotNull(message = "不能为空")
    @Length(max = 1,message = "长度不能超过1字符")
    private String periodScope;

    /**
     * 周期时间数值
     */
    @NotNull(message = "不能为空")
    @Range(min = 1, max = 365, message = "保养周期数值不合理")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer periodNumber;

    /**
     * 提醒时间数值
     */
    @NotNull(message = "不能为空")
    @Range(min = 0, max = 28, message = "提前提醒天数不合理")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer remindNumber;

    /**
     * 描述
     */
    @Length(max = 100,message = "长度不能超过100字符")
    private String description;

    /**
     * 保养内容Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> contents;
}
