package com.i2dsp.maintenance.controller;


import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.domain.dto.MaintenanceContentDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceContentVo;
import com.i2dsp.maintenance.service.impl.MaintenanceContentServiceImpl;
import com.i2dsp.maintenance.service.impl.TypeHasContentServiceImpl;
import com.i2dsp.maintenance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 存储保养条目信息 前端控制器
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
@RestController
@Validated
public class MaintenanceContentController {
    @Autowired
    MaintenanceContentServiceImpl maintenanceContentService;

    @Autowired
    private TypeHasContentServiceImpl typeHasContentService;

    /**
     * 分页多条件查询保养条目
     * @param maintenanceContentVo 查询条件
     * @param userId 用户Id
     * @return ResultVo
     */
    @GetMapping("/contents")
    public ResultVo getContent(@Valid MaintenanceContentVo maintenanceContentVo, @RequestHeader(defaultValue = "-1") Long userId){
        return maintenanceContentService.getContent(maintenanceContentVo, userId);
    }


    /**
     * 新添加保养条目,批量添加
     * @param contentDto 新添内容
     * @param userId 用户Id
     * @return ResultVo
     */
    @PostMapping("/contents")
    public ResultVo insertContent(@Valid @RequestBody List<MaintenanceContentDto> contentDto, @RequestHeader(defaultValue = "-1") Long userId) {
        //新添加保养条目
        return maintenanceContentService.insertContent(contentDto, userId);
    }

    /**
     * 修改保养条目
     * @param contentDto 修改内容
     * @param userId 用户Id
     * @return ResultVo
     */
    @PutMapping("/contents")
    public ResultVo alterContent(@Valid @RequestBody MaintenanceContentDto contentDto, @RequestHeader(defaultValue = "-1") Long userId) {
        //检验保养条目和保养类型是否存在关联关系
        Boolean hasRelevancy = typeHasContentService.hasRelevancy(contentDto.getContentId());
        if (hasRelevancy) {
            return new ResultVo(ResultEnum.ERROR,"该保养条目存在与保养类型的关联关系，修改失败！");
        }
        //修改保养条目
        Boolean alterContent = maintenanceContentService.alterContent(contentDto, userId);
        if (alterContent) {
            return new ResultVo(ResultEnum.OK);
        }else {
            return new ResultVo(ResultEnum.ERROR);
        }
    }

    /**
     * 删除指定保养条目
     * @param contentId 保养内容Id
     * @return ResultVo
     */
    @DeleteMapping("/contents")
    public ResultVo deleteContent(@RequestParam("contentId") Long contentId) {
        maintenanceContentService.deleteContent(contentId);
        return new ResultVo<>(ResultEnum.OK);
    }

}
