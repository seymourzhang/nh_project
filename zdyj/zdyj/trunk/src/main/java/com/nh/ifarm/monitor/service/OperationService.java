package com.nh.ifarm.monitor.service;

import com.nh.ifarm.util.common.PageData;

import java.util.List;

/**
 * Created by Seymour on 2016/10/13.
 */
public interface OperationService {

    /***
     * 按条件查询id
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> selectByCondition(PageData pd) throws Exception;

    /***
     * 按条件查询架构
     * @return List<PageData>
     * @throws Exception
     */
    List<PageData> selectAll(PageData pd) throws Exception;

    /***
     * 按条件查询栋舍
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> selectByConditionMy(PageData pd) throws Exception;

    /***
     * 查询架构上层级数据
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> selectForUp(PageData pd) throws Exception;

}
