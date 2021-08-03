package com.i2dsp.maintenance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.config.exception.GlobalException;
import com.i2dsp.maintenance.domain.Device;
import com.i2dsp.maintenance.domain.MaintenanceType;
import com.i2dsp.maintenance.domain.ProductHasType;
import com.i2dsp.maintenance.domain.dto.DeviceDto;
import com.i2dsp.maintenance.domain.dto.MaintenanceCountDto;
import com.i2dsp.maintenance.domain.dto.MaintenancePendingDto;
import com.i2dsp.maintenance.domain.vo.MaintenanceProductAndTypeVo;
import com.i2dsp.maintenance.domain.vo.MaintenanceRecordAndTypeVo;
import com.i2dsp.maintenance.openfeign.I2dspEmgData;
import com.i2dsp.maintenance.utils.ResultVo;
import com.i2dsp.maintenance.utils.TimestampUtils;
import com.i2dsp.maintenance.utils.TypeMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 待保养服务实现类
 * @author : 梁海聪
 * @since : 2021/07/26
 */
@Service
public class MaintenanceRequireServiceImpl {

    @Autowired
    private ProductHasTypeServiceImpl productHasTypeService;
    @Autowired
    private I2dspEmgData i2dspEmgData;
    @Autowired
    private MaintenanceLastRecordServiceImpl maintenanceLastRecordService;
    @Autowired
    private MaintenanceRecordServiceImpl maintenanceRecordService;
    @Autowired
    private MaintenceProductServiceImpl maintenceProductService;
    @Autowired
    private MaintenanceTypeServiceImpl maintenanceTypeService;
    @Autowired
    private AsyncServiceImpl asyncService;

    //年保养周期时间戳
    static final long YEAR = 24 * 60 * 60 * 360 * 1000l;

