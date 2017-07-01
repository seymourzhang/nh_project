package com.nh.ifarm.user.service;

import com.nh.ifarm.util.common.PageData;

/**
 * Created by Seymour on 2016/11/18.
 */
public interface SBUserImeiService {

    int insert(PageData pd) throws Exception;

    int insertLog(PageData pd) throws Exception;
}
