package com.nh.ifarm.breed.service;

import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: laven
 * @Date: 2017/6/8 上午11:40
 */
public interface CultivateCodeService {

    Json uploadCultivateStandard(HttpServletRequest request, HttpServletResponse response, MultipartFile file, PageData pageData) throws Exception;
    /**
     *@Description: 根据用户权限获取当前页需要显示的tab页面
     *
     *@Params:
     *@Author: laven
     *@Date: 2017/6/9 下午3:09
     */
    String getTabShow(int userId) throws Exception;

    /**
     * 获取预警比例
     * @return
     */
    Double getWarnRate();

    /**
     * 获取报警比例
     * @return
     */
    Double getAlarmRate();

    /**
     * 获取所有的育成标准设置数据
     * @return
     * @param goodsType
     */
    List<PageData> selectGrowSettingList(String goodsType) throws Exception;

    /**
     * 获取所有的产蛋标准设置数据
     * @return
     * @param goodsType
     */
    List<PageData> selectEggSettingList(String goodsType) throws Exception;

    /**
     * 获取所有的肉鸡标准的设置数据
     * @return
     * @param goodsType
     */
    List<PageData> selectMeatSettingList(String goodsType) throws Exception;


    Json settingWarnAndAlarmData(List<PageData> list, Map<String, Double> map, String goodsType) throws Exception;
}
