package com.nh.ifarm.breed.service;

import com.nh.ifarm.util.common.PageData;

import java.util.List;

/**
 * Created by Seymour on 2016/11/14.
 */
public interface SBGrowingStdService {
    List<PageData> selectByVarietyId(PageData pd) throws Exception;

    List<PageData> selectBroilByVarietyId(PageData pd) throws Exception;
}
