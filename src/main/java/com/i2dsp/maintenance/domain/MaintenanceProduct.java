package com.i2dsp.maintenance.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 保养产品实体类
 * @author 林隆星
 * @since 2021-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaintenanceProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 采用新国标中产品型号命名规则
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
      private Long productId;

    /**
     * 采用新国标中产品型号命名规则
     */
    private String productCode;

    /**
     * 产品名称SD103
     */
    private String productName;

    /**
     * 设备节点类型  1 直接连远端  2 网关中继类型  3 终端设备
     */
    private Integer nodeType;

    /**
     * 设备传输数据数，据格式形式，json 或其他 -保留字段
     */
    private String dataFormat;

    /**
     * 设备联网方式 WiFi 4G NB-ioT 433 两线制 powerbus等
     */
    private String netType;

    /**
     * 描述该产品的行业类别  如：消防报警  水质检测 消防广播 等
     */
    private String category;

    /**
     * 该产品的一些细节说明
     */
    private String productDescription;

    /**
     * 产品图片
     */
    private String profileImages;

    /**
     * 默认 0   1 表示逻辑删除
     */
    private Boolean isDelete;

    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableField(exist = false)
    private List<String> types;


}
