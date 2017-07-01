package com.nh.ifarm.system.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SDMenu implements Serializable{

	private Integer MENU_ID; // 菜单ID
	private Integer MENU_PID;// 菜单父ID
	private String MENU_NAME;// 菜单名称
	private String MENU_URL;// 菜单地址
	private String MENU_ICON;// 菜单图标
	private Date SORT_DATE;// 菜单排序时间
	private Date CREATE_DATE;// 菜单创建时间
	private Integer ISENABLED;// 是否启用菜单
	private Integer write_read;//菜单是否只读
	private String target;
	private SDMenu parentMenu;
	private List<SDMenu> subMenu;
	private boolean hasMenu = false;

	public Integer getWrite_read() {
		return write_read;
	}

	public void setWrite_read(Integer write_read) {
		this.write_read = write_read;
	}

	public Integer getMENU_ID() {
		return MENU_ID;
	}

	public void setMENU_ID(Integer mENU_ID) {
		MENU_ID = mENU_ID;
	}

	public Integer getMENU_PID() {
		return MENU_PID;
	}

	public void setMENU_PID(Integer mENU_PID) {
		MENU_PID = mENU_PID;
	}

	public String getMENU_NAME() {
		return MENU_NAME;
	}

	public void setMENU_NAME(String mENU_NAME) {
		MENU_NAME = mENU_NAME;
	}

	public String getMENU_URL() {
		return MENU_URL;
	}

	public void setMENU_URL(String mENU_URL) {
		MENU_URL = mENU_URL;
	}

	public String getMENU_ICON() {
		return MENU_ICON;
	}

	public void setMENU_ICON(String mENU_ICON) {
		MENU_ICON = mENU_ICON;
	}

	public Date getSORT_DATE() {
		return SORT_DATE;
	}

	public void setSORT_DATE(Date sORT_DATE) {
		SORT_DATE = sORT_DATE;
	}

	public Date getCREATE_DATE() {
		return CREATE_DATE;
	}

	public void setCREATE_DATE(Date cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}

	public Integer getISENABLED() {
		return ISENABLED;
	}

	public void setISENABLED(Integer iSENABLED) {
		ISENABLED = iSENABLED;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public SDMenu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(SDMenu parentMenu) {
		this.parentMenu = parentMenu;
	}

	public List<SDMenu> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<SDMenu> subMenu) {
		this.subMenu = subMenu;
	}

	public boolean isHasMenu() {
		return hasMenu;
	}

	public void setHasMenu(boolean hasMenu) {
		this.hasMenu = hasMenu;
	}

}
