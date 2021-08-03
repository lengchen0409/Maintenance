package com.i2dsp.maintenance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.config.exception.GlobalException;
import com.i2dsp.maintenance.domain.MaintenanceContent;
import com.i2dsp.maintenance.domain.TypeHasContent;
import com.i2dsp.maintenance.domain.dto.MaintenanceContentDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceContentVo;
import com.i2dsp.maintenance.mapper.MaintenanceContentMapper;
import com.i2dsp.maintenance.service.IMaintenanceContentService;
import com.i2dsp.maintenance.utils.RequestListExResult;
import com.i2dsp.maintenance.utils.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 存储保养内容信息 服务实现类
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
@Service
public class MaintenanceContentServiceImpl extends ServiceImpl<MaintenanceContentMapper, MaintenanceContent> implements IMaintenanceContentService {

    @Autowired
    private TypeHasContentServiceImpl typeHasContentService;

    @Autowired
    private MaintenanceContentMapper maintenanceContentMapper;

    /**
     * 分页查询多条件查询Content
     * @param maintenanceContentVo 保养内容名
     * @return 查询结果
     */
    @Override
    public ResultVo getContent(MaintenanceContentVo maintenanceContentVo, Long userId) {
        //判断是否分页查询
        if (maintenanceContentVo.getPageNum() != null && maintenanceContentVo.getPageSize() != null) {
            //开启分页
            PageHelper.startPage(maintenanceContentVo.getPageNum(), maintenanceContentVo.getPageSize());
        }
        return new ResultVo<>(new PageInfo<>(maintenanceContentMapper.contentList(maintenanceContentVo)));
    }

    /**
     * 添加保养信息
     * @param contentDtos 请求信息
     * @param userId 创建人Id
     * @return 是否添加成功
     */
    @Override
    public ResultVo insertContent(List<MaintenanceContentDto> contentDtos, Long userId) {
        //新建批量处理结果对象
        RequestListExResult<MaintenanceContentDto> result = new RequestListExResult<>();
        //记录批处理总条数
        result.setTotal(contentDtos.size());
        //创建批处理异常请求信息对象
        List<MaintenanceContentDto> errorContentDtos = new ArrayList<>();
        for (MaintenanceContentDto contentDto : contentDtos) {
            // 查询保养内容相同的记录
            QueryWrapper<MaintenanceContent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("content_name", contentDto.getContentName());
            queryWrapper.eq("is_deleted", 0);
            List<MaintenanceContent> contentList = this.list(queryWrapper);
            //判断是否有相同的保养内容的记录
            if (!contentList.isEmpty()) {
                //异常请求数加1
                result.addError();
                //保存异常请求信息
                errorContentDtos.add(contentDto);
                continue;
            }
            //添加保养内容
            this.save(new MaintenanceContent(contentDto, userId));
            result.addSuccess();
        }
        //批处理是否存在异常
        if (result.getError() > 0) {
            //赋值异常请求信息
            result.setErrorRequestList(errorContentDtos);
            //响应批处理异常信息
            return new ResultVo<>(ResultEnum.OK.getCode(), "部分请求成功", result);
        }else {
            //处理完成
            return new ResultVo<>(ResultEnum.OK);
        }
    }

    /**
     * 删除保养内容
     * @param contentIds 保养内容Ids
     * @return 是否删除成功
     */
    @Override
    public boolean deleteContent(Long contentId) {
        //根据contentId获取记录
        MaintenanceContent content = this.getById(contentId);

        //查询关联关系
        QueryWrapper<TypeHasContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id",contentId);
        List<TypeHasContent> typeHasContentList = typeHasContentService.list(queryWrapper);

        //判断记录是否存在/记录是否已被删除/是否存在保养类型-保养内容关联关系
        if (content == null) {
            throw new GlobalException(ResultEnum.BATCHING_ERROR,"记录不存在");
        }
        if ( content.getIsDeleted() == 1) {
            throw new GlobalException(ResultEnum.BATCHING_ERROR,"该记录已被删除，无法重复删除");
        }
        if (!typeHasContentList.isEmpty()) {
            throw new GlobalException(ResultEnum.BATCHING_ERROR,"存在关联关系");
        }

        //逻辑删除
        content.setIsDeleted(1);
        content.setGmtModified(String.valueOf(System.currentTimeMillis()));
        this.updateById(content);
        return true;
    }

    /**
     * 修改保养条目
     * @param contentDto
     * @param userId
     * @return
     */
    @Override
    public Boolean alterContent(MaintenanceContentDto contentDto, Long userId) {
        MaintenanceContent content = this.getById(contentDto.getContentId());
        if (content != null) {
            BeanUtils.copyProperties(contentDto,content);
            boolean updateStatus = this.updateById(content);
            return updateStatus;
        }else {
            throw new GlobalException("该保养条目不存在");
        }
    }

    /**
     * 检验保养条目的有效性
     * @param contents
     * @return
     */
    @Override
    public Boolean checkContentIdList(List<Long> contents) {
        List<MaintenanceContent> contentList = this.listByIds(contents);
        if (contents.size() == contentList.size()) {
            return true;
        }else {
            return false;
        }
    }

}
