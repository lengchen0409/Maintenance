package com.i2dsp.maintenance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.config.Enum.ScopeEnum;
import com.i2dsp.maintenance.config.exception.GlobalException;
import com.i2dsp.maintenance.domain.MaintenanceType;
import com.i2dsp.maintenance.domain.ProductHasType;
import com.i2dsp.maintenance.domain.TypeHasContent;
import com.i2dsp.maintenance.domain.dto.MaintenanceTypeDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceTypeDetailVo;
import com.i2dsp.maintenance.domain.vo.MaintenanceTypeVo;
import com.i2dsp.maintenance.mapper.MaintenanceTypeMapper;
import com.i2dsp.maintenance.service.IMaintenanceTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.i2dsp.maintenance.utils.ResultVo;
import com.i2dsp.maintenance.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 存储保养类型信息 服务实现类
 * </p>
 *
 * @author 林隆星
 * @since 2021-06-28
 */
@Service
public class MaintenanceTypeServiceImpl extends ServiceImpl<MaintenanceTypeMapper, MaintenanceType> implements IMaintenanceTypeService {

    @Autowired
    private MaintenanceTypeMapper maintenanceTypeMapper;

    @Autowired
    private TypeHasContentServiceImpl typeHasContentService;

    @Autowired
    private ProductHasTypeServiceImpl productHasContentService;

    @Autowired
    MaintenanceContentServiceImpl maintenanceContentService;

    /**
     * 查询保养类型
     *
     * @param maintenanceTypeVo
     * @param userId
     * @return
     */
    @Override
    public ResultVo getType(MaintenanceTypeVo maintenanceTypeVo, Long userId) {
        //判断是否分页查询
        if (maintenanceTypeVo.getPageNum() != null && maintenanceTypeVo.getPageSize() != null) {
            //开启分页
            PageHelper.startPage(maintenanceTypeVo.getPageNum(), maintenanceTypeVo.getPageSize());
        }
        return new ResultVo<>(new PageInfo<>(maintenanceTypeMapper.queryTypesAndProducts(maintenanceTypeVo)));
    }

    /**
     * 新增保养类型
     *
     * @param maintenanceTypeDto
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public ResultVo insertType(MaintenanceTypeDto maintenanceTypeDto, Long userId) {
        //参数校验
        if (!ScopeEnum.isScopeEnum(maintenanceTypeDto.getPeriodScope())) {
            throw new GlobalException("periodScope不合理，周期时间单位应为之一（y：年、m：月、w：周、d:日）");
        }

        MaintenanceType maintenanceType = new MaintenanceType(maintenanceTypeDto, userId);
        //计算保养周期时间戳
        maintenanceType.findPeriod();
        //保存保养类型
        boolean save = maintenanceTypeMapper.saveOne(maintenanceType);
        //保存保养内容
        if (!maintenanceTypeDto.getContents().isEmpty()) {
            //检验保养条目Id的有效性
            Boolean checkResult = maintenanceContentService.checkContentIdList(maintenanceTypeDto.getContents());
            if (!checkResult) {
                throw new GlobalException("存在无效保养条目Id");
            }
            //封装保养类型和保养内容关系
            List<TypeHasContent> typeHasContents = new ArrayList<>();
            maintenanceTypeDto.getContents().forEach(contentId ->
                    typeHasContents.add(new TypeHasContent(maintenanceType.getTypeId(), contentId))
            );
            save = typeHasContentService.saveOrUpdateBatch(typeHasContents);
        }
        //响应
        if (save) {
            return new ResultVo<>(ResultEnum.OK, "保养类型添加成功");
        } else {
            throw new GlobalException("保养类型添加失败");
        }
    }

    /**
     * 根据id查询保养类型详情
     *
     * @param typeId
     * @return
     */
    @Override
    public MaintenanceTypeDetailVo getTypeById(Long typeId) {
        return maintenanceTypeMapper.selectTypeById(typeId);
    }

