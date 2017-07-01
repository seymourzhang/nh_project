package com.nh.ifarm.device.dmr.entity;

/**
 * 
 * @TODO大牧人数据实体
 * @Date 2016-6-23
 * @author loyeWen
 *
 */
public class DMRSource {
	public String appKey;
	public String ControlID;
	public DmrQuota dq;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getControlID() {
		return ControlID;
	}
	public void setControlID(String controlID) {
		ControlID = controlID;
	}
	public DmrQuota getDmrQuota() {
		return dq;
	}
	public void setDmrQuota(DmrQuota dq) {
		this.dq = dq;
	}
}
