package com.nh.ifarm.breed.service.impl;

import com.nh.ifarm.base.service.BaseService;
import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.breed.service.CultivateCodeService;
import com.nh.ifarm.breed.service.SBGrowingStdService;
import com.nh.ifarm.breed.service.SDFileService;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.Office;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import com.nh.ifarm.util.service.ModuleService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * @Description:养殖宝典菜单的service类
 * @Author: laven
 * @Date: 2017/6/8 上午11:41
 */
@Service
public class CultivateCodeServiceImpl extends BaseService implements CultivateCodeService{
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    ModuleService moduleService;

    @Autowired
    private SDFileService sdFileService;

    @Autowired
    private SBGrowingStdService sbGrowingStdService;

    @Autowired
    private BatchManageService batchManageService;

    @Override
    public Json uploadCultivateStandard(HttpServletRequest request, HttpServletResponse response, MultipartFile file, PageData pageData) throws Exception {
        Json resJson = new Json();
        String fileType = pageData.getString("upload_file_type");
        //保存文件获取文件保存的路径。
        ResourceBundle conf = null;
        if("3".equals(fileType)){
            conf = ResourceBundle.getBundle("pro/excelFormat_cultivate_standard");
        }else if("4".equals(fileType)){
            conf = ResourceBundle.getBundle("pro/excelFormat_cultivate_standard_meat");
        }
        String filePath = sdFileService.uploadFile(request, response, file, fileType, resJson);
        //根据保存得到的文件路径读取文件,获取文件内容。
        Office office = new Office();
        Workbook workbook = office.openFile(filePath);
        pageData.putAll(office.checkExcelDataVaild(workbook, conf, fileType));
        //获取读取数据返回的状态
        String res = String.valueOf(pageData.get("error_type"));
        if(!"-1".equals(res)){
            resJson.setMsg(getResMsgMap().get(res));
        }
        //处理读取到文件的数据
        List<PageData> list = (List<PageData>) pageData.get("data");
        //如果数据已存在则更新,如果不存在则插入,获取此功能需要的数据结构
        List<PageData> growList = sbGrowingStdService.selectBroilByVarietyId(new PageData());
        List<PageData> eggList = sbGrowingStdService.selectByVarietyId(new PageData());
        List<PageData> meatList = sbGrowingStdService.selectCultivateStandardMeatData(new PageData());
        HashMap<String, PageData> growMap = getMap(growList, null);
        HashMap<String, PageData> eggMap = getMap(eggList, null);
        HashMap<String, PageData> meatMap = getMap(meatList, "meat");

        HashMap<String, Integer> goodsMap = batchManageService.getGoodsMap();

        if (fileType.equals("3") || fileType.equals("4")) {
            for (PageData pageDataList : list) {
                if (pageDataList.get(0).toString().equals("false")) {
                    pageDataList.put("modify_person", 0);
                    pageDataList.put("modify_date", new Date());
                    pageDataList.put("modify_time", new Date());
                    pageDataList.put("create_person", 0);
                    pageDataList.put("create_date", new Date());
                    pageDataList.put("create_time", new Date());

                    //判断导入的品种是否存在
                    if (goodsMap.get(pageDataList.get("variety")) == null) {
                        resJson.setMsg("存在未维护品种鸡");
                        break;
                    }
                    pageDataList.put("variety_id", goodsMap.get(pageDataList.get("variety")));
                    String cultivateStage = (String) pageDataList.get("cultivateStage");
                    if (cultivateStage.contains("育成")) {
                        //处理育成标准表数据
                        if(growMap.containsKey(getIndex(pageDataList, null))){
                            Json json = sbGrowingStdService.updateCultivateStandardGrowData(pageDataList);
                        }else{
                            Json json = sbGrowingStdService.saveCultivateStandardGrowData(pageDataList);
                        }
                    } else if (cultivateStage.contains("产蛋")) {
                        if(eggMap.containsKey(getIndex(pageDataList, null))){
                            Json json = sbGrowingStdService.updateCultivateStandardEggData(pageDataList);
                        }else{
                            Json json = sbGrowingStdService.saveCultivateStandardEggData(pageDataList);
                        }
                    } else if (cultivateStage.contains("肉鸡")) {
                        if(meatMap.containsKey(getIndex(pageDataList, "meat"))){
                            Json json = sbGrowingStdService.updateCultivateStandardMeatData(pageDataList);
                        }else{
                            Json json = sbGrowingStdService.saveCultivateStandardMeatData(pageDataList);
                        }
                    } else {
                        resJson.setMsg("上传数据不正确");
                        break;
                    }
                }
            }
            Json json = settingWarnAndAlarmData(list, null, null);
        } else {
            resJson.setMsg("导入了错误的类型数据");
            return resJson;
        }

        if("".equals(resJson.getMsg())){
            resJson.setSuccess(true);
        }else{
            resJson.setSuccess(false);
        }
        return resJson;
    }



