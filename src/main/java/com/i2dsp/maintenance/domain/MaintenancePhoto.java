package com.i2dsp.maintenance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 存储保养记录的图片信息
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenancePhoto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图片id
     */
    @TableId(value = "photo_id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long photoId;

    /**
     * 保养记录详情id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long recordDetailId;

    /**
     * 图片路径
     */
    private String photoPath;

    /**
     * 图片类型
     */
    private String photoType;

    /**
     * 创建时间
     */
    private String gmtCreate;


}
