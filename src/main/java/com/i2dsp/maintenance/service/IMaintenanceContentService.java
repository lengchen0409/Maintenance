package com.i2dsp.maintenance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.i2dsp.maintenance.domain.MaintenanceContent;
import com.i2dsp.maintenance.domain.dto.MaintenanceContentDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceContentVo;
import com.i2dsp.maintenance.utils.ResultVo;

import java.util.List;

/**
 * <p>
 * 存储保养内容信息 服务类
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
public interface IMaintenanceContentService extends IService<MaintenanceContent> {

    /**
     * 分页查询多条件查询Content
     * @param maintenanceContentVo 请求信息
     * @param userId
     * @return
     */
    ResultVo getContent(MaintenanceContentVo maintenanceContentVo, Long userId);

    /**
     * 添加保养信息
     * @param contentDto 请求信息
     * @param userId 创建人Id
     * @return
     */
    ResultVo insertContent(List<MaintenanceContentDto> contentDto, Long userId);

    /**
     *删除保养内容
     * @param contentId
     * @return
     */
    boolean deleteContent(Long contentIds);

    /**
     * 修改保养条目
     * @param contentDto
     * @param userId
     * @return
     */
    Boolean alterContent(MaintenanceContentDto contentDto, Long userId);

    /**
     * 检验保养条目的有效性
     * @param contents
     * @return
     */
    Boolean checkContentIdList(List<Long> contents);
}
