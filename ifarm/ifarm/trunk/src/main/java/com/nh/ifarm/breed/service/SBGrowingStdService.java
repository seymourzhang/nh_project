package com.nh.ifarm.breed.service;

import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;

import java.util.List;

/**
 * Created by Seymour on 2016/11/14.
 */
public interface SBGrowingStdService {
    List<PageData> selectByVarietyId(PageData pd) throws Exception;

    List<PageData> selectBroilByVarietyId(PageData pd) throws Exception;

    List<PageData> selectCultivateStandardMeatData(PageData pd) throws Exception;

    Json saveCultivateStandardGrowData(PageData pageData) throws Exception;

    Json updateCultivateStandardGrowData(PageData pageData) throws Exception;

    Json saveCultivateStandardEggData(PageData pageData) throws Exception;

    Json updateCultivateStandardEggData(PageData pageData) throws Exception;

    Json saveCultivateStandardMeatData(PageData pageData) throws Exception;

    Json updateCultivateStandardMeatData(PageData pageData) throws Exception;
}
