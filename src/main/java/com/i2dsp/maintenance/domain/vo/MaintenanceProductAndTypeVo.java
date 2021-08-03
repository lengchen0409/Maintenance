package com.i2dsp.maintenance.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 产品和保养类型封装实体类
 * @author : 梁海聪
 * @since : 2021/07/26 14:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceProductAndTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品和保养类型关联id
     */
    private Long id;

    /**
     * 产品id
     */
    private String productId;

    /**
     * 产品名
     */
    private String productName;

    /**
     * 保养类型id
     */
    private Long typeId;

    /**
     * 保养类型名
     */
    private String typeName;

    /**
     * 周期时间单位
     */
    private String periodScope;

    /**
     * 周期时间数值
     */
    private Integer periodNumber;

    /**
     * 距离保养提醒天数
     */
    private Integer remindNumber;

    /**
     * 保养类型周期时间戳
     */
    private String period;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private String gmtCreate;

    /**
     * 修改时间
     */
    private String gmtModified;

}
