package com.nh.ifarm.user.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 2016-7-6
 * 
 * @author varro 用户对象
 */
public class SDUser implements Serializable {
	private Integer id; // 用户id
	private String user_code; // 用户名
	private String user_real_name; // 中文名
	private String user_real_name_en; // 英文名
	private String user_password; // 密码
	private String user_mobile_1; // 手机1
	private String user_mobile_2; // 手机2
	private String user_tel_1; // 固话1
	private String user_tel_2; // 固话2
	private String user_email; // 邮箱
	private String user_status; // 用户状态 1-正常
	private String freeze_status; // 冻结状态 1-已删除 0-未删除
	private String memo_1; // 备用字段1
	private String memo_2; // 备用字段2
	private String memo_3; // 备用字段3
	private String memo_4; // 备用字段4
	private String create_person; // 创建人
	private Date create_date; // 创建日期
	private Date create_time; // 创建时间
	private Integer modify_person; // 修改人
	private Date modify_date; // 修改日期
	private Date modify_time; // 修改时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser_code() {
		return user_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	public String getUser_real_name() {
		return user_real_name;
	}

	public void setUser_real_name(String user_real_name) {
		this.user_real_name = user_real_name;
	}

	public String getUser_real_name_en() {
		return user_real_name_en;
	}

	public void setUser_real_name_en(String user_real_name_en) {
		this.user_real_name_en = user_real_name_en;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getUser_mobile_1() {
		return user_mobile_1;
	}

	public void setUser_mobile_1(String user_mobile_1) {
		this.user_mobile_1 = user_mobile_1;
	}

	public String getUser_mobile_2() {
		return user_mobile_2;
	}

	public void setUser_mobile_2(String user_mobile_2) {
		this.user_mobile_2 = user_mobile_2;
	}

	public String getUser_tel_1() {
		return user_tel_1;
	}

	public void setUser_tel_1(String user_tel_1) {
		this.user_tel_1 = user_tel_1;
	}

	public String getUser_tel_2() {
		return user_tel_2;
	}

	public void setUser_tel_2(String user_tel_2) {
		this.user_tel_2 = user_tel_2;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}

	public String getFreeze_status() {
		return freeze_status;
	}

	public void setFreeze_status(String freeze_status) {
		this.freeze_status = freeze_status;
	}

	public String getMemo_1() {
		return memo_1;
	}

	public void setMemo_1(String memo_1) {
		this.memo_1 = memo_1;
	}

	public String getMemo_2() {
		return memo_2;
	}

	public void setMemo_2(String memo_2) {
		this.memo_2 = memo_2;
	}

	public String getMemo_3() {
		return memo_3;
	}

	public void setMemo_3(String memo_3) {
		this.memo_3 = memo_3;
	}

	public String getMemo_4() {
		return memo_4;
	}

	public void setMemo_4(String memo_4) {
		this.memo_4 = memo_4;
	}

	public String getCreate_person() {
		return create_person;
	}

	public void setCreate_person(String create_person) {
		this.create_person = create_person;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getModify_person() {
		return modify_person;
	}

	public void setModify_person(Integer modify_person) {
		this.modify_person = modify_person;
	}

	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

}
