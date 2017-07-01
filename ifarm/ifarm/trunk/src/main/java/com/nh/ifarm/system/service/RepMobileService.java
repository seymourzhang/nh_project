package com.nh.ifarm.system.service;

import com.nh.ifarm.util.common.PageData;

import java.util.List;

/**
 * Created by Seymour on 2017/5/31.
 */
public interface RepMobileService {
    List<PageData> getRepMobile() throws Exception;

    List<PageData> getWeekDate(PageData pd) throws Exception;
    List<PageData> getItemData(PageData pd) throws Exception;
    List<PageData> getDeliveryData(PageData pd) throws Exception;
    List<PageData> getCutFeedData(PageData pd) throws Exception;
    List<PageData> getBreedData(PageData pd) throws Exception;


    List<PageData> getItemDataTotal(PageData pd) throws Exception;
    List<PageData> getDeliveryDataTotal(PageData pd) throws Exception;
    List<PageData> getCutFeedDataTotal(PageData pd) throws Exception;
    List<PageData> getBreedDataTotal(PageData pd) throws Exception;


    List<PageData> getItemDataAvg(PageData pd) throws Exception;
    List<PageData> getDeliveryDataAvg(PageData pd) throws Exception;
    List<PageData> getCutFeedDataAvg(PageData pd) throws Exception;
    List<PageData> getBreedDataAvg(PageData pd) throws Exception;


    List<PageData> getItemDataTarget(PageData pd) throws Exception;
    List<PageData> getDeliveryDataTarget(PageData pd) throws Exception;
    List<PageData> getCutFeedDataTarget(PageData pd) throws Exception;
    List<PageData> getBreedDataTarget(PageData pd) throws Exception;
}
