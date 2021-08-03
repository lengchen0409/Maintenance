package com.i2dsp.maintenance.utils;

import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndDetailAndPhotoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description 保养记录excel文件输出内容实体类
 * date: 2021-07-08 10:21
 *
 * @author 林隆星
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RecoderPrintBean {

    /**
     * 序号
     */
    private String number;

    /**
     * 保养记录时间
     */
    private String gmtCreate;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 保养类型名
     */
    private String typeName;

    /**
     * 保养员工姓名
     */
    private String staffName;

    /**
     * 保养员工手机号码
     */
    private String staffPhone;

    /**
     * 记录创建人Id
     */
    private String createUser;

    /**
     * 设备是否异常，0为正常，1为异常
     */
    private String isAbnormal;

    /**
     * 备注
     */
    private String remark;


    public static RecoderPrintBean getOne(MaintenanceRecordAndDetailAndPhotoVo record) {
        RecoderPrintBean one = new RecoderPrintBean();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(Long.parseLong(record.getGmtCreate()));
        one.setGmtCreate(format.format(date));
        one.setDeviceId(record.getDeviceId());
        one.setTypeName(record.getMaintenanceTypeList().get(0).getTypeName());
        one.setRemark(record.getRemark());
        one.setStaffName(record.getStaffName());
        one.setStaffPhone(record.getStaffPhone());
        one.setIsAbnormal(record.getIsAbnormal() ? "√" : "X" );
        return one;
    }
}