package com.i2dsp.maintenance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.i2dsp.maintenance.domain.Device;
import com.i2dsp.maintenance.domain.MaintenanceType;
import com.i2dsp.maintenance.domain.dto.DeviceDto;
import com.i2dsp.maintenance.domain.dto.MaintenanceCountDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndTypeVo;
import com.i2dsp.maintenance.openfeign.I2dspEmgData;
import com.i2dsp.maintenance.utils.TimestampUtils;
import com.i2dsp.maintenance.utils.TypeMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 线程实现类
 * @author : 梁海聪
 * @since : 2021/07/13 11:16
 */
@Service
public class AsyncServiceImpl {

    @Autowired
    private I2dspEmgData i2dspEmgData;

    @Autowired
    private MaintenanceRecordServiceImpl maintenanceRecordService;

    //一年的毫秒数
    static final long Year = 24 * 60 * 60 * 360 *1000l;

    /**
     * 线程： 负责调用查询设备接口
     * @param userId
     * @param productName
     * @return
     */
    @Async
    public Future<JSONObject> getResponse(Long userId, String productName) {
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setProductName(productName);
        JSONObject response = i2dspEmgData.getDeviceSimple(userId, deviceDto, 1, 0);
        return new AsyncResult<>(response);
    }

    /**
     * 线程： 负责调用查询设备接口
     * @param userId
     * @param deviceDto
     * @return
     */
    @Async
    public Future<JSONObject> getResponses(Long userId, DeviceDto deviceDto) {
        JSONObject response = i2dspEmgData.getDeviceSimple(userId, deviceDto, 1, 0);
        return new AsyncResult<>(response);
    }

    /**
     * 线程： 负责查询出待保养的设备
     * @param maps
     * @param listMap
     * @param map
     * @param devices
     * @return
     */
    @Async
    public Future<String> getMaintenanceCounts(Map<Long, MaintenanceCountDto> maps, Map<String, List<MaintenanceType>> listMap, Map<Long, MaintenanceRecordAndTypeVo> map, List<Device> devices) {

        //遍历设备
        for (Device device : devices) {
            //存储通过设备查询到的记录中存在的保养类型
            List<Long> maintenanceTypes = new ArrayList<>();
            //根据设备查询记录和保养类型
            List<MaintenanceRecordAndTypeVo> recordAndTypeVos = maintenanceRecordService.searchRecordAndTypeByDeviceId(device.getDeviceId());
            for (MaintenanceRecordAndTypeVo recordAndTypeVo : recordAndTypeVos) {
                //MaintenanceType maintenanceType = new MaintenanceType(recordAndTypeVo);
                //把保养类型添加进集合中
                maintenanceTypes.add(recordAndTypeVo.getTypeId());
                map.put(recordAndTypeVo.getTypeId(), recordAndTypeVo);
            }
            //根据该设备的产品名获得listmap中的保养类型列表
            List<MaintenanceType> maintenanceTypeList = listMap.get(device.getProductName());
            //遍历得到保养类型对象
            for (MaintenanceType maintenanceType : maintenanceTypeList) {
                //如果该设备保养记录存在
                if (maintenanceTypes.contains(maintenanceType.getTypeId())) {
                    //获取当前时间与最新记录时间的差值获得天数
                    long time = System.currentTimeMillis() - Long.valueOf(map.get(maintenanceType.getTypeId()).getGmtCreate());
                    //如果天数大于保养的周期时间
                    if (time > Long.valueOf(maintenanceType.getPeriod())) {
                        //调用封装好的方法，把需要待保养则存储在map中
                        updateMaps(maps, maintenanceType);
                    }
                } else {
                    //用当前时间戳与当年一月一日时间戳相减获得天数
                    long time = System.currentTimeMillis() - TimestampUtils.getAppointTimestamp();
                    if (maintenanceType.getPeriodScope().equals("y")) {
                        //如果天数大于保养周期时间
                        if (time > Year) {
                            //调用封装好的鹅方法，把需要待保养则存储在map中
                            updateMaps(maps, maintenanceType);
                        }
                    }else if (!maintenanceType.getPeriodScope().equals("y") && time > Long.valueOf(maintenanceType.getPeriod())) {
                        //调用封装好的鹅方法，把需要待保养则存储在map中
                        updateMaps(maps, maintenanceType);
                    }
                }
            }
        }
        return new AsyncResult<>("succes");
    }

    /**
     * 根据是否需要待保养来存储在map中
     * @param maps
     * @param maintenanceType
     */
    private synchronized void updateMaps(Map<Long, MaintenanceCountDto> maps, MaintenanceType maintenanceType) {
        //判断map中是否已存在该保养类型id的key
        if (maps.containsKey(maintenanceType.getTypeId())) {
            //获得该key值下面的value对象
            MaintenanceCountDto maintenanceCountDto = maps.get(maintenanceType.getTypeId());
            //给maintenanceCountDto的counts属性的值加1
            maintenanceCountDto.setCounts(maintenanceCountDto.getCounts()+1);
            //重新放入map中
            maps.put(maintenanceType.getTypeId(),maintenanceCountDto);
        }else {
            //不存在则新增
            MaintenanceCountDto maintenanceCountDto = new MaintenanceCountDto();
            //给maintenanceCountDto对象赋初值
            maintenanceCountDto.setTypeId(maintenanceType.getTypeId()).setTypeName(maintenanceType.getTypeName())
                    .setPeriodScope(maintenanceType.getPeriodScope())
                    .setPeriodNumber(maintenanceType.getPeriodNumber())
                    .setCounts(1);
            //重新放入map中
            maps.put(maintenanceType.getTypeId(), maintenanceCountDto);
        }
    }

}


