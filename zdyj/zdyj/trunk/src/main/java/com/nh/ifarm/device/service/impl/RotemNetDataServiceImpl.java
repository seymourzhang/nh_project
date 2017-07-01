package com.nh.ifarm.device.service.impl;

import com.nh.ifarm.util.common.PageData;
import org.springframework.stereotype.Service;

/**
 * Created by Yin Guo Xiang on 2017/2/20.
 */
@Service
public class RotemNetDataServiceImpl extends DeviceServiceImpl {

    @Override
    public void start() {

    }

    /**
     * 插入RotemNet指标数据
     * @param pd 数据对象
     */
    public void insertRotemNet(PageData pd) throws Exception{
        dao.save("SBRotemNetDataMapper.insertRotemNet",pd);
    }
}
