/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.mtc.device.bbfar.common;

import com.mtc.device.yincomm.common.ByteNumUtil;
import com.mtc.device.yincomm.common.StringHexUtil;

/**
 * @ClassName: BBFarUtil
 * @Description: 
 * @Date 2016年11月18日 上午11:39:12
 * @Author Yin Guo Xiang
 * 
 */
public class BBFarUtil {
	
	private byte uchCRCHi = (byte) 0xFF;  
	private byte uchCRCLo = (byte) 0xFF;
	
	private static byte[] auchCRCHi = { 0x00, (byte) 0xC1, (byte) 0x81,    (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,    (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,    (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,    (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81,    (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,    (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01,    (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,    (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81,    (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,    (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01,    (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,    (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81,    (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,    (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00,    (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,    (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81,    (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,    (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,    (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,    (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80,    (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,    (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01,    (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,    (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81,    (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,    (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,    (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,    (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80,    (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,    (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01,    (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,    (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81,    (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,    (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,    (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,    (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81,    (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,    (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,    (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,    (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80,    (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,    (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,    (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,    (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80,    (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,    (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00,    (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,    (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81,    (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,(byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,    (byte) 0xC1, (byte) 0x81, (byte) 0x40 };    private static byte[] auchCRCLo = { (byte) 0x00, (byte) 0xC0, (byte) 0xC1,    (byte) 0x01, (byte) 0xC3, (byte) 0x03, (byte) 0x02, (byte) 0xC2,    (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05,    (byte) 0xC5, (byte) 0xC4, (byte) 0x04, (byte) 0xCC, (byte) 0x0C,    (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF, (byte) 0xCE,    (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B,    (byte) 0xC9, (byte) 0x09, (byte) 0x08, (byte) 0xC8, (byte) 0xD8,    (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB,    (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF,    (byte) 0x1F, (byte) 0xDD, (byte) 0x1D, (byte) 0x1C, (byte) 0xDC,    (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7,    (byte) 0x17, (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12,    (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1, (byte) 0xD0,    (byte) 0x10, (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1,    (byte) 0x33, (byte) 0xF3, (byte) 0xF2, (byte) 0x32, (byte) 0x36,    (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35,    (byte) 0x34, (byte) 0xF4, (byte) 0x3C, (byte) 0xFC, (byte) 0xFD,    (byte) 0x3D, (byte) 0xFF, (byte) 0x3F, (byte) 0x3E, (byte) 0xFE,    (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39,    (byte) 0xF9, (byte) 0xF8, (byte) 0x38, (byte) 0x28, (byte) 0xE8,    (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B, (byte) 0x2A,    (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF,    (byte) 0x2D, (byte) 0xED, (byte) 0xEC, (byte) 0x2C, (byte) 0xE4,    (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7,    (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3,    (byte) 0x23, (byte) 0xE1, (byte) 0x21, (byte) 0x20, (byte) 0xE0,    (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63,    (byte) 0xA3, (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6,    (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65, (byte) 0x64,    (byte) 0xA4, (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D,    (byte) 0xAF, (byte) 0x6F, (byte) 0x6E, (byte) 0xAE, (byte) 0xAA,    (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9,    (byte) 0xA8, (byte) 0x68, (byte) 0x78, (byte) 0xB8, (byte) 0xB9,    (byte) 0x79, (byte) 0xBB, (byte) 0x7B, (byte) 0x7A, (byte) 0xBA,    (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D,    (byte) 0xBD, (byte) 0xBC, (byte) 0x7C, (byte) 0xB4, (byte) 0x74,    (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7, (byte) 0xB6,    (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73,    (byte) 0xB1, (byte) 0x71, (byte) 0x70, (byte) 0xB0, (byte) 0x50,    (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53,    (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57,    (byte) 0x97, (byte) 0x55, (byte) 0x95, (byte) 0x94, (byte) 0x54,    (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F,    (byte) 0x9F, (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A,    (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59, (byte) 0x58,    (byte) 0x98, (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89,    (byte) 0x4B, (byte) 0x8B, (byte) 0x8A, (byte) 0x4A, (byte) 0x4E,    (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D,    (byte) 0x4C, (byte) 0x8C, (byte) 0x44, (byte) 0x84, (byte) 0x85,    (byte) 0x45, (byte) 0x87, (byte) 0x47, (byte) 0x46, (byte) 0x86,    (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41,    (byte) 0x81, (byte) 0x80, (byte) 0x40 };
	private int value = 0;
	
	private BBFarUtil() {
		value = 0;       
	}
	private void update(byte[] puchMsg, int usDataLen) {
		int uIndex;   
		for (int i = 0; i < usDataLen; i++) {
			uIndex = (uchCRCHi ^ puchMsg[i]) & 0xff;      
			uchCRCHi = (byte) (uchCRCLo ^ auchCRCHi[uIndex]);    
			uchCRCLo = auchCRCLo[uIndex];   
		}     
		value = ((((int) uchCRCHi) << 8 | (((int) uchCRCLo) & 0xff))) & 0xffff;
		return;  
	}   
	private int getValue() {   
		return value;  
	} 
	private static byte[] HexString2Buf(String src) {   
		int len = src.length();   
		byte[] ret = new byte[len / 2+2];   
		byte[] tmp = src.getBytes();   
		for (int i = 0; i < len; i += 2) {    
			ret[i / 2] = uniteBytes(tmp[i], tmp[i + 1]);   
		}   
		return ret;  
	}
	private static byte uniteBytes(byte src0, byte src1) {   
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))    
					.byteValue();   
		_b0 = (byte) (_b0 << 4);   
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))     
					.byteValue();   
		byte ret = (byte) (_b0 ^ _b1);   
		return ret;  
	}
	
	public static byte[] getSendBuf(String toSend){
		byte[] bb = HexString2Buf(toSend);   
		BBFarUtil crc16 = new BBFarUtil();   
		crc16.update(bb, bb.length-2);   
		int ri = crc16.getValue();    
		
		byte[] res = new byte[2];
		res[0] = (byte) (0xff & ri);
		res[1] = (byte) ((0xff00 & ri) >> 8);  
		return res;  
	}
	
	/**
	 * 字符串转化成byte数组，笔笔发专用，其它可能不适用
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static byte[] transformHexStr(String sourceStr) {
		byte[] resByteArray = null;
		try {
			char[] tempChar = sourceStr.toCharArray();
			resByteArray = new byte[tempChar.length];
			for(int i = 0; i < tempChar.length; i++){
				resByteArray[i] = (byte)(tempChar[i] & 0xFF);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resByteArray;  
	}
	
	/**
	 * 16进制字符串,笔笔发专用，其它可能不适用
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static String parseFromHexStr(byte[] tByteArray) {
		StringBuffer resStr = new StringBuffer();
		try {
			for(int i = 0; i < tByteArray.length; i++){
				resStr.append((char)tByteArray[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resStr.toString();
	}
	
	/**
	 * 得到 CRC 校验值
	 * @param sourceHexStr
	 * @return
	 */
	public static int getCRCValue(String sourceHexStr){
		int crcValue = 0;
		try {
			byte[] sbuf = BBFarUtil.getSendBuf(sourceHexStr);
			byte [] tTempStrLength = new byte[4];
			tTempStrLength[0] = (byte)0;
			tTempStrLength[1] = (byte)0;
			tTempStrLength[2] = sbuf[0];
			tTempStrLength[3] = sbuf[1];
			crcValue = ByteNumUtil.bytesToInt(tTempStrLength);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return crcValue;
	}
	
	public static void main(String[] args) {
		// 发送
		String sourceStr = "keyid=f52800840284c4&IMEI=860111020942342&ICCID=898602B3131650063490&t1=23.1&t2=0.0&t3=0.0&t4=0.0&t5=0.0&t6=0.0&h1=&h2=&p=1&station=9b82;1819;460;00&CSQ=29&DBG=4536;4536;4536;10000&" ;
		System.out.println("sourceStr：" + sourceStr);
		String sourceStrHex = StringHexUtil.bytes2HexString(transformHexStr(sourceStr));
		System.out.println("sourceStrHex：" + sourceStrHex);
		sourceStr = sourceStr + "CRC="+getCRCValue(sourceStrHex);
		System.out.println("sourceStr：" + sourceStr);
		sourceStrHex = StringHexUtil.bytes2HexString(transformHexStr(sourceStr));
		System.out.println("sourceStrHex：" + sourceStrHex);
		
		// 解析
		String receiveStr = sourceStrHex;
		byte[] tByteArray = StringHexUtil.hexString2Bytes(receiveStr);
		String parseStr = parseFromHexStr(tByteArray);
		System.out.println("parseStr：" + parseStr);
		
	}
}
