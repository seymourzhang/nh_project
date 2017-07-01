package com.nh.ifarm.device.wirelessyt.entity;

import com.nh.ifarm.device.entity.DeviceQuota;

/**
 * 引通控制器指标类
 * Created by Raymon on 7/6/2016.
 */
public class WirelessYTQuota extends DeviceQuota {
    public long maketime ;
    public String signalStrength ;

    public long getMaketime() {
        return maketime;
    }
    public void setMaketime(long maketime) {
        this.maketime = maketime;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }
}
