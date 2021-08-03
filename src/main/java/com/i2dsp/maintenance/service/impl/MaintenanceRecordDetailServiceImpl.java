package com.i2dsp.maintenance.service.impl;

import com.i2dsp.maintenance.domain.MaintenanceRecord;
import com.i2dsp.maintenance.domain.MaintenanceRecordDetail;
import com.i2dsp.maintenance.mapper.MaintenanceRecordDetailMapper;
import com.i2dsp.maintenance.service.IMaintenanceRecordDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <p>
 * 存储保养记录的保养内容信息 服务实现类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
@Service
public class MaintenanceRecordDetailServiceImpl extends ServiceImpl<MaintenanceRecordDetailMapper, MaintenanceRecordDetail> implements IMaintenanceRecordDetailService {


    /**
     * 保存保养记录详情
     * @param maintenanceRecordDetail
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Long saveMaintenanceRecordDetail(MaintenanceRecordDetail maintenanceRecordDetail) throws Exception {
        Long recordDetailId = Math.abs(UUID.randomUUID().getMostSignificantBits());
        maintenanceRecordDetail.setRecordDetailId(recordDetailId);
        if (save(maintenanceRecordDetail)) {
            return recordDetailId;
        }
        throw new Exception("保养记录详情填写失败");
    }
}
