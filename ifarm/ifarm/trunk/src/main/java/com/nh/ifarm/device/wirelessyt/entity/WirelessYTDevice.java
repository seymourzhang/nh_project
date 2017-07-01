package com.nh.ifarm.device.wirelessyt.entity;

import java.util.Date;

/**
 * 引通控制器
 * @TODO
 * @Date 2016-6-23
 * @author loyeWen
 *
 */
public class WirelessYTDevice {
	private String cid;
	private String sNo;
	private String dataV;
	private String dataK;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getsNo() {
		return sNo;
	}
	public void setsNo(String sNo) {
		this.sNo = sNo;
	}
	public String getDataV() {
		return dataV;
	}
	public void setDataV(String dataV) {
		this.dataV = dataV;
	}
	public String getDataK() {
		return dataK;
	}
	public void setDataK(String dataK) {
		this.dataK = dataK;
	}

	private String power_status;
	private String voltage;
	private String run_time;
	private String signalStrength;
	private String hardware_version;
	private String software_version;
	private String device_attr;
	private String device_info;
	private String sim_iccid;

	// 系统时间，存储秒数
	private long makeTime;
	// 设备时间，存储秒数
	private long ytDataTime;

	private String frame_type;
	private String frame_sn;

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getSim_iccid() {
		return sim_iccid;
	}

	public void setSim_iccid(String sim_iccid) {
		this.sim_iccid = sim_iccid;
	}

	public String getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(String signalStrength) {
		this.signalStrength = signalStrength;
	}

	public long getMakeTime() {
		return makeTime;
	}

	public void setMakeTime(long makeTime) {
		this.makeTime = makeTime;
	}

	public String getDevice_attr() {
		return device_attr;
	}

	public void setDevice_attr(String device_attr) {
		this.device_attr = device_attr;
	}

	public String getPower_status() {
		return power_status;
	}

	public void setPower_status(String power_status) {
		this.power_status = power_status;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getRun_time() {
		return run_time;
	}

	public void setRun_time(String run_time) {
		this.run_time = run_time;
	}

	public String getHardware_version() {
		return hardware_version;
	}

	public void setHardware_version(String hardware_version) {
		this.hardware_version = hardware_version;
	}

	public String getSoftware_version() {
		return software_version;
	}

	public void setSoftware_version(String software_version) {
		this.software_version = software_version;
	}

	public String getFrame_type() {
		return frame_type;
	}

	public void setFrame_type(String frame_type) {
		this.frame_type = frame_type;
	}

	public String getFrame_sn() {
		return frame_sn;
	}

	public void setFrame_sn(String frame_sn) {
		this.frame_sn = frame_sn;
	}

	public long getYtDataTime() {
		return ytDataTime;
	}

	public void setYtDataTime(long ytDataTime) {
		this.ytDataTime = ytDataTime;
	}
}
