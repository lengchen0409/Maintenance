package com.i2dsp.maintenance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.i2dsp.maintenance.domain.MaintenanceLastRecord;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndTypeVo;
import com.i2dsp.maintenance.mapper.MaintenanceLastRecordMapper;
import com.i2dsp.maintenance.service.IMaintenanceLastRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 保养最新记录 服务实现类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-07-27
 */
@Service
public class MaintenanceLastRecordServiceImpl extends ServiceImpl<MaintenanceLastRecordMapper, MaintenanceLastRecord> implements IMaintenanceLastRecordService {

    @Autowired
    private MaintenanceLastRecordMapper maintenanceLastRecordMapper;

    /**
     * 条件查询最新保养记录信息
     * @param maintenanceRecordAndTypeVo
     * @return
     */
    @Override
    public List<MaintenanceRecordAndTypeVo> searchLastRecordAndType(MaintenanceRecordAndTypeVo maintenanceRecordAndTypeVo) {
        return maintenanceLastRecordMapper.searchLastRecordAndType(maintenanceRecordAndTypeVo);
    }

    /**
     * 条件查询该设备下最新的保养记录和保养类型
     * @param maintenanceRecordAndTypeVo
     * @return
     */
    @Override
    public List<MaintenanceRecordAndTypeVo> searchLastRecordAndTypeByDevice(MaintenanceRecordAndTypeVo maintenanceRecordAndTypeVo) {
        return maintenanceLastRecordMapper.searchLastRecordAndTypeByDevice(maintenanceRecordAndTypeVo);
    }

    /**
     * 条件查询所有最新保养记录和保养类型
     * @param maintenanceRecordAndTypeVo
     * @return
     */
    @Override
    public List<MaintenanceRecordAndTypeVo> searchLastRecordAndTypeByAll(MaintenanceRecordAndTypeVo maintenanceRecordAndTypeVo) {
        return maintenanceLastRecordMapper.searchLastRecordAndTypeByAll(maintenanceRecordAndTypeVo);
    }
}
