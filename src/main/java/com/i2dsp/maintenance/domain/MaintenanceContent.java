package com.i2dsp.maintenance.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.i2dsp.maintenance.domain.dto.MaintenanceContentDto;
import com.i2dsp.maintenance.utils.TimestampUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * <p>
 * 存储保养内容信息
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 保养内容id
     */
    @TableId(value = "content_id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contentId;

    /**
     * 保养内容名
     */
    private String contentName;

    /**
     * 是否需要判断，0为不需要，1为需要
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer isJudge;

    /**
     * 是否添加图片，0为不添加，1为添加
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer isUpload;

    /**
     * 是否必须填写备注，0为非必填，1为必填
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer isRemark;

    /**
     * 描述
     */
    private String description;

    /**
     * 逻辑删除字段，0代表未删除，1代表已删除
     */
    @JsonIgnore
    private Integer isDeleted;

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
     * 整合保养内容信息新建对象
     * @param contentDto 请求信息
     * @param userId userId
     */
    public MaintenanceContent(MaintenanceContentDto contentDto, Long userId){
        this.contentName = contentDto.getContentName();
        this.isJudge = contentDto.getIsJudge();
        this.isUpload = contentDto.getIsUpload();
        this.isRemark = contentDto.getIsRemark();
        this.description = contentDto.getDescription();
        this.createUserId = userId;
        this.gmtCreate = TimestampUtils.getCurrentTimestamp();
        this.gmtModified =  this.gmtCreate;
    }
}
