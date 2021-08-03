package com.i2dsp.maintenance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 存储保养记录的保养内容信息
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceRecordDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 保养记录详情id
     */
    @TableId(value = "record_detail_id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long recordDetailId;

    /**
     * 保养记录表id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long recordId;

    /**
     * 保养内容id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contentId;

    /**
     * 检查结果,0为未填写，1为表示是，2表示否
     */
    private Integer checkStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 保养图片列表
     */
    private List<MaintenancePhoto> maintenancePhotoList;

    /**
     * 保养内容列表
     */
    private List<MaintenanceContent> maintenanceContentList;


}
