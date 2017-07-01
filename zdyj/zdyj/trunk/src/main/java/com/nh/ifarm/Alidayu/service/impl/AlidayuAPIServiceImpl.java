/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.nh.ifarm.Alidayu.service.impl;

import com.nh.ifarm.Alidayu.entity.SLDayuTtsLog;
import com.nh.ifarm.Alidayu.entity.mapper.SLDayuTtsLogMapper;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcTtsNumSinglecallRequest;
import com.taobao.api.response.AlibabaAliqinFcTtsNumSinglecallResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.ResourceBundle;


/**
 * @ClassName: AlidayuAPIServiceImpl
 * @Description: 语音通知拨打服务。包含拨打方法，同时将拨打记录和拨打内容入库。拨打方法返回每次拨打的所分配的唯一ID。
 * 
 */
@Service
public class AlidayuAPIServiceImpl {
	
	private static Logger mLogger =Logger.getLogger(AlidayuAPIServiceImpl.class);

	private String appKey = getPropertyValue("appKey");
	private String appSecret = getPropertyValue("appSecret");
	private String showNum = getPropertyValue("showNum");
	private String extend = getPropertyValue("extend");
	private String callNumUrl = getPropertyValue("callNumUrl");

	@Autowired
	private SLDayuTtsLogMapper tSLDayuTtsLogMapper;

	private ResourceBundle conf = null;
	public  String getPropertyValue(String keyName){
		if(conf == null){
			conf= ResourceBundle.getBundle("pro/alidayu");
		}
		String value= conf.getString(keyName);
		return value;
	}
	
	private TaobaoClient client;

	private AlibabaAliqinFcTtsNumSinglecallRequest req;

	/**
	 *  拨打语音电话
	 */
	public JSONObject ttsNumSingleCell(String callNum,String ttsParams,String ttsCode){
		
		return ttsNumSingleCell(callNum, ttsParams, ttsCode, showNum, extend);
		
	}

	
	/**
	 *  拨打语音电话
	 * @param callNum 要拨打的电话号码
	 * @param ttsParams 拨打的参数json串
	 * @param ttsCode 模板ID
	 * @param showNum 拨打显示的号码
	 * @param extend 扩展字段
	 * @return {result:Succ/Fail,detail:null}
	 */
	public JSONObject ttsNumSingleCell(String callNum,String ttsParams,String ttsCode,String showNum,String extend){
		JSONObject returnJson = new JSONObject();
		try{
			if(client == null){
				client = new DefaultTaobaoClient(callNumUrl, appKey, appSecret);
			}
			if(req == null){
				req = new AlibabaAliqinFcTtsNumSinglecallRequest();
			}
			/**
			 * 公共回传参数，在“消息返回”中会透传回该参数；
			 * 举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，
			 * 用户可以根据该会员ID识别是哪位会员使用了你的应用
			 */
			req.setExtend(extend);
			/***
			 * 文本转语音（TTS）模板变量，传参规则{"key"："value"}，
			 * key的名字须和TTS模板中的变量名一致，多个变量之间以逗号隔开，
			 * 示例：{"name":"xiaoming","code":"1234"}
			 */
			req.setTtsParamString(ttsParams);
			/**
			 * 被叫号码，支持国内手机号与固话号码,格式如下057188773344,13911112222,4001112222,95500
			 */
			req.setCalledNum(callNum);
			/**
			 * 被叫号显，传入的显示号码必须是阿里大鱼“管理中心-号码管理”中申请或购买的号码
			 */
			req.setCalledShowNum(showNum);
			/**
			 *  TTS模板ID，传入的模板必须是在阿里大鱼“管理中心-语音TTS模板管理”中的可用模板
			 */
			req.setTtsCode(ttsCode);
			AlibabaAliqinFcTtsNumSinglecallResponse rsp = client.execute(req);

			/**
			 *   rsp.getBody() 的示例
			 *  success: {"alibaba_aliqin_fc_tts_num_singlecall_response":{"result":{"err_code":"0","model":"101679577699^100221309814","success":true},"request_id":"qm2xt3au7037"}}
			 *  fail:{"error_response":{"code":15,"msg":"Remote service error","sub_code":"isv.TTS_TEMPLATE_ILLEGAL","sub_msg":"未找到审核通过的文本转语音模板,ttsCode=TTS_96056210,partnerId=99109010036","request_id":"15q04lulip32i"}}
			 */

			String resultStr = rsp.getBody();
			mLogger.info("AliDayuResponse----- " + resultStr);
			JSONObject bodyJson = new JSONObject(resultStr);
			if(bodyJson.has("error_response")){
				returnJson.put("result","Fail");
				returnJson.put("detail",resultStr);
			}else{
				JSONObject resultJson = bodyJson.getJSONObject("alibaba_aliqin_fc_tts_num_singlecall_response").getJSONObject("result");
				if(resultJson.optBoolean("success")){
					returnJson.put("result","Succ");
					returnJson.put("detail","null");
				}else{
					returnJson.put("result","Fail");
					returnJson.put("detail",resultStr);
				}

				SLDayuTtsLog tSLDayuTtsLog = new SLDayuTtsLog();
				tSLDayuTtsLog.setBizId(resultJson.optString("model"));
				tSLDayuTtsLog.setCalledNum(callNum);
				tSLDayuTtsLog.setTempId(ttsCode);
				tSLDayuTtsLog.setTempParam(ttsParams);
				tSLDayuTtsLog.setCalledTime(new Date());
				tSLDayuTtsLog.setSubCode(resultJson.optString("err_code"));
				tSLDayuTtsLog.setRequestId(bodyJson.getJSONObject("alibaba_aliqin_fc_tts_num_singlecall_response").optString("request_id"));
				tSLDayuTtsLog.setExtend(extend);
				tSLDayuTtsLog.setStatusCode("00");

				returnJson.put("bizId",tSLDayuTtsLog.getBizId());

				tSLDayuTtsLog.setCreateTime(new Date());
				tSLDayuTtsLog.setModifyTime(new Date());

				tSLDayuTtsLogMapper.insert(tSLDayuTtsLog);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return returnJson;
	}
}