package com.i2dsp.maintenance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.i2dsp.maintenance.domain.TypeHasContent;
import com.i2dsp.maintenance.mapper.TypeHasContentMapper;
import com.i2dsp.maintenance.service.ITypeHasContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 保养类型和保养内容的关系管理中间表 服务实现类
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
@Service
public class TypeHasContentServiceImpl extends ServiceImpl<TypeHasContentMapper, TypeHasContent> implements ITypeHasContentService {

    /**
     * 判断是否存在类型和条目的关联关系
     * @param contentId
     * @return
     */
    @Override
    public Boolean hasRelevancy(Long contentId) {
        QueryWrapper<TypeHasContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id",contentId);
        int count = this.count(queryWrapper);
        if (count > 0) {
            return true;
        }else {
            return false;
        }
    }
}