    /**
     * 条件查询待保养设备信息
     * @param userId
     * @param maintenanceProductAndTypeVo
     * @param deviceDto
     * @param staffName
     * @return
     */
    public List<MaintenancePendingDto> searchMaintenanceNeeds(Long userId, MaintenanceProductAndTypeVo maintenanceProductAndTypeVo, DeviceDto deviceDto, String staffName) {
        //存放产品名和保养类型和产品信息的集合
        Map<String, List<MaintenanceProductAndTypeVo>> map = new HashMap<>();
        //存放待保养设备的集合
        List<MaintenancePendingDto> maintenancePendingDtoList = new ArrayList<>();
        //设备集合
        JSONArray devices = new JSONArray();
        //条件查询产品和保养类型集合
        List<MaintenanceProductAndTypeVo> productAndTypeVoList = productHasTypeService.searchProductAndType(maintenanceProductAndTypeVo);
        //遍历该集合
        for (MaintenanceProductAndTypeVo productAndTypeVo : productAndTypeVoList) {
            //判断保养类型和产品对象中该产品是否存在集合中
            if (map.containsKey(productAndTypeVo.getProductName())) {
                //存在就把该产品名下的保养类型放入集合
                List<MaintenanceProductAndTypeVo> productAndTypeList = map.get(productAndTypeVo.getProductName());
                productAndTypeList.add(productAndTypeVo);
                map.put(productAndTypeVo.getProductName(), productAndTypeList);
            }else {
                //不存在就新建
                List<MaintenanceProductAndTypeVo> productAndTypeList = new ArrayList<>();
                productAndTypeList.add(productAndTypeVo);
                map.put(productAndTypeVo.getProductName(), productAndTypeList);
            }
        }
        //生成产品名集合list
        List<String> productNames = new ArrayList<>(map.keySet());
        //创建一个记录和保养类型的对象
        MaintenanceRecordAndTypeVo recordAndTypeVos = new MaintenanceRecordAndTypeVo();
        try {
            //给该对象赋值
            recordAndTypeVos.setTypeId(maintenanceProductAndTypeVo.getTypeId()).setStaffName(staffName)
                    .setPeriodNumber(maintenanceProductAndTypeVo.getPeriodNumber())
                    .setPeriodScope(maintenanceProductAndTypeVo.getPeriodScope());
            //把产品名集合放入封装好的设备对象中
            deviceDto.setProductNames(productNames);
            //条件查询出该用户下的设备
            JSONObject deviceSimple = i2dspEmgData.getDeviceSimple(userId, deviceDto, 1, 0);
            if (deviceSimple.getInteger("code") == 20000) {
                JSONObject data = deviceSimple.getJSONObject("data");
                devices = data.getJSONArray("list");
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultEnum.OPENFEIGN_ERROR);
        }
        //返回待保养设备集合
        for (int i = 0; i < devices.size(); i++) {
            JSONObject device = devices.getJSONObject(i);
            //把该设备编号赋值给记录和保养类型对象
            recordAndTypeVos.setDeviceId(device.getString("deviceId"));
            //把该设备的产品名赋值给记录和保养类型对象
            recordAndTypeVos.setProductName(device.getString("productName"));
            //条件查询最新记录和保养类型
            List<MaintenanceRecordAndTypeVo> recordAndTypeVoList = maintenanceLastRecordService.searchLastRecordAndTypeByDevice(recordAndTypeVos);
            //遍历该集合得到最新记录和保养类型对象
            for (MaintenanceRecordAndTypeVo recordAndTypeVo : recordAndTypeVoList) {
                //新建一个待保养设备对象
                MaintenancePendingDto maintenancePendingDto = new MaintenancePendingDto();
                //判断是否存在上一次保养记录时间
                if (recordAndTypeVo.getGmtCreate() != null) {
                    //当前时间和上一次保养时间的差值
                    long time = System.currentTimeMillis() - Long.valueOf(recordAndTypeVo.getGmtCreate());
                    //判断该差值是否大于保养周期时间戳从而来判断是否待保养
                    if (time > Long.valueOf(recordAndTypeVo.getPeriod())) {
                        //该设备待保养就把该设备的信息属性赋值给待保养设备对象
                        maintenancePendingDto.setDeviceId(device.getString("deviceId"))
                                .setDeviceName(device.getString("deviceName"))
                                .setProductName(device.getString("productName"))
                                .setBuildingName(device.getString("buildingName"))
                                .setFloorName(device.getString("floorName"))
                                .setPlace(device.getString("place"))
                                .setTypeName(recordAndTypeVo.getTypeName())
                                .setLastStaffName(recordAndTypeVo.getStaffName())
                                .setLastRecordedTime(recordAndTypeVo.getGmtCreate());
                        //把该对象放入接收待保养设备的集合中
                        maintenancePendingDtoList.add(maintenancePendingDto);
                    }
                }else {
                    //用当前时间和该年的一月一日时间戳差值
                    long time = System.currentTimeMillis() - TimestampUtils.getAppointTimestamp();
                    //判断该记录和保养类型对象是中的保养类型是否属于年保养
                    if (recordAndTypeVo.getPeriodScope().equals("y")) {
                        //如果是年保养则用该差值和1年的时间戳作对比
                        if (time > YEAR) {
                            //该设备待保养就把该设备的信息属性赋值给待保养设备对象
                            maintenancePendingDto.setDeviceId(device.getString("deviceId"))
                                    .setDeviceName(device.getString("deviceName"))
                                    .setProductName(device.getString("productName"))
                                    .setBuildingName(device.getString("buildingName"))
                                    .setFloorName(device.getString("floorName"))
                                    .setPlace(device.getString("place"))
                                    .setTypeName(recordAndTypeVo.getTypeName());
                            //把该对象放入接收待保养设备的集合中
                            maintenancePendingDtoList.add(maintenancePendingDto);
                        }
                        //如果不是年保养而且该差值大于保养周期时间戳,则为待保养设备
                    }else if (!recordAndTypeVo.getPeriodScope().equals("y") && time > Long.valueOf(recordAndTypeVo.getPeriod())) {
                        //该设备待保养就把该设备的信息属性赋值给待保养设备对象
                        maintenancePendingDto.setDeviceId(device.getString("deviceId"))
                                .setDeviceName(device.getString("deviceName"))
                                .setProductName(device.getString("productName"))
                                .setBuildingName(device.getString("buildingName"))
                                .setFloorName(device.getString("floorName"))
                                .setPlace(device.getString("place"))
                                .setTypeName(recordAndTypeVo.getTypeName());
                        //把该对象放入接收待保养设备的集合中
                        maintenancePendingDtoList.add(maintenancePendingDto);
                    }
                }
            }
        }
        return maintenancePendingDtoList;
    }

