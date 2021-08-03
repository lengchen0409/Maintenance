package com.i2dsp.maintenance.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.domain.MaintenanceType;
import com.i2dsp.maintenance.domain.ProductHasType;
import com.i2dsp.maintenance.domain.dto.DeviceDto;
import com.i2dsp.maintenance.domain.dto.MaintenanceCountDto;
import com.i2dsp.maintenance.domain.dto.MaintenancePendingDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceProductAndTypeVo;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndTypeVo;
import com.i2dsp.maintenance.openfeign.I2dspEmgData;
import com.i2dsp.maintenance.service.impl.*;
import com.i2dsp.maintenance.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-29
 */
@RestController
@RequestMapping("/require")
public class MaintenanceRequireController {

    @Autowired
    private MaintenanceRecordServiceImpl maintenanceRecordService;
    @Autowired
    private MaintenanceTypeServiceImpl maintenanceTypeService;
    @Autowired
    private ProductHasTypeServiceImpl productHasTypeService;
    @Autowired
    private I2dspEmgData i2dspEmgData;
    @Autowired
    private MaintenceProductServiceImpl maintenceProductService;
    @Autowired
    private MaintenanceRequireServiceImpl maintenanceRequireService;
    @Autowired
    private AsyncServiceImpl asyncService;

    /**
     * 查询待保养设备(未启用)
     * @param userId
     * @param typeId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pendings")
    public ResultVo<ListPageUtils<MaintenancePendingDto>> searchMaintenancePending(@RequestHeader(defaultValue = "-1") Long userId, Long typeId, String staffName, DeviceDto deviceDto, Integer pageNum, Integer pageSize) {
        //页面参数校验
        if (!CheckPageIsNull.checkPageParamsIsNull(pageNum, pageSize)) {
            return new ResultVo<>(20400, "请输入页面参数");
        }
        List<MaintenancePendingDto> maintenancePendingDtoList = maintenanceRequireService.searchMaintenancePending(userId, typeId, staffName, deviceDto);
        //如果待保养集合中存在设备则返回
        if (maintenancePendingDtoList.size() > 0) {
            return new ResultVo<>(ResultEnum.OK,new ListPageUtils<>(maintenancePendingDtoList,pageNum,pageSize));
        }
        //不存在待保养设备
        return new ResultVo<>(20000,"查询成功,不存在待保养设备", null);
    }

    /**
     * 查询待保养设备数量
     * @param userId
     * @param productName
     * @return
     */
    @GetMapping("/counts")
    public ResultVo<List<MaintenanceCountDto>> searchMaintenanceCounts(@RequestHeader(defaultValue = "-1") Long userId, String productName) throws ExecutionException, InterruptedException {
        //查询待保养设备记录数
        List<MaintenanceCountDto> maintenanceCountDtoList = maintenanceRequireService.searchMaintenanceCounts(userId, productName);
        //判断该集合是否为空
        if (maintenanceCountDtoList.size() > 0) {
            return new ResultVo<>(ResultEnum.OK, maintenanceCountDtoList);
        }
        return new ResultVo<>(ResultEnum.OK, null);
    }

    /**
     * 条件查询待保养设备
     * @param userId
     * @param maintenanceProductAndTypeVo
     * @param deviceDto
     * @param staffName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/needs")
    public ResultVo<ListPageUtils<MaintenancePendingDto>> searchMaintenanceNeeds(@RequestHeader(defaultValue = "-1") Long userId, MaintenanceProductAndTypeVo maintenanceProductAndTypeVo, DeviceDto deviceDto, String staffName, Integer pageNum, Integer pageSize) {
        //判断页面参数
        if (!CheckPageIsNull.checkPageParamsIsNull(pageNum, pageSize)) {
            return new ResultVo<>(20400,"请输入页面参数");
        }
        //条件查询待保养设备
        List<MaintenancePendingDto> maintenancePendingDtoList = maintenanceRequireService.searchMaintenanceNeed(userId, maintenanceProductAndTypeVo, deviceDto, staffName);
        //判断该集合是否为空
        if (maintenancePendingDtoList.size() > 0) {
            return new ResultVo<>(ResultEnum.OK, new ListPageUtils<>(maintenancePendingDtoList, pageNum, pageSize));
        }
        return new ResultVo<>(ResultEnum.OK, null);
    }

    /**
     * 条件查询待保养设备数量
     * @param userId
     * @param maintenanceProductAndTypeVo
     * @param deviceDto
     * @param staffName
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/count")
    public ResultVo<List<MaintenanceCountDto>> searchMaintenanceCount(@RequestHeader(defaultValue = "-1") Long userId, MaintenanceProductAndTypeVo maintenanceProductAndTypeVo, DeviceDto deviceDto, String staffName) throws ExecutionException, InterruptedException {
        //查询待保养设备记录数
        List<MaintenanceCountDto> maintenanceCountDtoList = maintenanceRequireService.searchMaintenanceCount(userId, maintenanceProductAndTypeVo, deviceDto, staffName);
        //判断该集合是否为空
        if (maintenanceCountDtoList.size() > 0) {
            return new ResultVo<>(ResultEnum.OK, maintenanceCountDtoList);
        }
        return new ResultVo<>(ResultEnum.OK, null);
    }
}
