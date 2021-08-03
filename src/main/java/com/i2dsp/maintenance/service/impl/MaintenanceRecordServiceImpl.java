package com.i2dsp.maintenance.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.config.exception.GlobalException;
import com.i2dsp.maintenance.domain.MaintenanceContent;
import com.i2dsp.maintenance.domain.MaintenanceLastRecord;
import com.i2dsp.maintenance.domain.MaintenanceRecord;
import com.i2dsp.maintenance.domain.MaintenanceRecordDetail;
import com.i2dsp.maintenance.domain.dto.DeviceDto;
import com.i2dsp.maintenance.domain.dto.MaintenanceRecordDto;
import com.i2dsp.maintenance.domain.dto.MaintenanceRecords;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndDetailAndPhotoVo;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndTypeVo;
import com.i2dsp.maintenance.mapper.MaintenanceContentMapper;
import com.i2dsp.maintenance.mapper.MaintenanceRecordMapper;
import com.i2dsp.maintenance.openfeign.I2dspEmgData;
import com.i2dsp.maintenance.service.IMaintenanceRecordService;
import com.i2dsp.maintenance.utils.MultipartFileUtils;
import com.i2dsp.maintenance.utils.RecoderPrintBean;
import com.i2dsp.maintenance.utils.ResultVo;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 * 存储保养记录的基本信息 服务实现类
 * </p>
 *
 * @author 梁海聪
 * @since 2021-06-28
 */
