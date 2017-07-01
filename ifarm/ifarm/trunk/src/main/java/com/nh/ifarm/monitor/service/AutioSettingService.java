package com.nh.ifarm.monitor.service;

import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;

/**
 * Created by Seymour on 2017/1/11.
 */
public interface AutioSettingService {
    /***
     * 语音设置
     * @param pd
     * @return
     * @throws Exception
     */
    PageData saveInfo(PageData pd) throws Exception;

    /***
     * 语音设置查询
     * @param pd
     * @return
     * @throws Exception
     */
    PageData queryInfo(PageData pd) throws Exception;

    /**
     *查看imei信息
     */
    PageData queryImeiInfo(PageData pd) throws Exception;
}