    /**
     * 查询待保养设备数量
     * @param userId
     * @param productName
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<MaintenanceCountDto> searchMaintenanceCounts(Long userId, String productName) throws ExecutionException, InterruptedException {
        Future<JSONObject> responseJson = null;
        //调用线程获取设备的接口
        try {
            responseJson = asyncService.getResponse(userId, productName);
        }catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultEnum.OPENFEIGN_ERROR);
        }
        //存储待保养记录信息map集合
        Map<Long, MaintenanceCountDto> maps = new HashMap<>();
        //存储产品名和保养类型map集合
        Map<String, List<MaintenanceType>> listMap = new HashMap<>();
        //存储记录保养类型封装对象和保养类型对象的map集合
        Map<Long, MaintenanceRecordAndTypeVo> map = new HashMap<>();
        List<MaintenanceCountDto> maintenanceCountDtoList = new ArrayList<>();
        //根据用户获得该用户的产品列表
        List<String> products = maintenceProductService.getProducts(userId);
        //遍历产品类型名
        for (String product : products) {
            //通过设备的产品类型查询关联的保养类型列表
            QueryWrapper<ProductHasType> productHasTypeQueryWrapper = new QueryWrapper<>();
            productHasTypeQueryWrapper.select("type_id").eq("product_name", product);
            List<ProductHasType> productHasTypeList = productHasTypeService.list(productHasTypeQueryWrapper);
            //存储保养类型集合
            List<MaintenanceType> maintenanceTypeList = new ArrayList<>();
            //获取到该产品类型下对应的所有保养类型id的集合
            List<Long> typeIdList = productHasTypeList.stream().map(ProductHasType::getTypeId).collect(Collectors.toList());
            //根据保养类型id集合获得保养类型集合
            if (!typeIdList.isEmpty()) {
                List<MaintenanceType> typeByTypeIdList = maintenanceTypeService.getTypeByTypeIdList(typeIdList);
                //把保养类型集合添加进集合
                maintenanceTypeList.addAll(typeByTypeIdList);
            }
            //把产品名和保养类型集合放入map中
            listMap.put(product, maintenanceTypeList);
        }
        List<Future<String>> futureList = new ArrayList<>();
        //调用查询设备接口
        JSONObject response = responseJson.get();
        if (response.getInteger("code") == 20000) {
            //返回设备
            List<Device> devices = JSONObject.parseArray(response.getJSONObject("data").getJSONArray("list").toJSONString(), Device.class);
            //定义每一次进入的设备数量
            int loopSize = devices.size() / 3;
            int index = devices.size();
            for (int i = 0; i < index; i = i + loopSize) {
                if (index > i + loopSize) {
                    //获取待保养设备
                    futureList.add(asyncService.getMaintenanceCounts(maps, listMap, map, devices.subList(i, i + loopSize)));
                }else {
                    //获取待保养设备
                    futureList.add(asyncService.getMaintenanceCounts(maps, listMap, map, devices.subList(i, index)));
                }
            }
            //等待所有线程结束
            for (Future<String> future : futureList) {
                future.get();
            }
        }
        //把map集合中的value值对象放入list集合中
        maintenanceCountDtoList.addAll(maps.values());
        return maintenanceCountDtoList;
    }

    /**
     * 条件查询待保养设备2
     * @param userId
     * @param maintenanceProductAndTypeVo
     * @param deviceDto
     * @param staffName
     * @return
     */
    public List<MaintenancePendingDto> searchMaintenanceNeed(Long userId, MaintenanceProductAndTypeVo maintenanceProductAndTypeVo, DeviceDto deviceDto, String staffName) {
        //存放产品名和保养类型和产品信息的集合
        Map<String, List<MaintenanceProductAndTypeVo>> map = new HashMap<>();
        //存放待保养设备的集合
        List<MaintenancePendingDto> maintenancePendingDtoList = new ArrayList<>();
        //存放设备编号和最新保养记录和保养类型的集合
        Map<String, List<MaintenanceRecordAndTypeVo>> maps = new HashMap<>();
        //设备集合
        JSONArray devices = new JSONArray();
        //条件查询产品和保养类型集合
        List<MaintenanceProductAndTypeVo> productAndTypeVoList = productHasTypeService.searchProductAndType(maintenanceProductAndTypeVo);
        //遍历该集合
        for (MaintenanceProductAndTypeVo productAndTypeVo : productAndTypeVoList) {
            //判断保养类型和产品对象中该产品是否存在集合中
            if (map.containsKey(productAndTypeVo.getProductName())) {
                //存在就把该产品名下的保养类型放入集合
                List<MaintenanceProductAndTypeVo> productAndTypeList = map.get(productAndTypeVo.getProductName());
                productAndTypeList.add(productAndTypeVo);
                map.put(productAndTypeVo.getProductName(), productAndTypeList);
            }else {
                //不存在就新建
                List<MaintenanceProductAndTypeVo> productAndTypeList = new ArrayList<>();
                productAndTypeList.add(productAndTypeVo);
                map.put(productAndTypeVo.getProductName(), productAndTypeList);
            }
        }
        //生成产品名集合list
        List<String> productNames = new ArrayList<>(map.keySet());
        //创建一个记录和保养类型的对象
        MaintenanceRecordAndTypeVo recordAndTypeVos = new MaintenanceRecordAndTypeVo();
        try {
            //给该对象赋值
            recordAndTypeVos.setTypeId(maintenanceProductAndTypeVo.getTypeId()).setStaffName(staffName)
                    .setPeriodNumber(maintenanceProductAndTypeVo.getPeriodNumber())
                    .setPeriodScope(maintenanceProductAndTypeVo.getPeriodScope());
            //条件查询最新记录中待保养的设备的保养记录和保养类型对象集合
            List<MaintenanceRecordAndTypeVo> recordAndTypeVoList = maintenanceLastRecordService.searchLastRecordAndTypeByAll(recordAndTypeVos);
            //遍历该集合
            for (MaintenanceRecordAndTypeVo recordAndTypeVo : recordAndTypeVoList) {
                //把设备编号作为key,保养记录和保养类型对象集合作为value
                if (maps.containsKey(recordAndTypeVo.getDeviceId())) {
                    List<MaintenanceRecordAndTypeVo> vos = maps.get(recordAndTypeVo.getDeviceId());
                    vos.add(recordAndTypeVo);
                    maps.put(recordAndTypeVo.getDeviceId(), vos);
                }else {
                    List<MaintenanceRecordAndTypeVo> vos = new ArrayList<>();
                    vos.add(recordAndTypeVo);
                    maps.put(recordAndTypeVo.getDeviceId(), vos);
                }
            }
            //把产品名集合放入封装好的设备对象中
            deviceDto.setProductNames(productNames);
            //条件查询出该用户下的设备
            JSONObject deviceSimple = i2dspEmgData.getDeviceSimple(userId, deviceDto, 1, 0);
            if (deviceSimple.getInteger("code") == 20000) {
                JSONObject data = deviceSimple.getJSONObject("data");
                devices = data.getJSONArray("list");
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultEnum.OPENFEIGN_ERROR);
        }
        //返回待保养设备集合
        for (int i = 0; i < devices.size(); i++) {
            JSONObject device = devices.getJSONObject(i);
            //存放保养类型id和最新记录和保养类型对象
            Map<Long, MaintenanceRecordAndTypeVo> recordAndTypeVoMap = new HashMap<>();
            //根据该设备的产品名获得产品和保养类型对象集合
            List<MaintenanceProductAndTypeVo> productAndTypeVos = map.get(device.getString("productName"));
            //根据设备编号获得待保养的最新保养记录和保养类型集合
            List<MaintenanceRecordAndTypeVo> recordAndTypeVoList = maps.get(device.getString("deviceId"));
            //判断该设备是否存在待保养的最新保养记录和保养类型对象
            if (recordAndTypeVoList != null) {
                for (MaintenanceRecordAndTypeVo recordAndTypeVo : recordAndTypeVoList) {
                    //存放该设备下保养类型为key的待保养的最新保养记录和保养类型对象
                    recordAndTypeVoMap.put(recordAndTypeVo.getTypeId(), recordAndTypeVo);
                }
            }
            //遍历产品和保养类型集合
            for (MaintenanceProductAndTypeVo productAndTypeVo : productAndTypeVos) {
                //新建一个待保养设备对象
                MaintenancePendingDto maintenancePendingDto = new MaintenancePendingDto();
                //判断该设备的产品关联的保养类型是否存在待保养的最新保养记录
                if (recordAndTypeVoMap.containsKey(productAndTypeVo.getTypeId())) {
                    //如果存在获得该保养记录和保养类型对象
                    MaintenanceRecordAndTypeVo recordAndTypeVo = recordAndTypeVoMap.get(productAndTypeVo.getTypeId());
                    //该设备待保养就把该设备的信息属性赋值给待保养设备对象
                    maintenancePendingDto.setDeviceId(device.getString("deviceId"))
                            .setDeviceName(device.getString("deviceName"))
                            .setProductName(device.getString("productName"))
                            .setBuildingName(device.getString("buildingName"))
                            .setFloorName(device.getString("floorName"))
                            .setPlace(device.getString("place"))
                            .setTypeName(recordAndTypeVo.getTypeName())
                            .setLastStaffName(recordAndTypeVo.getStaffName())
                            .setLastRecordedTime(recordAndTypeVo.getGmtCreate());
                    //把该对象放入接收待保养设备的集合中
                    maintenancePendingDtoList.add(maintenancePendingDto);
                    //如果不存在记录而且上一次保养人为空
                } else if (staffName == null){
                    //用当前时间和该年的一月一日时间戳差值
                    long time = System.currentTimeMillis() - TimestampUtils.getAppointTimestamp();
                    //判断该产品和保养类型对象是中的保养类型是否属于年保养
                    if (productAndTypeVo.getPeriodScope().equals("y")) {
                        //如果是年保养则用该差值和1年的时间戳作对比
                        if (time > YEAR) {
                            //该设备待保养就把该设备的信息属性赋值给待保养设备对象
                            maintenancePendingDto.setDeviceId(device.getString("deviceId"))
                                    .setDeviceName(device.getString("deviceName"))
                                    .setProductName(device.getString("productName"))
                                    .setBuildingName(device.getString("buildingName"))
                                    .setFloorName(device.getString("floorName"))
                                    .setPlace(device.getString("place"))
                                    .setTypeName(productAndTypeVo.getTypeName());
                            //把该对象放入接收待保养设备的集合中
                            maintenancePendingDtoList.add(maintenancePendingDto);
                        }
                        //如果不是年保养而且该差值大于保养周期时间戳,则为待保养设备
                    }else if (!productAndTypeVo.getPeriodScope().equals("y") && time > Long.valueOf(productAndTypeVo.getPeriod())) {
                        //该设备待保养就把该设备的信息属性赋值给待保养设备对象
                        maintenancePendingDto.setDeviceId(device.getString("deviceId"))
                                .setDeviceName(device.getString("deviceName"))
                                .setProductName(device.getString("productName"))
                                .setBuildingName(device.getString("buildingName"))
                                .setFloorName(device.getString("floorName"))
                                .setPlace(device.getString("place"))
                                .setTypeName(productAndTypeVo.getTypeName());
                        //把该对象放入接收待保养设备的集合中
                        maintenancePendingDtoList.add(maintenancePendingDto);
                    }
                }
            }
        }
        return maintenancePendingDtoList;
    }