@Service
public class MaintenanceRecordServiceImpl extends ServiceImpl<MaintenanceRecordMapper, MaintenanceRecord> implements IMaintenanceRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceRecordServiceImpl.class);

    @Autowired
    private MaintenanceRecordMapper maintenanceRecordMapper;
    @Autowired
    private MaintenanceContentMapper maintenanceContentMapper;
    @Autowired
    private MaintenanceRecordDetailServiceImpl maintenanceRecordDetailService;
    @Autowired
    private MultipartFileUtils multipartFileUtils;
    @Autowired
    private MaintenancePhotoServiceImpl maintenancePhotoService;
    @Autowired
    private MaintenanceLastRecordServiceImpl maintenanceLastRecordService;
    @Autowired
    private I2dspEmgData i2dspEmgData;


    /**
     * 填写保养记录信息
     * @param userId
     * @param maintenanceRecords
     * @param fileImages
     * @param fileNumber
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Integer> fillMaintenanceRecord(Long userId, MaintenanceRecords maintenanceRecords, MultipartFile[] fileImages, Integer[] fileNumber) {
        //保养记录对象
        MaintenanceRecord maintenanceRecord = new MaintenanceRecord(maintenanceRecords);
        //保养最新记录对象
        MaintenanceLastRecord maintenanceLastRecord = new MaintenanceLastRecord(maintenanceRecords);
        //保养内容id数组
        String[] contentIds = maintenanceRecords.getContentId();
        //检查结果数组
        Integer[] checkStatus = maintenanceRecords.getCheckStatus();
        //保养详情备注
        String remark = maintenanceRecords.getRemarks();
        //将字符串分割成数组
        String[] remarks = remark.split("#");

        Long recordId = null;
        Long recordDetailId = null;
        int k = 0;
        try{
            //保存保养记录，返回recordId
            recordId = saveMaintenanceRecord(maintenanceRecord
                            .setCreateUser(userId)
                            .setGmtCreate(String.valueOf(System.currentTimeMillis())));
            QueryWrapper<MaintenanceLastRecord> lastestRecordQueryWrapper = new QueryWrapper<>();
            lastestRecordQueryWrapper.eq("device_id", maintenanceRecords.getDeviceId()).eq("type_id", maintenanceRecords.getTypeId());
            maintenanceLastRecord.setLastRecordTime(maintenanceRecord.getGmtCreate());
            maintenanceLastRecordService.saveOrUpdate(maintenanceLastRecord, lastestRecordQueryWrapper);
            for (int i = 0; i < contentIds.length; i++) {
                //根据保养内容id获取保养内容
                MaintenanceContent maintenanceContent = maintenanceContentMapper.selectById(Long.valueOf(contentIds[i]));
                //参数校验
                String s = checkContents(maintenanceContent, checkStatus[i], remarks[i], fileNumber[i]);
                if (!s.equals("true")) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return new ResultVo<>(ResultEnum.ERROR, s);
                }
                //保存保养详情信息
                recordDetailId = maintenanceRecordDetailService.saveMaintenanceRecordDetail(new MaintenanceRecordDetail()
                                                .setRecordId(recordId)
                                                .setContentId(Long.valueOf(contentIds[i]))
                                                .setCheckStatus(checkStatus[i])
                                                .setRemark(remarks[i]));
                //创建图片数组
                MultipartFile[] fileImage = new MultipartFile[fileNumber[i]];
                for (int j = 0; j < fileNumber[i]; j++) {
                    fileImage[j] = fileImages[k];
                    k++;
                }
                try {
                    List<String> fileNames = new ArrayList<>();
                    //图片统一上传，返回图片名称集合
                    fileNames = multipartFileUtils.handleFile(fileImage, userId, String.valueOf(recordDetailId));
                    //保存保养图片
                    maintenancePhotoService.saveMaintenancePhoto(String.valueOf(recordDetailId), fileNames);
                    //图片异常处理
                }catch (Exception e) {
                    e.printStackTrace();
                    logger.error("********************ERROR 图片保存异常****************************");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return new ResultVo<>(20400,"图片保存异常",0);
                }
            }
            return new ResultVo<>(20000,"添加成功",1);
        }catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultVo<>(20400,"添加失败",0);
        }
    }


    /**
     * 保存保养记录
     * @param maintenanceRecord
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Long saveMaintenanceRecord(MaintenanceRecord maintenanceRecord) throws Exception {
        //生成一个随机的long类型uuid作为recordId
        Long recordId = Math.abs(UUID.randomUUID().getMostSignificantBits());
        maintenanceRecord.setRecordId(recordId);
        //保存保养记录后返回recordId
        if (save(maintenanceRecord)) {
            return recordId;
        }
        throw new Exception("保养记录填写失败");
    }

    /**
     * 查询保养记录详情
     * @param maintenanceRecordDto
     * @param userId
     * @return
     */
    @Override
    public List<MaintenanceRecordAndDetailAndPhotoVo> searchMaintenancesByAll(Long userId, MaintenanceRecordDto maintenanceRecordDto) {
        //存储符合条件的保养记录详情
        List<MaintenanceRecordAndDetailAndPhotoVo> recordAndDetailAndPhotoVoList = new ArrayList<>();
        //存储设备和保养记录详情的map集合
        Map<String, List<MaintenanceRecordAndDetailAndPhotoVo>> map = new HashMap<>();
        JSONArray devices = new JSONArray();
        //条件查询所有保养记录
        List<MaintenanceRecordAndDetailAndPhotoVo> recordAndDetailAndPhotoVos = maintenanceRecordMapper.searchMaintenancesByAll(maintenanceRecordDto);
        //遍历所有条件查询的记录
        for (MaintenanceRecordAndDetailAndPhotoVo recordAndDetailAndPhotoVo : recordAndDetailAndPhotoVos) {
            //判断该记录的设备是否已存在map中
            if (map.containsKey(recordAndDetailAndPhotoVo.getDeviceId())) {
                //如果存在就把该保养记录详情对象放入该key下的list集合中
                List<MaintenanceRecordAndDetailAndPhotoVo> vos = map.get(recordAndDetailAndPhotoVo.getDeviceId());
                vos.add(recordAndDetailAndPhotoVo);
                //重新放入map中
                map.put(recordAndDetailAndPhotoVo.getDeviceId(), vos);
            }else {
                //不存在key值就新增
                List<MaintenanceRecordAndDetailAndPhotoVo> vos = new ArrayList<>();
                vos.add(recordAndDetailAndPhotoVo);
                map.put(recordAndDetailAndPhotoVo.getDeviceId(), vos);
            }
        }
        //把map的key值放入集合list中存储
        List<String> mapKeyList = new ArrayList<>(map.keySet());
        //判断集合是否为空来判断该设备是否存在保养记录
        if (mapKeyList.size() == 0) {
            throw new GlobalException(ResultEnum.OK, "没有该信息的保养记录");
        }
        //新建一个设备信息对象,把设备编号列表放入集合中
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDeviceIdArr(mapKeyList);
        //调用查询该用户下的设备服务接口
        try {
            JSONObject response = i2dspEmgData.getDeviceSimple(userId, deviceDto, 1, 0);
            if (response.getInteger("code") == 20000) {
                JSONObject data = response.getJSONObject("data");
                //返回设备列表
                devices = data.getJSONArray("list");
                for (int i = 0; i < devices.size(); i++) {
                    JSONObject dev = devices.getJSONObject(i);
                    //如果属于，把该保养记录存在list集合中
                    recordAndDetailAndPhotoVoList.addAll(map.get(dev.getString("deviceId")));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultEnum.OPENFEIGN_ERROR);
        }
        return recordAndDetailAndPhotoVoList;
    }

    /**
     * 根据设备查询记录和保养类型
     * @param deviceId
     * @return
     */
    @Override
    public List<MaintenanceRecordAndTypeVo> searchRecordAndTypeByDeviceId(String deviceId) {
        return maintenanceRecordMapper.searchRecordAndTypeByDeviceId(deviceId);
    }

    /**
     * 根据保养类型id查询保养记录和保养类型
     * @param typeId
     * @return
     */
    @Override
    public List<MaintenanceRecordAndTypeVo> searchRecordAndTypeByTypeId(Long typeId, String staffName) {
        return maintenanceRecordMapper.searchRecordAndTypeByTypeId(typeId, staffName);
    }

    /**
     * 参数校验
     * @param maintenanceContent
     * @param checkStatus
     * @param remark
     * @param fileNumber
     * @return
     */
    public String checkContents(MaintenanceContent maintenanceContent, Integer checkStatus, String remark, Integer fileNumber) {
        if (maintenanceContent.getIsJudge() == 0 && checkStatus != 0) {
            return maintenanceContent.getContentName() +"这条保养内容不需要填写状态判断";
        }else if (maintenanceContent.getIsJudge() == 1 && checkStatus == 0) {
            return maintenanceContent.getContentName() +"这条保养内容需要填写状态判断";
        }else if (maintenanceContent.getIsRemark() == 1 && remark.isEmpty()) {
            return maintenanceContent.getContentName() +"这条保养内容必须填写备注";
        }else if (maintenanceContent.getIsUpload() == 0 && fileNumber != 0) {
            return maintenanceContent.getContentName() +"这条保养内容不需要图片上传";
        }else if (maintenanceContent.getIsUpload() == 1 && fileNumber == 0) {
            return maintenanceContent.getContentName() +"这条保养内容需要图片上传";
        }
        return "true";
    }

    /**
     * 获取设备保养记录excel文件
     * @param maintenanceRecordDto
     * @param userId
     * @param response
     * @author 林隆星
     */
    @Override
    public void getExcelFile(Long userId, MaintenanceRecordDto maintenanceRecordDto, HttpServletResponse response) {
        //获取保养记录信息
        List<MaintenanceRecordAndDetailAndPhotoVo> records = this.searchMaintenancesByAll(userId, maintenanceRecordDto);
        //封装生成excel文件内容
        List<RecoderPrintBean> recoderPrintBeans = new ArrayList<>();
        //excel文件输出配置
        ExcelWriter writer = null;

        try {
            //记录信息处理
            for (int i = 0; i < records.size(); i++) {
                //转换为RecoderPrintBean
                MaintenanceRecordAndDetailAndPhotoVo record = records.get(i);
                RecoderPrintBean onedBean = RecoderPrintBean.getOne(record);
                //序号
                onedBean.setNumber(String.valueOf(i+1));
                //创建人信息
                JSONObject userInfo = i2dspEmgData.getUserInfo(record.getCreateUser());
                if (userInfo.getInteger("code") == 20000 ) {
                    onedBean.setCreateUser(userInfo.getJSONObject("data").getString("nickname"));
                }
                recoderPrintBeans.add(onedBean);
            }
            // 通过工具类创建writer
            writer = ExcelUtil.getWriter(true);
            //左对齐
//            writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
            //自定义标题别名，自定义列宽
            writer.addHeaderAlias("number", "序号");
            writer.setColumnWidth(0,5);
            writer.addHeaderAlias("gmtCreate", "保养记录时间");
            writer.setColumnWidth(1,16);
            writer.addHeaderAlias("deviceId", "设备ID");
            writer.setColumnWidth(2,35);
            writer.addHeaderAlias("typeName", "保养类型");
            writer.setColumnWidth(3,17);
            writer.addHeaderAlias("staffName", "保养人");
            writer.setColumnWidth(4,7);
            writer.addHeaderAlias("staffPhone", "联系方式");
            writer.setColumnWidth(5,13);
            writer.addHeaderAlias("createUser", "记录填写人");
            writer.setColumnWidth(6,10);
            writer.addHeaderAlias("isAbnormal", "是否异常");
            writer.setColumnWidth(7,10);
            writer.addHeaderAlias("remark", "备注");
            writer.setColumnWidth(8,20);
            //文件名
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String content = format.format(date) + " 设备保养记录表.xlsx";

            String explain = "说明：本文件简要展示设备的保养情况，详细的保养记录请在集成智慧服务生态平台中查询。";
            StyleSet style = writer.getStyleSet();
            CellStyle cellStyle = style.getCellStyle();
            //水平居左
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setWrapText(true);

            //文件返回配置
            response.setContentType("multipart/form-data");
            //为下载文件设置名字
            response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(content.getBytes("UTF-8"), "ISO8859-1") + "\"");
            //获取响应的输出流
            ServletOutputStream out = response.getOutputStream();
            // 合并单元格后的标题行，使用默认标题样式
            writer.merge(8, content);
            writer.merge(8, explain, false);
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(recoderPrintBeans, true);
            //out为OutputStream，需要写出到的目标流
            writer.flush(out);
        }catch (Exception e) {
            throw new GlobalException(e,"Excel文件生成失败");
        } finally {
            // 关闭writer，释放内存
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 检测是否存在该保养类型的保养记录
     * @param typeId
     * @return
     */
    @Override
    public Boolean checkRecord(Long typeId) {
        QueryWrapper<MaintenanceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id",typeId);
        int count = this.count(queryWrapper);
        if (count > 0) {
            return true;
        }else {
            return false;
        }
    }
}
