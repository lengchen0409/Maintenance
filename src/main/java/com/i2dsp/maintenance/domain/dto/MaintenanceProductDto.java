package com.i2dsp.maintenance.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * description 产品数据传输对象
 * date: 2021-07-02 15:24
 *
 * @author 林隆星
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceProductDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 产品名
     */
    @NotNull(message = "产品名不能为空")
    private String productName;

//    /**
//     * 产品Id
//     */
//    @NotNull(message = "产品Id不能为空")
//    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    private Long productId;

    /**
     * 产品拥有的保养类型
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> typeIds;

}
