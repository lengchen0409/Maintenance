package com.i2dsp.maintenance.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * description 保养类型 Vo类
 * date: 2021-07-01 09:37
 *
 * @author 林隆星
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer pageSize = 0;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer pageNum = 1;

    /**
     * 保养类型名
     */
    @Length(max = 50,message = "长度不能超过50字符")
    private String typeName;

    @Range(min = 0,max = 1,message = "allocationStatus只能为 0 或 1 ")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer allocationStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long typeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @Length(max = 50,message = "长度不能超过50字符")
    private String productName;
}
