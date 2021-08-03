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
 * 保养类型和保养内容的关系管理中间表
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TypeHasContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 保养类型Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long typeId;

    /**
     * 保养内容Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contentId;

    /**
     * 逻辑删除字段，0代表未删除，1代表已删除
     */
    private Boolean isDeleted;


    public TypeHasContent(Long typeId, Long contentId) {
        this.typeId = typeId;
        this.contentId = contentId;
        this.isDeleted = false;
    }
}