    /**
     * 根据typeId查询
     *
     * @param typeId
     * @return
     */
    @Override
    public MaintenanceType getTypeByTypeId(Long typeId) {
        QueryWrapper<MaintenanceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId);
        return this.getOne(queryWrapper);
    }

    /**
     * 根据保养类型id集合获得保养类型集合
     *
     * @param typeIdList
     * @return
     */
    @Override
    public List<MaintenanceType> getTypeByTypeIdList(List<Long> typeIdList) {
        return maintenanceTypeMapper.selectBatchIds(typeIdList);
    }

    /**
     * 修改保养类型
     *
     * @param maintenanceTypeDto
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public boolean alterType(MaintenanceTypeDto maintenanceTypeDto, Long userId) {
        //参数校验
        if (!ScopeEnum.isScopeEnum(maintenanceTypeDto.getPeriodScope())) {
            throw new GlobalException("periodScope不合理，周期时间单位应为之一（y：年、m：月、w：周、d:日）");
        }

        MaintenanceType maintenanceType = new MaintenanceType(maintenanceTypeDto, userId);
        maintenanceType.setTypeId(maintenanceTypeDto.getTypeId());
        //修改保养周期时间戳
        maintenanceType.findPeriod();

        //保存保养类型
        int update = maintenanceTypeMapper.updateById(maintenanceType);
        if (update == 0) {
            return false;
        }

        //删除保养类型和保养内容关联关系
        QueryWrapper<TypeHasContent> typeHasContentQueryWrapper = new QueryWrapper<>();
        typeHasContentQueryWrapper.eq("type_id", maintenanceTypeDto.getTypeId());
        typeHasContentService.remove(typeHasContentQueryWrapper);

        //保存保养内容
        if (!maintenanceTypeDto.getContents().isEmpty()) {
            //检验保养条目Id的有效性
            Boolean checkResult = maintenanceContentService.checkContentIdList(maintenanceTypeDto.getContents());
            if (!checkResult) {
                throw new GlobalException("存在无效保养条目Id");
            }
            //封装保养类型和保养内容关系
            List<TypeHasContent> typeHasContents = new ArrayList<>();
            maintenanceTypeDto.getContents().forEach(contentId ->
                    typeHasContents.add(new TypeHasContent(maintenanceType.getTypeId(), contentId))
            );
            boolean save = typeHasContentService.saveBatch(typeHasContents);
            return save;
        }
        return true;
    }

    /**
     * 删除保养类型
     *
     * @param typeId
     * @return
     */
    @Override
    @Transactional
    public boolean deleteType(Long typeId) {
        //根据typeId获取记录
        MaintenanceType maintenanceType = this.getById(typeId);

        //查询关联关系
        QueryWrapper<ProductHasType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId);
        List<ProductHasType> productHasTypeList = productHasContentService.list(queryWrapper);

        //判断记录是否存在/记录是否已被删除/是否存在产品-保养类型关联关系
        if (maintenanceType == null) {
            throw new GlobalException(ResultEnum.BATCHING_ERROR, "记录不存在");
        }
        if (Boolean.TRUE.equals(maintenanceType.getIsDeleted())) {
            throw new GlobalException(ResultEnum.BATCHING_ERROR, "该记录已被删除，无法重复删除");
        }
        if (!productHasTypeList.isEmpty()) {
            throw new GlobalException(ResultEnum.BATCHING_ERROR, "存在关联关系");
        }
        //逻辑删除
        maintenanceType.setIsDeleted(true);
        maintenanceType.setGmtModified(TimestampUtils.getCurrentTimestamp());
        this.updateById(maintenanceType);
        return true;
    }

    /**
     * 根据产品名查询保养类型
     *
     * @param productName 产品名
     * @return
     */
    @Override
    public List<MaintenanceType> getTypeByProductName(String productName) {
        return maintenanceTypeMapper.getTypeByProductName(productName);
    }


}
