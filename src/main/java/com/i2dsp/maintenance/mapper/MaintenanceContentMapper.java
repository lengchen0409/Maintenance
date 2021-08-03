package com.i2dsp.maintenance.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.i2dsp.maintenance.domain.MaintenanceContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.i2dsp.maintenance.domain.vo.MaintenanceContentVo;

import java.util.List;

/**
 * <p>
 * 存储保养内容信息 Mapper 接口
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
public interface MaintenanceContentMapper extends BaseMapper<MaintenanceContent> {

    List<MaintenanceContent> contentList(MaintenanceContentVo maintenanceContentVo);
}
