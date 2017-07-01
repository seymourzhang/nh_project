package com.nh.ifarm.device.yingtong.entity;

public class YingtongSource {
//	起始符
//	length 4
	public String initStr;//4
//	帧类型
//	a) 需应答：本帧是否需要接收端返回应答帧。
//	b) 应答： 标识本帧是否为应答帧。
//	c) 保留。
//	d) 版本号：用于标识该帧的版本号，可为后面做兼容性功能使用
//	e) 加密类型
//	length 2
	public String sourceType;//2s
//	帧头加帧负荷的长度。
//	length 4
	public String sourceLength;//2s
	
//	上行帧时填充MagicMote 的唯一序列号。下行帧不填数据。
//	length 4
	public String magicMoteID;//4s
	
//	帧号用于标识帧的唯一性。
//	当主动发送一帧数据时，帧号应递增；当发送应答帧时，帧号应该填入需应答帧的帧号。。
//length 2
	public String sourceID;
	
//	包含整帧（帧头+帧负荷）crc 校验和，计算时本字段填充为0。源码请参考"crc 计算"这
//	一章节。
//	length 2
	public String checkCode ;
//	帧负荷内容
//	length  长度不定
	public String content;
	public String getInitStr() {
		return initStr;
	}
	public void setInitStr(String initStr) {
		this.initStr = initStr;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getSourceLength() {
		return sourceLength;
	}
	public void setSourceLength(String sourceLength) {
		this.sourceLength = sourceLength;
	}
	public String getMagicMoteID() {
		return magicMoteID;
	}
	public void setMagicMoteID(String magicMoteID) {
		this.magicMoteID = magicMoteID;
	}
	public String getSourceID() {
		return sourceID;
	}
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
