package com.i2dsp.maintenance.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageInfo;
import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.domain.dto.MaintenanceRecordDto;
import com.i2dsp.maintenance.domain.dto.MaintenanceRecords;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndDetailAndPhotoVo;
import com.i2dsp.maintenance.openfeign.I2dspEmgData;
import com.i2dsp.maintenance.service.impl.MaintenanceRecordServiceImpl;
import com.i2dsp.maintenance.utils.CheckPageIsNull;
import com.i2dsp.maintenance.utils.ListPageUtils;
import com.i2dsp.maintenance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 存储保养记录的基本信息 前端控制器
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
@RestController
@RequestMapping("/user")
public class MaintenanceRecordController {

    @Autowired
    private MaintenanceRecordServiceImpl maintenanceRecordService;

    @Autowired
    private I2dspEmgData i2dspEmgData;
    /**
     * 填写保养记录
     * @param userId
     * @param maintenanceRecords
     * @param fileImages
     * @param fileNumber
     * @return
     */
    @PostMapping("/records")
    public ResultVo<Integer> fillMaintenanceRecord(@RequestHeader(defaultValue = "-1") Long userId, @Valid MaintenanceRecords maintenanceRecords, MultipartFile[] fileImages, Integer[] fileNumber) {
        return maintenanceRecordService.fillMaintenanceRecord(userId, maintenanceRecords, fileImages, fileNumber);
    }

    /**
     * 查询保养记录详情
     * @param userId
     * @param maintenanceRecordDto
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/records")
    public ResultVo<ListPageUtils<MaintenanceRecordAndDetailAndPhotoVo>> searchMaintenances(@RequestHeader(defaultValue = "-1") Long userId, MaintenanceRecordDto maintenanceRecordDto, Integer pageNum, Integer pageSize) {
        if (!CheckPageIsNull.checkPageParamsIsNull(pageNum, pageSize)) {
            return new ResultVo<>(20400,"请输入页面参数");
        }
        //条件查询该用户下的所有保养记录
        List<MaintenanceRecordAndDetailAndPhotoVo> recordAndDetailAndPhotoVos = maintenanceRecordService.searchMaintenancesByAll(userId, maintenanceRecordDto);
        //判断集合是否为空
        if (recordAndDetailAndPhotoVos.size() > 0) {
            return new ResultVo<>(ResultEnum.OK, new ListPageUtils<>(recordAndDetailAndPhotoVos, pageNum, pageSize));
        }
        return new ResultVo<>(ResultEnum.OK, "该用户下的设备没有保养记录");
    }

    /**
     * 查生成保养记录的excel文件
     * @param userId
     * @param maintenanceRecordDto
     * @return
     * @author 林隆星
     */
    @GetMapping("/records/print")
    public void  printRecords(@RequestHeader(defaultValue = "-1") Long userId, MaintenanceRecordDto maintenanceRecordDto, HttpServletResponse response) throws IOException {
        maintenanceRecordService.getExcelFile(userId, maintenanceRecordDto, response);
    }

}