    /**
     * 查询待保养设备数量2
     * @param userId
     * @param maintenanceProductAndTypeVo
     * @param deviceDto
     * @param staffName
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<MaintenanceCountDto> searchMaintenanceCount(Long userId, MaintenanceProductAndTypeVo maintenanceProductAndTypeVo, DeviceDto deviceDto, String staffName) throws ExecutionException, InterruptedException {
        Future<JSONObject> responseJson = null;
        //调用线程获取设备的接口
        try {
            responseJson = asyncService.getResponses(userId, deviceDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultEnum.OPENFEIGN_ERROR);
        }
        JSONArray devices = new JSONArray();
        //存储待保养记录信息map集合
        Map<Long, MaintenanceCountDto> maps = new HashMap<>();
        //存放产品名作为key,产品名和保养类型对象作为value
        Map<String, List<MaintenanceProductAndTypeVo>> productAndTypeVoMap = new HashMap<>();
        //存储设备编号为key,最新保养记录和保养类型对象为value的map集合
        Map<String, List<MaintenanceRecordAndTypeVo>> listMap = new HashMap<>();
        //待保养设备数量对象集合
        List<MaintenanceCountDto> maintenanceCountDtoList = new ArrayList<>();
        //条件查询产品和保养类型集合
        List<MaintenanceProductAndTypeVo> productAndTypeVoList = productHasTypeService.searchProductAndType(maintenanceProductAndTypeVo);
        //遍历该集合
        for (MaintenanceProductAndTypeVo productAndTypeVo : productAndTypeVoList) {
            //判断保养类型和产品对象中该产品是否存在集合中
            if (productAndTypeVoMap.containsKey(productAndTypeVo.getProductName())) {
                //存在就把该产品名下的保养类型放入集合
                List<MaintenanceProductAndTypeVo> productAndTypeList = productAndTypeVoMap.get(productAndTypeVo.getProductName());
                productAndTypeList.add(productAndTypeVo);
                productAndTypeVoMap.put(productAndTypeVo.getProductName(), productAndTypeList);
            } else {
                //不存在就新建
                List<MaintenanceProductAndTypeVo> productAndTypeList = new ArrayList<>();
                productAndTypeList.add(productAndTypeVo);
                productAndTypeVoMap.put(productAndTypeVo.getProductName(), productAndTypeList);
            }
        }
        //创建一个记录和保养类型的对象
        MaintenanceRecordAndTypeVo recordAndTypeVos = new MaintenanceRecordAndTypeVo();
        //给该对象赋值
        recordAndTypeVos.setTypeId(maintenanceProductAndTypeVo.getTypeId()).setStaffName(staffName)
                .setPeriodNumber(maintenanceProductAndTypeVo.getPeriodNumber())
                .setPeriodScope(maintenanceProductAndTypeVo.getPeriodScope());
        //条件查询最新记录中待保养的设备的保养记录和保养类型对象集合
        List<MaintenanceRecordAndTypeVo> recordAndTypeVoList = maintenanceLastRecordService.searchLastRecordAndTypeByAll(recordAndTypeVos);
        //遍历该集合
        for (MaintenanceRecordAndTypeVo recordAndTypeVo : recordAndTypeVoList) {
            //把设备编号作为key,保养记录和保养类型对象集合作为value
            if (listMap.containsKey(recordAndTypeVo.getDeviceId())) {
                List<MaintenanceRecordAndTypeVo> vos = listMap.get(recordAndTypeVo.getDeviceId());
                vos.add(recordAndTypeVo);
                listMap.put(recordAndTypeVo.getDeviceId(), vos);
            } else {
                List<MaintenanceRecordAndTypeVo> vos = new ArrayList<>();
                vos.add(recordAndTypeVo);
                listMap.put(recordAndTypeVo.getDeviceId(), vos);
            }
        }
        //调用查询设备接口
        JSONObject response = responseJson.get();
        if (response.getInteger("code") == 20000) {
            //返回设备
            devices = response.getJSONObject("data").getJSONArray("list");
        }
        //返回待保养设备集合
        for (int i = 0; i < devices.size(); i++) {
            JSONObject device = devices.getJSONObject(i);
            //存放保养类型id和最新记录和保养类型对象
            Map<Long, MaintenanceRecordAndTypeVo> recordAndTypeVoMap = new HashMap<>();
            //根据该设备的产品名获得产品和保养类型对象集合
            List<MaintenanceProductAndTypeVo> productAndTypeVos = productAndTypeVoMap.get(device.getString("productName"));
            //根据设备编号获得待保养的最新保养记录和保养类型集合
            List<MaintenanceRecordAndTypeVo> recordAndTypeVosList = listMap.get(device.getString("deviceId"));
            //判断该设备是否存在待保养的最新保养记录和保养类型对象
            if (recordAndTypeVosList != null) {
                for (MaintenanceRecordAndTypeVo recordAndTypeVo : recordAndTypeVosList) {
                    //存放该设备下保养类型为key的待保养的最新保养记录和保养类型对象
                    recordAndTypeVoMap.put(recordAndTypeVo.getTypeId(), recordAndTypeVo);
                }
            }
            //如果产品和保养类型对象集合不为空
            if (productAndTypeVos != null) {
                for (MaintenanceProductAndTypeVo productAndTypeVo : productAndTypeVos) {
                    if (recordAndTypeVoMap.containsKey(productAndTypeVo.getTypeId())) {
                        //待保养
                        updateMap(maps, productAndTypeVo);
                    } else if (staffName == null) {
                        //用当前时间和该年的一月一日时间戳差值
                        long time = System.currentTimeMillis() - TimestampUtils.getAppointTimestamp();
                        //判断该产品和保养类型对象是中的保养类型是否属于年保养
                        if (productAndTypeVo.getPeriodScope().equals("y")) {
                            //如果是年保养则用该差值和1年的时间戳作对比
                            if (time > YEAR) {
                                //待保养
                                updateMap(maps, productAndTypeVo);
                            }
                            //如果不是年保养而且该差值大于保养周期时间戳,则为待保养设备
                        } else if (!productAndTypeVo.getPeriodScope().equals("y") && time > Long.valueOf(productAndTypeVo.getPeriod())) {
                            //待保养
                            updateMap(maps, productAndTypeVo);
                        }
                    }
                }
            }

        }
        //把map集合中的value值对象放入list集合中
        maintenanceCountDtoList.addAll(maps.values());
        return maintenanceCountDtoList;
    }

    /**
     * 查询待保养设备信息(未启用)
     * @param userId
     * @param typeId
     * @param staffName
     * @param deviceDto
     * @return
     */
    public List<MaintenancePendingDto> searchMaintenancePending(Long userId, Long typeId, String staffName, DeviceDto deviceDto) {
        //存储设备编号和保养记录和保养类型对象
        Map<String, MaintenanceRecordAndTypeVo> map = new HashMap<>();
        JSONArray devices = new JSONArray();
        List<MaintenancePendingDto> maintenancePendingDtoList = new ArrayList<>();
        //根据typeId查询出保养类型对象
        MaintenanceType type = maintenanceTypeService.getTypeByTypeId(typeId);
        if (type == null) {
            throw new GlobalException(ResultEnum.OK,"找不到该保养类型");
        }
        //算出该保养类型下的周期时间
        long period = TypeMapUtils.typeConstants.get(type.getPeriodScope()) * type.getPeriodNumber() - type.getRemindNumber();
        //根据typeId查询产品类型
        QueryWrapper<ProductHasType> productHasTypeQueryWrapper = new QueryWrapper<>();
        productHasTypeQueryWrapper.eq("type_id", typeId);
        ProductHasType productHasType = productHasTypeService.getOne(productHasTypeQueryWrapper);
        if (productHasType == null) {
            throw new GlobalException(ResultEnum.OK, "该保养类型下无保养的产品");
        }
        //根据保养类型id查询保养类型和保养记录
        List<MaintenanceRecordAndTypeVo> recordAndTypeVos = maintenanceRecordService.searchRecordAndTypeByTypeId(typeId, staffName);
        //遍历每一个保养类型和记录对象
        for (MaintenanceRecordAndTypeVo recordAndTypeVo : recordAndTypeVos) {
            //放进map集合中
            map.put(recordAndTypeVo.getDeviceId(), recordAndTypeVo);
        }
        try {
            deviceDto.setProductName(productHasType.getProductName());
            //调用查询设备的服务接口
            JSONObject response = i2dspEmgData.getDeviceSimple(userId,  deviceDto, 1, 0);
            if (response.getInteger("code") == 20000) {
                JSONObject data = response.getJSONObject("data");
                //返回设备列表
                devices = data.getJSONArray("list");

                for (int i = 0; i < devices.size(); i++) {
                    MaintenancePendingDto maintenancePendingDto1 = new MaintenancePendingDto();
                    //获取设备json对象
                    JSONObject dev = devices.getJSONObject(i);
                    //根据设备id获取map集合中key对应的值
                    MaintenanceRecordAndTypeVo recordAndTypeVo = map.get(dev.getString("deviceId"));
                    //如果存在该记录，则拿当前时间和记录表中的时间做比较来计算待保养设备
                    if (recordAndTypeVo != null) {
                        //用当前时间戳减去上一次保养的时间戳获得天数
                        long day = (System.currentTimeMillis() - Long.valueOf(recordAndTypeVo.getGmtCreate()))/86400000l;
                        //获取到该保养类型下的周期和天数作比较，是否进入待保养的时间范围
                        if (day > period){
                            maintenancePendingDto1.setDeviceId(dev.getString("deviceId"))
                                    .setDeviceName(dev.getString("deviceName"))
                                    .setProductName(dev.getString("productName"))
                                    .setBuildingName(dev.getString("buildingName"))
                                    .setFloorName(dev.getString("floorName"))
                                    .setPlace(dev.getString("place"))
                                    .setLastRecordedTime(recordAndTypeVo.getGmtCreate())
                                    .setLastStaffName(recordAndTypeVo.getStaffName());
                            //把待保养的对象放进集合中
                            maintenancePendingDtoList.add(maintenancePendingDto1);
                        }
                        //如果不存在上一次保养的记录
                    }else if (staffName == null || staffName == "") {
                        //用当前时间戳与当年一月一日时间戳相减获得天数
                        long day = (System.currentTimeMillis() - TimestampUtils.getAppointTimestamp())/86400000l;
                        //获取到该保养类型下的周期和天数作比较，是否进入待保养的时间范围
                        if (day > period) {
                            maintenancePendingDto1.setDeviceId(dev.getString("deviceId"))
                                    .setDeviceName(dev.getString("deviceName"))
                                    .setProductName(dev.getString("productName"))
                                    .setBuildingName(dev.getString("buildingName"))
                                    .setFloorName(dev.getString("floorName"))
                                    .setPlace(dev.getString("place"));
                            //把待保养的对象放进集合中
                            maintenancePendingDtoList.add(maintenancePendingDto1);
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultEnum.OPENFEIGN_ERROR);
        }
        return maintenancePendingDtoList;
    }


    /**
     * 根据是否需要待保养来存储在map中
     * @param maps
     * @param maintenanceProductAndTypeVo
     */
    private void updateMap(Map<Long, MaintenanceCountDto> maps, MaintenanceProductAndTypeVo maintenanceProductAndTypeVo) {
        //判断map中是否已存在该保养类型id的key
        if (maps.containsKey(maintenanceProductAndTypeVo.getTypeId())) {
            //获得该key值下面的value对象
            MaintenanceCountDto maintenanceCountDto = maps.get(maintenanceProductAndTypeVo.getTypeId());
            //给maintenanceCountDto的counts属性的值加1
            maintenanceCountDto.setCounts(maintenanceCountDto.getCounts()+1);
            //重新放入map中
            maps.put(maintenanceProductAndTypeVo.getTypeId(),maintenanceCountDto);
        }else {
            //不存在则新增
            MaintenanceCountDto maintenanceCountDto = new MaintenanceCountDto();
            //给maintenanceCountDto对象赋初值
            maintenanceCountDto.setTypeId(maintenanceProductAndTypeVo.getTypeId()).setTypeName(maintenanceProductAndTypeVo.getTypeName())
                    .setPeriodScope(maintenanceProductAndTypeVo.getPeriodScope())
                    .setPeriodNumber(maintenanceProductAndTypeVo.getPeriodNumber())
                    .setCounts(1);
            //重新放入map中
            maps.put(maintenanceProductAndTypeVo.getTypeId(), maintenanceCountDto);
        }
    }

}
