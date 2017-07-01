package com.nh.ifarm.product.service;

import com.nh.ifarm.util.common.PageData;

import java.util.List;

/**
 * Created by Seymour on 2016/10/24.
 */
public interface TaskService {
    /**
     * @return
     * @throws Exception
     */
    List<PageData> getTaskTypeName() throws Exception;

    /**
     *
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getTaskCodeName(PageData pd) throws Exception;

    /***
     *
     * @return
     * @throws Exception
     */
    List<PageData> getDateTypeName() throws Exception;

    /**
     * 执行存储过程
     * @param procName 存储过程名
     * @throws Exception
     */
    void execProc(String procName) throws Exception;
}