    /**
     * 处理预警及警告的数据
     *
     * 1.如果是导入文件触发,判断对应警告预警设置表中是否存在数据;存在数据则读取rate进行计算,不存在数据,则获取默认rate进行计算
     * 2.如果是提交警告或预警设置的表单触发,直接获取当前标准数据,进行计算覆盖操作。
     * list 导入文件的数据,且经过上传逻辑处理后的数据,为其赋值了id及判断了品种是否存在的数据集合
     * @return
     */
    @Override
    public Json settingWarnAndAlarmData(List<PageData> list, Map<String, Double> map, String goodsType) throws Exception{
        Json resJson = new Json();
        Double warnRate = getWarnRate();
        Double alarmRate = getAlarmRate();
        List<PageData> growSettingList = selectGrowSettingList(goodsType);
        List<PageData> eggSettingList = selectEggSettingList(goodsType);
        List<PageData> meatSettingList = selectMeatSettingList(goodsType);
        HashMap<String, PageData> growSettingMap = getMap(growSettingList, null);
        HashMap<String, PageData> eggSettingMap = getMap(eggSettingList, null);
        HashMap<String, PageData> meatSettingMap = getMap(meatSettingList, "meat");

        if(list != null){
            for (PageData pageDataList : list) {
                if (pageDataList.get(0).toString().equals("false")) {
                    String cultivateStage = (String) pageDataList.get("cultivateStage");
                    if (cultivateStage.contains("育成")) {
                        String index = getIndex(pageDataList, null);
                        if(growSettingMap.containsKey(index)){
                            //获取设置表内现有的比例进行计算后更新数据
                            PageData growSetting = calculate(pageDataList, growSettingMap.get(index), null, null, null, null);
                            Json json = updateGrowSettingData(growSetting);
                        }else{
                            //使用默认的比例数据计算后保存数据
                            PageData growSetting = calculate(pageDataList, null, null, null, warnRate, alarmRate);
                            Json json = insertGrowSettingData(growSetting);
                        }
                    } else if (cultivateStage.contains("产蛋")) {
                        String index = getIndex(pageDataList, null);
                        if(eggSettingMap.containsKey(index)){
                            PageData eggSetting = calculate(pageDataList, eggSettingMap.get(index), null, null, null, null);
                            Json json = updateEggSettingData(eggSetting);
                        }else{
                            PageData eggSetting = calculate(pageDataList, null, null, null, warnRate, alarmRate);
                            Json json = insertEggSettingData(eggSetting);
                        }
                    } else if (cultivateStage.contains("肉鸡")) {
                        String index = getIndex(pageDataList, "meat");
                        if(meatSettingMap.containsKey(index)){
                            PageData meatSetting = calculate(pageDataList, meatSettingMap.get(index), null, null, null, null);
                            Json json = updateMeatSettingData(meatSetting);
                        }else{
                            PageData meatSetting = calculate(pageDataList, null, null, null, warnRate, alarmRate);
                            Json json = insertMeatSettingData(meatSetting);
                        }
                    } else {
                        resJson.setMsg("上传数据不正确");
                        break;
                    }
                }
            }
        }
        if(map != null){
            Double type = Double.valueOf(String.valueOf(map.get("type")));
            map.remove("type");
            boolean isWarn = getIsWarn(type);
            boolean isGrow = false;
            boolean isEgg = false;
            boolean isMeat = false;
            HashMap<String, PageData> settingMap = null;

            List<PageData> growList = sbGrowingStdService.selectBroilByVarietyId(new PageData());
            List<PageData> eggList = sbGrowingStdService.selectByVarietyId(new PageData());
            List<PageData> meatList = sbGrowingStdService.selectCultivateStandardMeatData(new PageData());
            HashMap<String, PageData> growMap = getMap(growList, null);
            HashMap<String, PageData> eggMap = getMap(eggList, null);
            HashMap<String, PageData> meatMap = getMap(meatList, "meat");

            if(type == 4D || type == 7D){
                //育成数据处理
                list = growList;
                settingMap = growSettingMap;
                isGrow = true;
            }else if(type == 5D || type == 8D){
                //产蛋数据处理
                list = eggList;
                settingMap = eggSettingMap;
                isEgg = true;
            }else if(type == 6D || type == 9D){
                //肉鸡数据处理
                list = meatList;
                settingMap = meatSettingMap;
                isMeat = true;
            }
            //数据处理
            for (int i = 0; i < list.size(); i++) {
                PageData pageData = list.get(i);
                String index = getIndex(pageData, isMeat ? "meat" : null);
                //使用前端传来的比率计算新的数据
                PageData settingData = calculate(pageData, settingMap.get(index), (isWarn ? map : null), (isWarn ? null : map), null, null);
                if(isGrow){
                    Json json = updateGrowSettingData(settingData);
                }
                if(isEgg){
                    Json json = updateEggSettingData(settingData);
                }
                if(isMeat){
                    Json json = updateMeatSettingData(settingData);
                }

            }
        }
        return resJson;
    }

