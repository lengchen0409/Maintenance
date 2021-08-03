package com.i2dsp.maintenance.service;

import com.i2dsp.maintenance.domain.TypeHasContent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 保养类型和保养内容的关系管理中间表 服务类
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
public interface ITypeHasContentService extends IService<TypeHasContent> {

    /**
     * 判断是否存在类型和条目的关联关系
     * @param contentId
     * @return
     */
    Boolean hasRelevancy(Long contentId);
}
