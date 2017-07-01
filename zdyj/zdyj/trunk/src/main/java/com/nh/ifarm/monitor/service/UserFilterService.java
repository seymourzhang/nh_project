package com.nh.ifarm.monitor.service;

import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;

import java.util.List;

/**
 * Created by Seymour on 2016/9/7.
 */
public interface UserFilterService {
    /***
     * 按条件查询
     *
     * @param pd
     * @return
     * @throws Exception
     */
    PageData selectByCondition(PageData pd) throws Exception;

    /***
     * 插入监控设置
     *
     * @param pd
     * @return
     * @throws Exception
     */
    int saveSet(PageData pd) throws Exception;

    /***
     * 修改监控设置
     *
     * @param pd
     * @return
     * @throws Exception
     */
    int updateSet(PageData pd) throws Exception;
}