    private boolean getIsWarn(Double type) {
        boolean isWarn = false;
        if(type == 4D || type == 5D || type == 6D){
            isWarn = true;
        }
        return isWarn;
    }

    private Json insertGrowSettingData(PageData growSetting) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("CultivateCodeMapper.insertGrowSettingData", growSetting);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

    private Json updateGrowSettingData(PageData growSetting) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("CultivateCodeMapper.updateGrowSettingData", growSetting);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

    private Json insertEggSettingData(PageData eggSetting) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("CultivateCodeMapper.insertEggSettingData", eggSetting);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

    private Json updateEggSettingData(PageData eggSetting) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("CultivateCodeMapper.updateEggSettingData", eggSetting);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

    private Json insertMeatSettingData(PageData meatSetting) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("CultivateCodeMapper.insertMeatSettingData", meatSetting);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

    private Json updateMeatSettingData(PageData meatSetting) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("CultivateCodeMapper.updateMeatSettingData", meatSetting);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }
    Map<String, String> columnMap = null;
    /**
     * 计算预警及报警数据(通过参数判断调用方)情况如下
     *
     * 1.文件导入时使用设置表内的原有rate数据进行计算。stdPageData, settingPageData, null, null, null, null
     * 2.文件导入时使用默认的rate数据进行计算。stdPageData, null, null, null, [warnRate,] [alarmRate]
     * 3.提交预警设置时进行计算。stdPageData, settingPageData, warnRateMap, null, null, null
     * 4.提交报警设置时进行计算。stdPageData, settingPageData, null, alarmRateMap, null, null
     * @param stdPageData:标准数据
     * @param settingPageData:需要计算的数据对象
     * @param warnRateMap:前端提交的预警数据设置集合
     * @param alarmRateMap:前端提交的报警数据设置集合
     * @param warnRate:默认的预警比例数据
     * @param alarmRate:默认的报警比例数据
     * @return
     */
    private PageData calculate(PageData stdPageData, PageData settingPageData, Map<String, Double> warnRateMap, Map<String, Double> alarmRateMap, Double warnRate, Double alarmRate) throws Exception{
        PageData settingData = null;
        if(columnMap == null){
            columnMap = getColumnMap();
        }
        if(warnRateMap == null && alarmRateMap == null && settingPageData == null ){
            //文件第一次导入,初始化设置表内的预警报警等数据
            settingData = new PageData();
            Iterator it = stdPageData.keySet().iterator();
            while(it.hasNext()){
                String key = String.valueOf(it.next());
                if(columnMap.containsKey(key)){
                    //当前字段标准值
                    double std = Double.valueOf("-".equals(stdPageData.getString(key))? "0" : stdPageData.getString(key));
                    //预警值比率
                    double wRate = warnRate;
                    //报警值比率
                    double aRate = alarmRate;
                    //高预警值
                    double highWarn = std * (1 + (wRate/100));
                    //低预警值
                    double lowWarn = std * (1 - (wRate/100));
                    //高报警值
                    double highAlarm = std * (1 + (aRate/100));
                    //低报警值
                    double lowAlarm = std * (1 - (aRate/100));

                    settingData.put(key + "_warm_rate", wRate);
                    settingData.put(key + "_alarm_rate", aRate);
                    settingData.put(key + "_high_warm", highWarn);
                    settingData.put(key + "_low_warm", lowWarn);
                    settingData.put(key + "_high_alarm", highAlarm);
                    settingData.put(key + "_low_alarm", lowAlarm);

                }else{
                    settingData.put(key, stdPageData.get(key));
                }
            }
        }
        if(warnRateMap == null && alarmRateMap == null && settingPageData != null ){
            //文件导入触发,设置表内已经存在比率数据,使用原数据进行计算
            settingData = settingPageData;
            Iterator it = stdPageData.keySet().iterator();
            while(it.hasNext()){
                String key = String.valueOf(it.next());
                if(columnMap.containsKey(key)){
                    try{
                        //当前字段标准值
                        double std = Double.valueOf("-".equals(stdPageData.getString(key))? "0" : stdPageData.getString(key));
                        //预警值比率
                        String wRateString = settingData.getString(key + "_warm_rate");
                        double wRate = wRateString == "" ? 0D : Double.valueOf(wRateString);
                        //报警值比率
                        String aRateString = settingData.getString(key + "_alarm_rate");
                        double aRate = aRateString == "" ? 0D : Double.valueOf(aRateString);
                        //高预警值
                        double highWarn = std * (1 + (wRate/100));
                        //低预警值
                        double lowWarn = std * (1 - (wRate/100));
                        //高报警值
                        double highAlarm = std * (1 + (aRate/100));
                        //低报警值
                        double lowAlarm = std * (1 - (aRate/100));

                        settingData.put(key + "_warm_rate", wRate);
                        settingData.put(key + "_alarm_rate", aRate);
                        settingData.put(key + "_high_warm", highWarn);
                        settingData.put(key + "_low_warm", lowWarn);
                        settingData.put(key + "_high_alarm", highAlarm);
                        settingData.put(key + "_low_alarm", lowAlarm);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }
        }
        if(warnRateMap != null && alarmRateMap == null && settingPageData != null ){
            //前端修改预警比率时触发
            settingData = settingPageData;
            Iterator it = stdPageData.keySet().iterator();
            while(it.hasNext()){
                String key = String.valueOf(it.next());
                if(columnMap.containsKey(key)){
                    //当前字段标准值
                    double std = Double.valueOf("-".equals(stdPageData.getString(key))? "0" : stdPageData.getString(key));
                    //预警值比率
                    double wRate = warnRateMap.get(key) == null? 0D : warnRateMap.get(key);
                    //报警值比率
                    String aRateString = settingData.getString(key + "_alarm_rate");
                    double aRate = aRateString == "" ? 0D : Double.valueOf(aRateString);
                    //高预警值
                    double highWarn = std * (1 + (wRate/100));
                    //低预警值
                    double lowWarn = std * (1 - (wRate/100));
                    //高报警值
                    double highAlarm = std * (1 + (aRate/100));
                    //低报警值
                    double lowAlarm = std * (1 - (aRate/100));

                    settingData.put(key + "_warm_rate", wRate);
                    settingData.put(key + "_alarm_rate", aRate);
                    settingData.put(key + "_high_warm", highWarn);
                    settingData.put(key + "_low_warm", lowWarn);
                    settingData.put(key + "_high_alarm", highAlarm);
                    settingData.put(key + "_low_alarm", lowAlarm);

                }

            }
        }
        if(warnRateMap == null && alarmRateMap != null && settingPageData != null ){
            //前端修改报警比率时触发
            settingData = settingPageData;
            Iterator it = stdPageData.keySet().iterator();
            while(it.hasNext()){
                String key = String.valueOf(it.next());
                if(columnMap.containsKey(key)){
                    //当前字段标准值
                    double std = Double.valueOf("-".equals(stdPageData.getString(key))? "0" : stdPageData.getString(key));
                    //预警值比率
                    String wRateString = settingData.getString(key + "_warm_rate");
                    double wRate = wRateString == "" ? 0D : Double.valueOf(wRateString);
                    //报警值比率
                    double aRate = alarmRateMap.get(key) == null ? 0D :alarmRateMap.get(key);
                    //高预警值
                    double highWarn = std * (1 + (wRate/100));
                    //低预警值
                    double lowWarn = std * (1 - (wRate/100));
                    //高报警值
                    double highAlarm = std * (1 + (aRate/100));
                    //低报警值
                    double lowAlarm = std * (1 - (aRate/100));

                    settingData.put(key + "_warm_rate", wRate);
                    settingData.put(key + "_alarm_rate", aRate);
                    settingData.put(key + "_high_warm", highWarn);
                    settingData.put(key + "_low_warm", lowWarn);
                    settingData.put(key + "_high_alarm", highAlarm);
                    settingData.put(key + "_low_alarm", lowAlarm);

                }

            }
        }
        return settingData;
    }

    /**
     * 获取育成,产蛋,肉鸡的标准表内所有需要计算的字段Map集合
     * @return
     */
    private Map<String,String> getColumnMap()  throws Exception{
        HashMap<String, String> resMap = new HashMap<String, String>();
        List<PageData> columnList = getColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            resMap.put(String.valueOf(columnList.get(i).get("COLUMN_NAME")), "");
        }
        //删除一些公共且不需要计算的字段
        resMap.remove("id");
        resMap.remove("variety_id");
        resMap.remove("variety");
        resMap.remove("grow_week_age");
        resMap.remove("grow_age");
        resMap.remove("create_person");
        resMap.remove("modify_person");
        resMap.remove("create_date");
        resMap.remove("create_time");
        resMap.remove("modify_date");
        resMap.remove("modify_time");
        resMap.remove("cl_laying_rate_low_warm");
        return resMap;
    }

    private List<PageData> getColumnList() throws Exception{
        List<PageData> resList = new ArrayList<PageData>();
        ResourceBundle conf = ResourceBundle.getBundle("pro/dbconfig");
        String schema = String.valueOf(conf.getObject("user_name"));
        PageData dataMap = new PageData();
        dataMap.put("schema", schema);
        dataMap.put("tableName", "s_b_growing_std");
        List<PageData> growColumnList = (List<PageData>) dao.findForList("CultivateCodeMapper.selectColumnNameList", dataMap);
        dataMap.put("tableName", "s_b_lay_egg_std");
        List<PageData> eggColumnList = (List<PageData>) dao.findForList("CultivateCodeMapper.selectColumnNameList", dataMap);
        dataMap.put("tableName", "s_b_meat_std");
        List<PageData> meatColumnList = (List<PageData>) dao.findForList("CultivateCodeMapper.selectColumnNameList", dataMap);
        resList.addAll(growColumnList);
        resList.addAll(eggColumnList);
        resList.addAll(meatColumnList);
        return resList;
    }

    @Override
    public String getTabShow(int userId) throws Exception {
        String resString = "";
        PageData pageData = new PageData();
        pageData.put("userId", userId);
        pageData.put("menuId", 11);//种鸡菜单
        boolean menu11Right = moduleService.service("SDMenuServiceImpl", "checkMenuRight",new Object[]{pageData} );
        pageData.put("menuId", 12);//肉鸡菜单
        boolean menu12Right = moduleService.service("SDMenuServiceImpl", "checkMenuRight",new Object[]{pageData} );

        if(menu11Right){
            resString += "12";
        }
        if(menu12Right){
            resString += "3";
        }
        return resString;
    }

    @Override
    public Double getWarnRate() {
        return 5D;
    }

    @Override
    public Double getAlarmRate() {
        return 10D;
    }

    @Override
    public List<PageData> selectGrowSettingList(String goodsType) throws Exception{
        PageData params = new PageData();
        params.put("variety_id", goodsType);
        List<PageData> growSettingList = (List<PageData>) dao.findForList("CultivateCodeMapper.selectGrowSettingList", params);
        List<PageData> growList = sbGrowingStdService.selectBroilByVarietyId(new PageData());
        HashMap<String, PageData> growMap = getMap(growList, null);
        growSettingList = mergeData(growSettingList, growMap, null);
        return growSettingList;
    }

    /**
     * 查找预警及报警数据时将标准数据同时放入,以实现鼠标悬停事件
     * @param settingList
     * @param stdMap
     * @return
     */
    private List<PageData> mergeData(List<PageData> settingList, HashMap<String, PageData> stdMap, String type) {
        for (int i = 0; i < settingList.size(); i++) {
            PageData pageData = settingList.get(i);
            String index = getIndex(pageData, type);
            if(stdMap.containsKey(index)){
                Iterator it = stdMap.get(index).keySet().iterator();
                while (it.hasNext()){
                    String key = String.valueOf(it.next());
                    pageData.put(key, stdMap.get(index).get(key));
                }
            }
        }
        return settingList;
    }

    @Override
    public List<PageData> selectEggSettingList(String goodsType) throws Exception{
        PageData params = new PageData();
        params.put("variety_id", goodsType);
        List<PageData> eggSettingList = (List<PageData>) dao.findForList("CultivateCodeMapper.selectEggSettingList", params);
        List<PageData> eggList = sbGrowingStdService.selectByVarietyId(new PageData());
        HashMap<String, PageData> eggMap = getMap(eggList, null);
        eggSettingList = mergeData(eggSettingList, eggMap, null);
        return eggSettingList;
    }

    @Override
    public List<PageData> selectMeatSettingList(String goodsType) throws Exception{
        PageData params = new PageData();
        params.put("variety_id", goodsType);
        List<PageData> meatSettingList = (List<PageData>) dao.findForList("CultivateCodeMapper.selectMeatSettingList", params);
        List<PageData> meatList = sbGrowingStdService.selectCultivateStandardMeatData(new PageData());
        HashMap<String, PageData> meatMap = getMap(meatList, "meat");
        meatSettingList = mergeData(meatSettingList, meatMap, "meat");
        return meatSettingList;
    }



    private HashMap<String,PageData> getMap(List<PageData> pageDataList, String type) {
        HashMap<String, PageData> resMap = new HashMap<String, PageData>();
        for (int i = 0; i <pageDataList.size() ; i++) {
            String index = getIndex(pageDataList.get(i), type);
            resMap.put(index, pageDataList.get(i));
        }
        return resMap;
    }

    private String getIndex(PageData pageData, String type) {
        String varietyId = String.valueOf(pageData.get("variety_id"));
        String growWeekAge = "";
        if(type == null || "".equals(type)){
            growWeekAge = String.valueOf(pageData.get("grow_week_age"));
        }else if(type.equals("meat")){
            growWeekAge = String.valueOf(pageData.get("grow_age"));
        }
        return varietyId + "_" + growWeekAge;
    }

}
