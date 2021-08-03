package com.i2dsp.maintenance.controller;


import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.domain.dto.MaintenanceTypeDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceTypeVo;
import com.i2dsp.maintenance.service.impl.MaintenanceRecordServiceImpl;
import com.i2dsp.maintenance.service.impl.MaintenanceTypeServiceImpl;
import com.i2dsp.maintenance.service.impl.ProductHasTypeServiceImpl;
import com.i2dsp.maintenance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 存储保养类型信息 前端控制器
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
@RestController
@Validated
public class MaintenanceTypeController {

    @Autowired
    MaintenanceTypeServiceImpl maintenanceTypeService;

    @Autowired
    private ProductHasTypeServiceImpl productHasContentService;

    @Autowired
    private MaintenanceRecordServiceImpl maintenanceRecordService;

    /**
     * 分页多条件查询保养类型
     * @param maintenanceTypeVo 查询条件
     * @param userId 用户Id
     * @return ResultVo
     */
    @GetMapping("/types")
    public ResultVo getType(@Valid MaintenanceTypeVo maintenanceTypeVo, @RequestHeader(defaultValue = "-1") Long userId){
        return maintenanceTypeService.getType(maintenanceTypeVo, userId);
    }

    /**
     * 根据Id查询保养类型
     * @param typeId 保养类型Id
     * @param userId 用户Id
     * @return ResultVo
     */
    @GetMapping("/types/detail")
    public ResultVo getType(@RequestParam("typeId") @NotNull(message = "查询保养类型Id不能为空") Long typeId, @RequestHeader(defaultValue = "-1") Long userId){
        return new ResultVo<>(maintenanceTypeService.getTypeById(typeId));
    }

    /**
     * 新添加保养类型
     * @param maintenanceTypeDto 添加内容
     * @param userId 用户Id
     * @return ResultVo
     */
    @PostMapping("/types")
    public ResultVo insertType(@Valid @RequestBody MaintenanceTypeDto maintenanceTypeDto, @RequestHeader(defaultValue = "-1") Long userId) {
        //添加
        return maintenanceTypeService.insertType(maintenanceTypeDto, userId);
    }

    /**
     * 修改保养类型
     * @param maintenanceTypeDto 修改内容
     * @param userId 用户Id
     * @return ResultVo
     */
    @PutMapping("/types")
    public ResultVo alterType(@Valid @RequestBody MaintenanceTypeDto maintenanceTypeDto, @RequestHeader(defaultValue = "-1") Long userId) {
        //检验保养类型和保养产品是否存在关联关系
        Boolean hasRelevancy =  productHasContentService.hasRelevancy(maintenanceTypeDto.getTypeId());
        if (hasRelevancy) {
            return new ResultVo(ResultEnum.ERROR,"该保养类型存在与保养产品的关联关系，修改失败！");
        }
        //检查该保养类型是否存在保养记录
        Boolean checkRecord = maintenanceRecordService.checkRecord(maintenanceTypeDto.getTypeId());
        if (checkRecord) {
            return new ResultVo(ResultEnum.ERROR, "该保养类型存在保养记录，无法修改！");
        }
        //修改保养类型
        Boolean alterResult = maintenanceTypeService.alterType(maintenanceTypeDto, userId);
        if (alterResult) {
            return new ResultVo(ResultEnum.OK);
        }else {
            return new ResultVo(ResultEnum.ERROR);
        }
    }

    /**
     * 删除指定保养类型
     * @param typeIds 保养类型Id数组
     * @return ResultVo
     */
    @DeleteMapping("/types")
    public ResultVo deleteType(@RequestParam("typeId") Long typeId) {
        maintenanceTypeService.deleteType(typeId);
        return new ResultVo<>(ResultEnum.OK);
    }

    /**
     * 根据产品名查询保养类型
     * @param userId 用户Id
     * @return ResultVo
     */
    @GetMapping("/types/productname")
    public ResultVo getTypesByProductName(@RequestParam("productName") String productName, @RequestHeader(value = "userId",defaultValue = "-1") Long userId) {
        return new ResultVo<>(maintenanceTypeService.getTypeByProductName(productName));
    }
}
