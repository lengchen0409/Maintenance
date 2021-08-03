package com.i2dsp.maintenance.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MaintenanceCountDto implements Serializable {

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
     * 保养周期单位
     */
    private String periodScope;

    /**
     * 保养周期数
     */
    private Integer periodNumber;

    /**
     * 数量
     */
    private Integer counts;
}
