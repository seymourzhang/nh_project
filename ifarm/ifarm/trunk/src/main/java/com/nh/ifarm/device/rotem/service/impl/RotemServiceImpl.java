package com.nh.ifarm.device.rotem.service.impl;


import com.nh.ifarm.device.rotem.entity.RotemQuota;
import com.nh.ifarm.device.rotem.service.RotemService;
import com.nh.ifarm.device.service.impl.DeviceServiceImpl;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.PageData;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Rotem控制器服务类
 * Created by Raymon on 7/6/2016.
 */
@Service
public class RotemServiceImpl extends DeviceServiceImpl implements RotemService {

    /**
     * 构造函数
     */
    public RotemServiceImpl() {
    }

    /**
     * 获取数据对象
     * @return 数据对象
     */
    public PageData getData(){
        return demoDataForRotem();
    }

    /**
     * 更新至数据库
     */
    public void writeData(PageData pd){
        for(Object obj : pd.values()){
            RotemQuota rq = (RotemQuota)obj;
            PageData tmp = rotemQuotaToPageData(rq);
            try{
                List<PageData> list = (List<PageData>)dao.findForList("SDDeviceDataCurMapper.findById",tmp);
                if(list.size()>0) {
                    //移动数据至历史表
                    moveToHistory(tmp);
                    //更新已有数据
                    update(tmp);
                } else{
                    //插入新数据
                    insert(tmp);
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * 清空数据对象
     */
    public void clearData(){
    }

    /**
     * 引通指标对象转换成数据对象
     * @param rq 引通指标对象
     * @return 数据对象
     */
    PageData rotemQuotaToPageData(RotemQuota rq){
        PageData tmp = new PageData();
        try {
            tmp.put(rq.ID.getCode(),rq.ID.getValue());
            tmp.put(rq.DEVICE_ID.getCode(),rq.DEVICE_ID.getValue());
            tmp.put(rq.PORT_ID.getCode(),rq.PORT_ID.getValue());
            tmp.put(rq.DEVICE_TYPE.getCode(),rq.DEVICE_TYPE.getValue());
            tmp.put(rq.ALARM_CODE.getCode(),rq.ALARM_CODE.getValue());
            tmp.put(rq.SET_TEMP.getCode(),rq.SET_TEMP.getValue());
            tmp.put(rq.OUTSIDE_TEMP.getCode(),rq.OUTSIDE_TEMP.getValue());
            tmp.put(rq.AVG_TEMP.getCode(),rq.AVG_TEMP.getValue());
            tmp.put(rq.INSIDE_TEMP1.getCode(),rq.INSIDE_TEMP1.getValue());
            tmp.put(rq.INSIDE_TEMP2.getCode(),rq.INSIDE_TEMP2.getValue());
            tmp.put(rq.INSIDE_TEMP3.getCode(),rq.INSIDE_TEMP3.getValue());
            tmp.put(rq.INSIDE_TEMP4.getCode(),rq.INSIDE_TEMP4.getValue());
            tmp.put(rq.WATER_CONSUMPTION.getCode(),rq.WATER_CONSUMPTION.getValue());
            tmp.put(rq.HUMIDITY.getCode(),rq.HUMIDITY.getValue());
            tmp.put(rq.CO2.getCode(),rq.CO2.getValue());
            tmp.put(rq.NEGATIVE_PRESSURE.getCode(),rq.NEGATIVE_PRESSURE.getValue());
            tmp.put(rq.LARGER_FAN_1.getCode(),rq.LARGER_FAN_1.getValue());
            tmp.put(rq.LARGER_FAN_2.getCode(),rq.LARGER_FAN_2.getValue());
            tmp.put(rq.LARGER_FAN_3.getCode(),rq.LARGER_FAN_3.getValue());
            tmp.put(rq.SMALL_FAN_1.getCode(),rq.SMALL_FAN_1.getValue());
            tmp.put(rq.SMALL_FAN_2.getCode(),rq.SMALL_FAN_2.getValue());
            tmp.put(rq.SMALL_FAN_3.getCode(),rq.SMALL_FAN_3.getValue());
            tmp.put(rq.FENESTELLA.getCode(),rq.FENESTELLA.getValue());
            tmp.put(rq.HEATING_STATE.getCode(),rq.HEATING_STATE.getValue());
            tmp.put(rq.REFRIGERATION_STATE.getCode(),rq.REFRIGERATION_STATE.getValue());
            tmp.put(rq.SKATEBOARD.getCode(),rq.SKATEBOARD.getValue());
            tmp.put(rq.COLLECT_DATETIME.getCode(), Const.getSDF().parse(rq.COLLECT_DATETIME.getValue()));
            tmp.put(rq.SOURCE_CODE.getCode(),rq.SOURCE_CODE.getValue());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return tmp;
    }

    /**
     * 启动服务
     */
    public void start(){}



//
//	private static Logger mLogger =Logger.getLogger(RotemServer.class);
//
//
//
//	private static RotemServer mGateWayService = null;
//
//
//
//	public static RotemServer getInstance(){
//
//		if(mGateWayService == null){
//
//			mGateWayService = new RotemServer();
//
//		}
//
//		return mGateWayService;
//
//	}
//
//
//
//	public static void main(String[] args) throws JSONException {
//
//		RotemServer mGateWayService = RotemServer.getInstance();
//
//		/*
//
//		 *   通过 Get 方式 获取GateWay 的目标温度以及通风级别
//
//		*/
//
//		JSONArray tempArray = mGateWayService.getTempsAndMaxMinLevels();
//
//		mLogger.info(tempArray.toString());
//		/*
//
//		 *   通过 POST 方式 远程控制GateWay 的目标温度以及通风级别
//
//
//
//		String PostData = "[{\"id\":0,\"Tunnel\":\"39\",\"Target\":\"35\",\"Heat\":\"31\",\"MaxLevel\":\"3\",\"MinLevel\":\"1\",\"Day\":\"1\"},"
//
//						 + "{\"id\":1,\"Tunnel\":\"38\",\"Target\":\"33\",\"Heat\":\"30\",\"MaxLevel\":\"5\",\"MinLevel\":\"3\",\"Day\":\"7\"},"
//
//						 + "{\"id\":2,\"Tunnel\":\"35\",\"Target\":\"31\",\"Heat\":\"28\",\"MaxLevel\":\"7\",\"MinLevel\":\"4\",\"Day\":\"14\"},"
//
//						 + "{\"id\":3,\"Tunnel\":\"33\",\"Target\":\"29\",\"Heat\":\"25\",\"MaxLevel\":\"8\",\"MinLevel\":\"6\",\"Day\":\"21\"},"
//
//						 + "{\"id\":4,\"Tunnel\":\"31\",\"Target\":\"27\",\"Heat\":\"25\",\"MaxLevel\":\"11\",\"MinLevel\":\"9\",\"Day\":\"28\"},"
//
//						 + "{\"id\":5,\"Tunnel\":\"30\",\"Target\":\"25\",\"Heat\":\"22\",\"MaxLevel\":\"15\",\"MinLevel\":\"11\",\"Day\":\"35\"},"
//
//						 + "{\"id\":6,\"Tunnel\":\"30\",\"Target\":\"25\",\"Heat\":\"22\",\"MaxLevel\":\"20\",\"MinLevel\":\"12\",\"Day\":\"42\"},"
//
//						 + "{\"id\":7,\"Tunnel\":\"0\",\"Target\":\"0\",\"Heat\":\"0\",\"MaxLevel\":\"0\",\"MinLevel\":\"0\",\"Day\":\"0\"},"
//
//						 + "{\"id\":8,\"Tunnel\":\"0\",\"Target\":\"0\",\"Heat\":\"0\",\"MaxLevel\":\"0\",\"MinLevel\":\"0\",\"Day\":\"0\"},"
//
//						 + "{\"id\":9,\"Tunnel\":\"0\",\"Target\":\"0\",\"Heat\":\"0\",\"MaxLevel\":\"0\",\"MinLevel\":\"0\",\"Day\":\"0\"}]" ;
//
//
//
//		JSONArray mJSONArray = new JSONArray(PostData);
//
//		if(mGateWayService.setTempsAndMaxMinLevels(mJSONArray)){
//
//			mLogger.info("==== 成功 ====");
//
//		}else{
//
//			mLogger.info("==== 失败 ====");
//
//		};
//
//		 */
//
//	}
//
//
//
//	public boolean setTempsAndMaxMinLevels(JSONArray mTempLevels){
//
//		try {
//
//			if(mTempLevels == null ){
//
//				mLogger.info("TemperatureCurvem.JSONArray is null");
//
//				return false ;
//
//			}
//
//
//
//			Element MinMaxLevels = new Element("Properties");
//
//			Element TemperatureCurve = new Element("Properties");
//
//			for(int i = 0; i<mTempLevels.length(); i++){
//
//				Element tDay1 = new Element("Property");
//
//				tDay1.setAttribute("name","Day");
//
//				tDay1.setAttribute("value",mTempLevels.getJSONObject(i).getString("Day"));
//
//				MinMaxLevels.addContent(tDay1);
//
//
//
//				Element tDay2 = new Element("Property");
//
//				tDay2.setAttribute("name","Day");
//
//				tDay2.setAttribute("value",mTempLevels.getJSONObject(i).getString("Day"));
//
//
//
//				TemperatureCurve.addContent(tDay2);
//
//
//
//				Element tTarget = new Element("Property");
//
//				tTarget.setAttribute("name","Target");
//
//				tTarget.setAttribute("value",mTempLevels.getJSONObject(i).getString("Target"));
//
//				TemperatureCurve.addContent(tTarget);
//
//
//
//				Element tHeat = new Element("Property");
//
//				tHeat.setAttribute("name","Heat");
//
//				tHeat.setAttribute("value",mTempLevels.getJSONObject(i).getString("Heat"));
//
//				TemperatureCurve.addContent(tHeat);
//
//
//
//				Element tTunnel = new Element("Property");
//
//				tTunnel.setAttribute("name","Tunnel");
//
//				tTunnel.setAttribute("value",mTempLevels.getJSONObject(i).getString("Tunnel"));
//
//				TemperatureCurve.addContent(tTunnel);
//
//
//
//				Element tMinLevel = new Element("Property");
//
//				tMinLevel.setAttribute("name","MinLevel");
//
//				tMinLevel.setAttribute("value",mTempLevels.getJSONObject(i).getString("MinLevel"));
//
//				MinMaxLevels.addContent(tMinLevel);
//
//
//
//				Element tMaxLevel = new Element("Property");
//
//				tMaxLevel.setAttribute("name","MaxLevel");
//
//				tMaxLevel.setAttribute("value",mTempLevels.getJSONObject(i).getString("MaxLevel"));
//
//				MinMaxLevels.addContent(tMaxLevel);
//
//			}
//
//			// 提交 MinMaxLevels
//
//			Element Entity1 = new Element("Entity");
//
//			Entity1.setAttribute("name","MinMaxLevels");
//
//			Entity1.addContent(MinMaxLevels);
//
//			Element Entities1 = new Element("Entities");
//
//			Entities1.addContent(Entity1);
//
//			if(!postXmlData(Entities1)){
//
//				mLogger.info("post MinMaxLevels failed");
//
//				return false;
//
//			}
//
//			// 提交 TemperatureCurve
//
//			Element Entity2 = new Element("Entity");
//
//			Entity2.setAttribute("name","TemperatureCurve");
//
//			Entity2.addContent(TemperatureCurve);
//
//			Element Entities2 = new Element("Entities");
//
//			Entities2.addContent(Entity2);
//
//			if(!postXmlData(Entities2)){
//
//				mLogger.info("post TemperatureCurve failed");
//
//				return false;
//
//			}
//
//			return true;
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//			return false;
//
//		}
//
//	}
//
//
//
//	private boolean postXmlData(Element tEntities){
//
//		Element Entity = new Element("Entity");
//
//		Entity.setAttribute("name", "Controller");
//
//		Entity.setAttribute("ID", Constants_App.ControllerID);
//
//		Entity.setAttribute("allProperties", "False");
//
//		Entity.addContent(tEntities);
//
//		Element Entities = new Element("Entities");
//
//		Entities.addContent(Entity);
//
//		Element Query = new Element("Query");
//
//		Query.setAttribute("Type","SET");
//
//		Query.addContent(Entities);
//
//		Element Queries = new Element("Queries");
//
//		Queries.addContent(Query);
//
//		Element root = new Element("AC2000Request");
//
//		root.addContent(Queries);
//
//
//
//		Document doc = new Document();
//
//		doc.setRootElement(root);
//
//
//
//		XMLOutputter mXMLOutputter = new XMLOutputter();
//
//		Format format = Format.getPrettyFormat();
//
//		format.setEncoding("UTF-8");
//
//		format.setIndent("");
//
//		format.setLineSeparator("");
//
//
//
//		mXMLOutputter.setFormat(format);
//
//		String sendXML = mXMLOutputter.outputString(doc);
//
//		mLogger.info("sendXML==" + sendXML);
//
//
//
//	    String base64enSendXML = "";
//
//	    try {
//			base64enSendXML = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(sendXML.getBytes("utf-8"));
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//
//
//
//	    String jsonSendXML = "\""+ base64enSendXML +"\"";
//
//	    String requestURL =  Constants_App.GateWayServer + "API/XML/" + Constants_App.UnitNum;
//
//
//
//	    String resStr = HttpRequestUtil.reqPost(requestURL, jsonSendXML);
//
//
//
//	    return parseRes(resStr);
//
//	}
//
//
//
//	private boolean parseRes(String resStr){
//
//		boolean res = false;
//
//		try {
//
//
//
//			JSONObject jsonObject = new JSONObject(resStr);
//
//
//
//			Object ErrorsObj = jsonObject.get("Errors");
//
//
//
//			if(ErrorsObj.toString().toString().equals("null")){
//
//				res = true;
//
//			}else{
//
//				JSONArray tJSONArray = null ;
//
//				if(ErrorsObj instanceof JSONArray){
//
//					tJSONArray = (JSONArray)ErrorsObj;
//
//				}
//
//				if(tJSONArray != null && tJSONArray.length()>0){
//
//					JSONObject errorJson = tJSONArray.getJSONObject(0);
//
//					res = false;
//
//					mLogger.info("ErrorCode=" + errorJson.getString("ErrorCode")+ " ErrorMessage=" + errorJson.getString("ErrorMessage")  );
//
//				}
//
//			}
//
//
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//
//		return res;
//
//	}
//
//
//
//	public JSONArray getTempsAndMaxMinLevels(){
//
//		JSONArray resJsonArray = null;
//
//		String tGateWayServer = Constants_App.GateWayServer;
//
//		String tUnitNum = Constants_App.UnitNum;
//
//		String tControllerID = Constants_App.ControllerID;
//
//		try {
//
//			resJsonArray = new JSONArray();
//
//			JSONArray tempArray = getTemperatureCurve(tGateWayServer, tUnitNum, tControllerID);
//
//			JSONArray levelsArray = getMinMaxLevels(tGateWayServer, tUnitNum, tControllerID);
//
//
//
//			if(tempArray == null || levelsArray == null){
//
//				mLogger.info("TemperatureCurve or MinMaxLevels is null");
//
//				return null;
//
//			}
//
//			ArrayList<String> daysList = new ArrayList<String>();
//
//
//
//			daysList = getDayAges(daysList,tempArray);
//
//			daysList = getDayAges(daysList,levelsArray);
//
//
//
//			for(int i=0; i<daysList.size(); i++){
//
//				JSONObject tJobj = new JSONObject();
//
//				tJobj.put("Day", daysList.get(i));
//
//				boolean isExists = false;
//
//				for(int j = 0; j<tempArray.length(); j++){
//
//					if(tempArray.getJSONObject(j).getString("Day").equals(daysList.get(i))){
//
//						tJobj.put("Target", tempArray.getJSONObject(j).getString("Target"));
//
//						tJobj.put("Heat", tempArray.getJSONObject(j).getString("Heat"));
//
//						tJobj.put("Tunnel", tempArray.getJSONObject(j).getString("Tunnel"));
//
//						isExists = true;
//
//						break;
//
//					}
//
//				}
//
//				if(!isExists){
//
//					tJobj.put("Target", "0");
//
//					tJobj.put("Heat", "0");
//
//					tJobj.put("Tunnel", "0");
//
//				}
//
//				isExists =false;
//
//				for(int j = 0; j<levelsArray.length(); j++){
//
//					if(levelsArray.getJSONObject(j).getString("Day").equals(daysList.get(i))){
//
//						tJobj.put("MinLevel", levelsArray.getJSONObject(j).getString("MinLevel"));
//
//						tJobj.put("MaxLevel", levelsArray.getJSONObject(j).getString("MaxLevel"));
//
//						isExists = true;
//
//						break;
//
//					}
//
//				}
//
//				if(!isExists){
//
//					tJobj.put("MinLevel", "0");
//
//					tJobj.put("MaxLevel", "0");
//
//				}
//
//				tJobj.put("id", i);
//
//				resJsonArray.put(tJobj);
//
//			}
//
//			if(resJsonArray.length()<10){
//
//				int length = resJsonArray.length() ;
//
//				for(int i = 0; i<10-length; i++){
//
//					JSONObject tJobj = new JSONObject();
//
//					tJobj.put("Day", "0");
//
//					tJobj.put("Target", "0");
//
//					tJobj.put("Heat", "0");
//
//					tJobj.put("Tunnel", "0");
//
//					tJobj.put("MinLevel", "0");
//
//					tJobj.put("MaxLevel", "0");
//
//					tJobj.put("id", length + i);
//
//					resJsonArray.put(tJobj);
//
//				}
//
//			}
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//
//		return resJsonArray ;
//
//	}
//
//
//
//	private ArrayList<String> getDayAges(ArrayList<String> preparedList,JSONArray dataArrays) throws JSONException{
//
//		for(int j = 0; j<dataArrays.length(); j++){
//
//			String dayAge = dataArrays.getJSONObject(j).getString("Day");
//
//			if(!preparedList.contains(dayAge)){
//
//				preparedList.add(dayAge);
//
//			}
//
//		}
//
//		Collections.sort(preparedList, new Comparator<String>() {
//
//				public int compare(String o1, String o2) {
//
//					int n1 = Integer.parseInt(o1);
//
//					int n2 = Integer.parseInt(o2);
//
//					if(n1 > n2){
//
//						return 1;
//
//					}else if(n1 == n2){
//
//						return 0;
//
//					}else{
//
//						return -1;
//
//					}
//
//				}
//
//			});
//
//		if(preparedList.contains("0")){
//
//			preparedList.remove("0");
//
//			preparedList.add("0");
//
//		}
//
//		return preparedList;
//
//	}
//
//
//
//	public JSONArray getTemperatureCurve(String tGateWayServer,String tUnitNum,String tControllerID){
//
//
//
//		JSONArray tarJsonArray = new JSONArray();
//
//		try {
//
//			String resXML = getTableXML(tGateWayServer,tUnitNum,tControllerID,"TemperatureCurve");
//
//
//
//			if(resXML == null){
//
//				return null ;
//
//			}
//
//
//
//			SAXBuilder builder = new SAXBuilder();
//
//			Document doc = builder.build(new ByteArrayInputStream(resXML.getBytes())) ;
//
//			Element root = doc.getRootElement();
//
//			mLogger.debug("AC2000.Result:" + root.getAttributeValue("Result"));
//
//			// ControllerEntity
//
//			Element ControllerEntity = root.getChild("Entity") ;
//
//			mLogger.debug("ControllerEntity.name:" + ControllerEntity.getAttributeValue("name") + ";ControllerEntity.ID:" + ControllerEntity.getAttributeValue("ID"));
//
//			// 一级Entity
//
//			List<Element> EntityTypes = ControllerEntity.getChild("Entities").getChildren("Entity") ;
//
//			for (Iterator<Element> ite1 = EntityTypes.iterator(); ite1.hasNext();) {
//
//				Element EntityType = ite1.next();
//
//			    // 明细 Entity
//
//				mLogger.debug("EntityTypes.name:" + EntityType.getAttributeValue("name"));
//
//				List<Element> entities = EntityType.getChild("Entities").getChildren("Entity") ;
//
//
//
//			    for (Iterator<Element> ite2 = entities.iterator(); ite2.hasNext();) {
//
//			    	Element tEntity = ite2.next();
//
//			    	mLogger.debug("EntityType.ID:" + tEntity.getAttributeValue("ID"));
//
//			    	JSONObject tempJson = new JSONObject();
//
//
//
//			    	// 明细 Properties
//
//			    	List<Element> properties = tEntity.getChild("Properties").getChildren("Property");
//
//			    	for (Iterator<Element> ite3 = properties.iterator(); ite3.hasNext();) {
//
//			    		Element tProperty = ite3.next();
//
//			    		tempJson.put(tProperty.getAttributeValue("name"), tProperty.getAttributeValue("value"));
//
//			        }
//
//			    	tarJsonArray.put(tempJson);
//
//			    }
//
//			}
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//
//		return tarJsonArray;
//
//	}
//
//
//
//	public JSONArray getMinMaxLevels(String tGateWayServer,String tUnitNum,String tControllerID){
//
//
//
//		JSONArray tarJsonArray = new JSONArray();
//
//		try {
//
//			String resXML = getTableXML(tGateWayServer,tUnitNum,tControllerID,"MinMaxLevels");
//
//
//
//			if(resXML == null){
//
//				return null ;
//
//			}
//
//
//
//			SAXBuilder builder = new SAXBuilder();
//
//			Document doc = builder.build(new ByteArrayInputStream(resXML.getBytes())) ;
//
//			Element root = doc.getRootElement();
//
//			mLogger.debug("AC2000.Result:" + root.getAttributeValue("Result"));
//
//			// ControllerEntity
//
//			Element ControllerEntity = root.getChild("Entity") ;
//
//			mLogger.debug("ControllerEntity.name:" + ControllerEntity.getAttributeValue("name") + ";ControllerEntity.ID:" + ControllerEntity.getAttributeValue("ID"));
//
//			// 一级Entity
//
//			List<Element> EntityTypes = ControllerEntity.getChild("Entities").getChildren("Entity") ;
//
//			for (Iterator<Element> ite1 = EntityTypes.iterator(); ite1.hasNext();) {
//
//				Element EntityType = ite1.next();
//
//			    // 明细 Entity
//
//				mLogger.debug("EntityTypes.name:" + EntityType.getAttributeValue("name"));
//
//				List<Element> entities = EntityType.getChild("Entities").getChildren("Entity") ;
//
//
//
//			    for (Iterator<Element> ite2 = entities.iterator(); ite2.hasNext();) {
//
//			    	Element tEntity = ite2.next();
//
//			    	mLogger.debug("EntityType.ID:" + tEntity.getAttributeValue("ID"));
//
//			    	JSONObject tempJson = new JSONObject();
//
//
//
//			    	// 明细 Properties
//
//			    	List<Element> properties = tEntity.getChild("Properties").getChildren("Property");
//
//			    	for (Iterator<Element> ite3 = properties.iterator(); ite3.hasNext();) {
//
//			    		Element tProperty = ite3.next();
//
//			    		tempJson.put(tProperty.getAttributeValue("name"), tProperty.getAttributeValue("value"));
//
//			        }
//
//			    	tarJsonArray.put(tempJson);
//
//			    }
//
//			}
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//
//		return tarJsonArray;
//
//	}
//
//
//
//	private String getResXML(String resStr){
//
//		String xmlResponse = null;
//
//		try {
//
//			JSONObject jsonObject = new JSONObject(resStr);
//
//			if(jsonObject.has("Status") && jsonObject.getInt("Status") == 1){
//
//				xmlResponse = jsonObject.has("XMLResponse")?jsonObject.getString("XMLResponse"):"";
//
//				mLogger.debug("xmlResponse:"+xmlResponse);
//
//			}else{
//
//				mLogger.info("返回结果错误：Errors=" + (jsonObject.has("Errors")?jsonObject.get("Errors").toString() : "null"));
//
//			}
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//
//		return xmlResponse;
//
//	}
//
//
//
//	// Alarms/{pUnitNumber}/{pControllerNumber}
//
//	public String getAlarmsXML(String tGateWayServer,String tUnitNum,String tControllerID){
//
//
//
//		String requestURL = tGateWayServer + "API/Alarms/"+tUnitNum+"/"+tControllerID ;
//
//		mLogger.debug("Alarms：" + requestURL);
//
//		String resXML = HttpRequestUtil.reqGet(requestURL) ;
//
//		return getResXML(resXML);
//
//	}
//
//
//
//	// DashBoard/{pUnitNumber}/{pControllerNumber}
//
//	public String getDashboardXML(String tGateWayServer,String tUnitNum,String tControllerID){
//
//
//
//		String requestURL = tGateWayServer + "API/DashBoard/"+tUnitNum+"/"+tControllerID ;
//
//		mLogger.debug("DashBoard：" + requestURL);
//
//		String resXML = HttpRequestUtil.reqGet(requestURL) ;
//
//		return getResXML(resXML);
//
//	}
//
//
//
//	// Devices/{pUnitNumber}
//
//	public String getDevicesXML(String tGateWayServer,String tUnitNum){
//
//
//
//		String requestURL = tGateWayServer + "API/Devices/"+tUnitNum ;
//
//		mLogger.debug("Devices：" + requestURL);
//
//		String resXML = HttpRequestUtil.reqGet(requestURL) ;
//
//		return getResXML(resXML);
//
//	}
//
//
//
//	// Gateways
//
//	public String getGatewaysXML(String tGateWayServer){
//
//
//
//		String requestURL = tGateWayServer + "API/Gateways" ;
//
//		mLogger.info("Gateways：" + requestURL);
//
//		String resXML = HttpRequestUtil.reqGet(requestURL) ;
//
//		return getResXML(resXML);
//
//	}
//
//
//
//	// Table/{pUnitNumber}/{pControllerNumber}/{pTableName}
//
//	public String getTableXML(String tGateWayServer,String tUnitNum,String tControllerID,String tTableName){
//
//		String requestURL = tGateWayServer + "API/Table/"+tUnitNum+"/"+tControllerID+"/"+tTableName;
//
//		mLogger.info("Table：" + requestURL);
//
//		String resXML = HttpRequestUtil.reqGet(requestURL) ;
//
//		return getResXML(resXML);
//
//	}

}
