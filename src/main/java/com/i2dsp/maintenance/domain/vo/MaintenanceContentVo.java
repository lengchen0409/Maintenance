package com.i2dsp.maintenance.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.simpleframework.xml.Default;

import java.io.Serializable;

/**
 * description Content Vo类
 * date: 2021-06-29 09:34
 *
 * @author 林隆星
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceContentVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer pageSize = 0;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer pageNum = 1;

    /**
     * 保养内容名
     */
    @Length(max = 100,message = "长度不能超过100字符")
    private String contentName;

    private Long contentId;

    //保养类型ID，配合allocationStatus查找已分配或未分配的保养条目
    private Long typeId;

    //分配状态,0是未分配，1是已分配
    private Integer allocationStatus;

}
