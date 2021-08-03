package com.i2dsp.maintenance.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * description 保养类型数据传输对象
 * date: 2021-06-29 09:44
 *
 * @author 林隆星
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceContentDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long contentId;

    /**
     * 保养内容名
     */
    @NotNull(message = "保养内容不能为空")
    @Length(min = 4, max = 50,message = "保养内容至少要4个字符")
    private String contentName;

    /**
     * 是否需要判断，0为不需要，1为需要
     */
    @Range(min = 0, max = 1,message = "isJudge只能是0或1")
    @NotNull(message = "检查判断配置不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer isJudge;

    /**
     * 是否添加图片，0为不添加，1为添加
     */
    @Range(min = 0, max = 1,message = "isUpload只能是0或1")
    @NotNull(message = "图片上传配置不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer isUpload;

    /**
     * 是否必须填写备注，0为非必填，1为必填
     */
    @Range(min = 0, max = 1,message = "isRemark只能是0或1")
    @NotNull(message = "备注配置不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer isRemark;

    /**
     * 描述
     */
    @Length(max = 100,message = "长度不能超过100字符")
    private String description;



}
