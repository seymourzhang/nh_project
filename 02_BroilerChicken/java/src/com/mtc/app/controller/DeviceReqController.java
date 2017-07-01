/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.mtc.app.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mtc.device.BbfarWiredManage;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mtc.app.biz.DeviceBbfReqManager;
import com.mtc.app.service.BaseQueryService;
import com.mtc.app.service.SBBbfarSercice;
import com.mtc.common.util.PubFun;
import com.mtc.entity.app.SBBbfar;
import com.mtc.entity.app.SBRotemNetData;

/**
 * @ClassName: DeviceReqController
 * @Description:
 * @Date 2016-1-4 下午5:45:57
 * @Author Shao Yao Yu
 * 
 */
@Controller
@RequestMapping("/sys/device")
public class DeviceReqController {

	private static Logger mLogger = Logger.getLogger(DeviceReqController.class);
	@Autowired
	private DeviceBbfReqManager mDeviceBbfReqManager;
	@Autowired
	private   SBBbfarSercice tSBBbfarSercice;
	@Autowired
	private BaseQueryService mBaseQueryService;

	@RequestMapping("/dataUploadBBF")
	public void dataUploadBBF(HttpServletRequest request,
			HttpServletResponse response) {
		mLogger.info("=====Now start executing DeviceReqController.dataUploadBBF");
		
		String dealRes = null;
		Date curDate = new Date();

		try {
			SBBbfar tSBBbfar = new SBBbfar();
			String keyid = request.getParameter("keyid");
			
			mLogger.info("KeyId = " + keyid + "  CurJavaThreadId == " + Thread.currentThread().getId() + " " + PubFun.getCurrentDate() + " " + PubFun.getCurrentTime());
			
			String t1 = request.getParameter("t1");
			String t2 = request.getParameter("t2");
			String t3 = request.getParameter("t3");
			String t4 = request.getParameter("t4");
			String t5 = request.getParameter("t5");
			String t6 = request.getParameter("t6");
			String h1 = request.getParameter("h1");
			String h2 = request.getParameter("h2");
			String p = request.getParameter("p");
			String station = request.getParameter("station");
			String gps = request.getParameter("gps");
			String help = request.getParameter("help");
			String dateTime = request.getParameter("dateTime");
			String IMEI = request.getParameter("IMEI");
			String ICCID = request.getParameter("ICCID");
			if (keyid != null) {
				tSBBbfar.setKeyid(keyid);
				if (t1 != null) {
					tSBBbfar.setT1O(t1);
				}
				if (t2 != null) {
					tSBBbfar.setT2O(t2);
				}
				if (t3 != null) {
					tSBBbfar.setT3O(t3);
				}
				if (t4 != null) {
					tSBBbfar.setT4O(t4);
				}
				if (t5 != null) {
					tSBBbfar.setT5O(t5);
				}
				if (t6 != null) {
					tSBBbfar.setT6O(t6);
				}
				if (h1 != null) {
					tSBBbfar.setH1O(h1);
				}
				if (h2 != null) {
					tSBBbfar.setH2O(h2);
				}
				if (p != null) {
					tSBBbfar.setP(p);
				}
				if (station != null) {
					tSBBbfar.setStation(station);
				}
				if (gps != null) {
					tSBBbfar.setGps(gps);
				}
				if (help != null) {
					tSBBbfar.setHelp(help);
				}
				if (dateTime != null) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					java.util.Date date = sdf.parse(dateTime);
					tSBBbfar.setDateTime(date);
				} else {
					tSBBbfar.setDateTime(curDate);
				}
				
				
				if(!PubFun.isNull(IMEI)){
					tSBBbfar.setBak1(IMEI);
				}
				if(!PubFun.isNull(ICCID)){
					tSBBbfar.setBak2(ICCID);
				}

				tSBBbfar = BbfarWiredManage.getInstance().dealData(tSBBbfar);
				
				HashMap<String, Object> mParas = new HashMap<String, Object>();
				mParas.put("SBBbfar", tSBBbfar);
				mDeviceBbfReqManager.saveSbbbfar(mParas);
				
				mDeviceBbfReqManager.dealBBFarData(tSBBbfar);

				mLogger.info("KeyId = " + keyid + " end CurJavaThreadId == " + Thread.currentThread().getId() + " " + PubFun.getCurrentDate() + " " + PubFun.getCurrentTime());
				dealRes = "MTC response: upLoad Data Sucess!";
			} else {
				mLogger.info("设备ID为空");
				dealRes = "设备数据为空，请输入正确数据。";
			}
		} catch (Throwable e) {
			e.printStackTrace();
			dealRes = "服务程序错误，请联系管理员。";
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json;charset=utf-8");
		try {
			mLogger.info("Result:" + dealRes);
			response.getWriter().write(dealRes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mLogger.info("=====Now end executing DeviceReqController.dataUploadBBF");
	}

	@RequestMapping("/showData")
	public void showData(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String tRes = "";
		mLogger.info("=====Now start executing DeviceReqController.showData");
		try {
			String tSQL = "SELECT keyid,date_format(date_time,'%Y-%m-%d %H:%i:%s') as date_time,t1,t2,t3,t4,t5,t6,h1,h2,p,station,gps,bak1,bak2 from s_b_bbfar order by id desc  LIMIT 100 ";
			mLogger.info("DeviceReqController.showData.sql"+tSQL);
			List aList = mBaseQueryService.selectMapByAny(tSQL);
			ObjectMapper mapper = new ObjectMapper();
			tRes = mapper.writeValueAsString(aList);
			System.out.println("tRes=" + tRes);
			/** 业务处理结束 **/
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(tRes);
		mLogger.info("=====Now end executing DeviceReqController.showData");
	}
	
	@RequestMapping("/showRotemNetData")
	public void showRotemNetData(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String tRes = "";
		mLogger.info("=====Now start executing DeviceReqController.showRotemNetData");
		try {
			String tSQL = "SELECT date_format(make_time,'%Y-%m-%d %H:%i:%s') as make_time, col_a, col_b, col_c, col_d, col_e, col_f, col_g, col_h, col_i, col_j, col_k, col_l, col_m, col_n, col_o, col_p, col_q, col_r, col_s, col_t, col_u, col_v, col_w, col_x, col_y, col_z, col_aa, col_ab, col_ac, col_ad, col_ae, col_af, col_ag, col_ah, col_ai, col_aj, col_ak, col_al, col_am, col_an, col_ao, col_ap, col_aq, col_ar, col_as, col_at, col_au, col_av, col_aw, col_ax from s_b_rotem_net_data ORDER BY id desc LIMIT 100 ";
			mLogger.info("DeviceReqController.showData.sql"+tSQL);
			List aList = mBaseQueryService.selectMapByAny(tSQL);
			ObjectMapper mapper = new ObjectMapper();
			tRes = mapper.writeValueAsString(aList);
			System.out.println("tRes=" + tRes);
			/** 业务处理结束 **/
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(tRes);
		mLogger.info("=====Now end executing DeviceReqController.showRotemNetData");
	}
	
	@RequestMapping("/rotemNetUpload")
	public void rotemNetUpload(HttpServletRequest request,
			HttpServletResponse response) {
		mLogger.info("=====Now start executing RotemNetReqController.rotemNetUpload");
		
		String dealRes = null;
		Date curDate = new Date();
		try {
			int columnLength = 50; 
			boolean isNull = false;
			
			SBRotemNetData tSBRotemNetData = new SBRotemNetData();
			Class SBRotemNetDataClass = SBRotemNetData.class; 
			Method SBRotemNetDataSetMethod = null;
			for(int i = 1; i <= columnLength; i++ ){
				String paraName = PubFun.excelColIndexToStr(i);
				String paraCol = "";
				if(paraName.length()>1){
					paraCol = paraName.substring(0, 1) + paraName.substring(1).toLowerCase();
				}else{
					paraCol = paraName ;
				}
				SBRotemNetDataSetMethod = SBRotemNetDataClass.getDeclaredMethod("setCol" + paraCol, String.class);
				String paraValue = request.getParameter(paraName);
				if(!StringUtils.isEmpty(paraValue)){
					paraValue = URLDecoder.decode(paraValue,"UTF-8");
					SBRotemNetDataSetMethod.invoke(tSBRotemNetData, paraValue);
					isNull = true;
				}
				mLogger.info("excel." + paraName + "=" + paraValue);
			}
			if(isNull){
				HashMap<String, Object> mParas = new HashMap<String, Object>();
				tSBRotemNetData.setMakeTime(new Date());
				mParas.put("SBRotemNetData", tSBRotemNetData);
				mDeviceBbfReqManager.saveSBRotemNetData(mParas);
				dealRes = "Success，数据已存储。";
			}else{
				dealRes = "请求无合法数据，请重新上传!";
			}
			
			
		} catch (Throwable e) {
			e.printStackTrace();
			dealRes = "服务程序错误，请联系管理员。";
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json;charset=utf-8");
		try {
			mLogger.info("Result:" + dealRes);
			response.getWriter().write(dealRes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mLogger.info("=====Now end executing RotemNetReqController.rotemNetUpload");
	}
}
