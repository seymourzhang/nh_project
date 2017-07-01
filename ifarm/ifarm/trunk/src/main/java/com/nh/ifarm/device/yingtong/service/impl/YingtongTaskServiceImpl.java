package com.nh.ifarm.device.yingtong.service.impl;

import com.nh.ifarm.device.yingtong.common.ByteNumUtil;
import com.nh.ifarm.device.yingtong.common.CRC16_Modbus;
import com.nh.ifarm.device.yingtong.common.StringHexUtil;
import com.nh.ifarm.device.yingtong.entity.YingtongControl;
import com.nh.ifarm.device.yingtong.entity.YingtongQuota;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.DateUtil;
import com.nh.ifarm.util.common.Logger;
import com.nh.ifarm.util.common.PageData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * 引通控制器任务服务类
 * Created by Raymon on 7/6/2016.
 */

public class YingtongTaskServiceImpl implements Runnable {
	
//	 @Autowired
//	 private  YingtongServiceImpl impl;


	
    private YingtongControl ytc;

    private static Logger mLogger =Logger.getLogger(YingtongTaskServiceImpl.class);

    private static String VALUE_TYPE_FLOAT = "FLOAT";
    private static String VALUE_TYPE_INT = "INT";
    private static String VALUE_TYPE_STRING = "STRING";
    private static String VALUE_TYPE_NULL = "NULL";

    private static String DATA_TYPE_NULL = "NULL";
    private static String DATA_TYPE_TEMP = "TEMP";
    private static String DATA_TYPE_HUMI = "HUMI";

    private static byte[] HEADBYTE = {0x7B,0x3A,0x29,0x7D};

    private static byte TYPE_DATA = 0x01; // 主动上报数据
    private static byte TYPE_KEEPALIVE = 0x02; //设备信息查询
    private static byte TYPE_ACK = 0x03; //时间同步

    static PageData rtPd = new PageData();

//	private SBYincommManager tSBYincommManager;
//
//	public SBYincommManager gettSBYincommManager() {
//		return tSBYincommManager;
//	}
//
//	public void settSBYincommManager(SBYincommManager tSBYincommManager) {
//		this.tSBYincommManager = tSBYincommManager;
//	}

    private Socket socket = null;

    public void setSocket(Socket socket ){
        this.socket =socket;
    }

    public YingtongTaskServiceImpl(){
    }

    @Override
    public void run() {
        try {
            String socketTaskName = Const.getSDF().format(new Date()) ;
            mLogger.info("YingtongTaskServiceImpl("+socketTaskName+") start ,线程号====" + Thread.currentThread());

            int dataCount = 1;
            InputStream in = socket.getInputStream();
            ByteArrayOutputStream bo = null;
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length=in.read(buffer))!=-1) {
                bo = new ByteArrayOutputStream();
                bo.write(buffer, 0, length);
                long longtime = System.currentTimeMillis();
                byte[] res = bo.toByteArray();

                mLogger.info(Const.getSDF().format(new Date(longtime))+"接收帧"+dataCount+"："+ StringHexUtil.bytes2HexString(res));

                byte[] response = dealDatas(res);

                if(response != null){
                    mLogger.info(Const.getSDF().format(new Date(longtime))+"确认帧"+dataCount+"："+ StringHexUtil.bytes2HexString(response));
                    OutputStream tOutputStream = socket.getOutputStream();
                    tOutputStream.write(response);
                    tOutputStream.flush();
                }

                mLogger.info("");
                dataCount++;
            }
            mLogger.info("YingtongTaskServiceImpl("+socketTaskName+") end   ,线程号====" + Thread.currentThread());
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte [] dealDatas(byte[] datas){
        byte [] returnData = null;
        //调试用模拟引通数据包
//        String res = "7B3A297D88010058000000040004AE8305E60000000405E70002041045D53E29064057679C7305E60000000405E70003041AC61C3C00064057679C7305E60000000407D3000507D4407E9868041045D53E29041AC61C3C00";
//        datas = StringHexUtil.hexString2Bytes(res);
        if(datas == null){
            mLogger.info("Error：数据为空。");
            return returnData;
        }
        if (datas.length<16) {
            mLogger.info("Error：数据长度小于16。");
            return returnData;
        }


        // 校验起始符是否正确 datas[0-3]
        if (datas[0] != HEADBYTE[0] || datas[1] != HEADBYTE[1] || datas[2] != HEADBYTE[2] || datas[3] != HEADBYTE[3]) {
            mLogger.info("Error：起始符有误。");
            return returnData;
        }
        // 帧类型  datas[4-5]
        String messageStr1 = StringHexUtil.b2BS(datas[4]);
        String messageStr2 = StringHexUtil.b2BS(datas[5]);

        String versionNo = messageStr1.substring(3, 6);

        // 是否需要应答帧
        boolean needreturn = messageStr1.substring(0, 1).equals("1")?true:false;
        //0-无加密  1-AES_128  2-AES_196   3-AES_256
        String aesType = messageStr1.substring(6, 8);
//		mLogger.info("aesType："+aesType);
        // 1-主动上报数据   2-设备信息查询   3-时间同步
        int messageType = datas[5];
//		mLogger.info("messageType："+messageType);

        // 校验数据的实际长度
        byte [] temp1 = {(byte)0,(byte)0,datas[6],datas[7]};
        if(ByteNumUtil.bytesToInt(temp1) != datas.length){
            mLogger.info("Error：数据标识长度与实际长度不符。");
            return returnData;
        }

        // 设备序列号
        byte [] temp2 = {datas[8],datas[9],datas[10],datas[11]};
        String deviceSN = StringHexUtil.bytes2HexString(temp2);
//		mLogger.info("DeviceSN："+ deviceSN);

        // 数据序列号
        byte [] temp3 = {(byte)0,(byte)0,datas[12],datas[13]};
        int dataSN = ByteNumUtil.bytesToInt(temp3);
        mLogger.info("数据帧序列号："+ dataSN);

        // 数据CRC验证
        byte [] temp4 = datas.clone();
        temp4[14] = 0;
        temp4[15] = 0;
        byte[] crcCal = CRC16_Modbus.getSendBuf(StringHexUtil.bytes2HexString(temp4));

        if(crcCal[0] != datas[14] || crcCal[1] != datas[15]){
            mLogger.info("Error：CRC验证失败。");
            return returnData;
        }

        int loopId = 1;
        int IdBit = 0;

        // temp-温度  humi-湿度  time-时间同步


        boolean humiFlag = false;

        Float dataValue = 0f;
        int dataId = 0;
        long sensorSN = 0;
        String sensorSTR = null;
        Date dataDate = null;

        while((16 + IdBit)<datas.length){
            byte [] temp5 = {(byte)0,(byte)0,datas[16+IdBit],datas[16+IdBit+1]};

            int tId = ByteNumUtil.bytesToInt(temp5);

            String tName = "";
            String tValueType = "";
            int tValueLength = 0;
            String precision = "";
            if(tId == 1000){
                tName = "温度";
                tValueType = VALUE_TYPE_FLOAT;
                tValueLength = 4;
                precision = "0.1";
                if(!humiFlag){
                    dataId = tId;
                }
            }else if(tId == 1005){
                tName = "室外温度";
                tValueType = VALUE_TYPE_FLOAT;
                tValueLength = 4;
                precision = "0.1";
            }else if(tId == 1010){
                tName = "湿度";
                tValueType = VALUE_TYPE_FLOAT;
                tValueLength = 4;
                precision = "0.1";

                humiFlag = true;
                dataId = tId;
            }else if(tId == 1015){
                tName = "室外湿度";
                tValueType = VALUE_TYPE_FLOAT;
                tValueLength = 4;
                precision = "0.1";
            }else if(tId == 1020){
                tName = "氨气";
                tValueType = VALUE_TYPE_FLOAT;
                tValueLength = 4;
                precision = "0.1";
            }else if(tId == 1030){
                tName = "硫化氢";
                tValueType = VALUE_TYPE_FLOAT;
                tValueLength = 4;
                precision = "0.1";
            }else if(tId == 1040){
                tName = "二氧化碳";
                tValueType = VALUE_TYPE_FLOAT;
                tValueLength = 4;
                precision = "0.1";
            }else if(tId == 1050){
                tName = "气压";
                tValueType = VALUE_TYPE_FLOAT;
                tValueLength = 4;
                precision = "0.1";
            }else if(tId == 1060){
                tName = "牲畜体温";
                tValueType = VALUE_TYPE_FLOAT;
                tValueLength = 4;
                precision = "0.1";
            }else if(tId == 1500){
                tName = "资产类型";
                tValueType = VALUE_TYPE_INT;
                tValueLength = 4;
            }else if(tId == 1510){
                tName = "MagicMote序列号";
                tValueType = VALUE_TYPE_INT;
                tValueLength = 4;
                init();
                //loyesin
            }else if(tId == 1511){
                tName = "接口编号";
                tValueType = VALUE_TYPE_INT;
                tValueLength = 2;
                //loyesin
            }else if(tId == 1514){
                tName = "传感器序列号";
                tValueType = VALUE_TYPE_INT;
                tValueLength = 8;
            }else if(tId == 1515){
                tName = "耳标序列号";
                tValueType = VALUE_TYPE_INT;
                tValueLength = 4;
            }else if(tId == 1600){
                tName = "时间戳";
                tValueType = VALUE_TYPE_INT;
                tValueLength = 4;
                //loyesin
            }else if(tId == 2003){
                tName = "MagicMote供电状态";
                tValueType = VALUE_TYPE_INT;
                tValueLength = 2;
            }else if(tId == 2004){
                tName = "电池电压";
                tValueType = VALUE_TYPE_FLOAT;
                tValueLength = 4;
                precision = "0.01";
            }else if(tId == 2006){
                tName = "运行时间";
                tValueType = VALUE_TYPE_INT;
                tValueLength = 4;
            }else if(tId == 2007){
                tName = "软件版本";
                tValueType = VALUE_TYPE_STRING;
                tValueLength = 0;
            }else if(tId == 2008){
                tName = "硬件版本";
                tValueType = VALUE_TYPE_STRING;
                tValueLength = 0;
            }else{
                mLogger.info("Error：发现未知的数据ID类型。ID="+tId);
                return returnData = genResponseByte(messageType,temp3);
            }
            IdBit += 2;

            if(tValueLength == 0){
                if(tValueType.equals(VALUE_TYPE_STRING)){
                    byte [] tTempStrLength = new byte[2];
                    tTempStrLength[0] = datas[16+IdBit];
                    tTempStrLength[1] = datas[16+IdBit+1];
                    IdBit += 2;
                    tValueLength = ByteNumUtil.bytesToInt(tTempStrLength);
                }else{
                    mLogger.info("Error：数据值的长度为正常定义。");
                    return returnData;
                }
            }

            byte [] tValueBytes = new byte[tValueLength];
            for(int in = 0; in < tValueBytes.length; in ++){
                tValueBytes[in] = datas[16+IdBit+in] ;
            }
            String tRealVal = "";
            if(tValueType == VALUE_TYPE_INT){
                if(tValueLength == 2){
                    byte [] intBytes = {(byte)0,(byte)0,tValueBytes[0],tValueBytes[1]};
                    tRealVal += ByteNumUtil.bytesToInt(intBytes);
                }else if(tValueLength == 4){
                    tRealVal += ByteNumUtil.bytesToInt(tValueBytes);
                }else if(tValueLength == 8){
                    tRealVal += ByteNumUtil.bytes2Long(tValueBytes);
                }else{
                    mLogger.info("Error：Value类型是Int,但是长度异常。");
                    return returnData;
                }
                if(tId == 1600){
                    dataDate = new Date(Long.parseLong(tRealVal)*1000);
                    tRealVal = Const.getSDF().format(dataDate);
                }
                if(tId == 1514){
                    sensorSN = ByteNumUtil.bytes2Long(tValueBytes);
                    sensorSTR = StringHexUtil.bytes2HexString(tValueBytes);
                }
            }else if(tValueType == VALUE_TYPE_FLOAT){
                if(tValueLength == 4){
                    tRealVal += Float.intBitsToFloat(ByteNumUtil.bytesToInt(tValueBytes));

                    if(tId == 1000 && !humiFlag){
                        dataValue = Float.intBitsToFloat(ByteNumUtil.bytesToInt(tValueBytes));
                    }
                    if(tId == 1010){
                        dataValue = Float.intBitsToFloat(ByteNumUtil.bytesToInt(tValueBytes));
                    }
                }else{
                    mLogger.info("Error：Value类型是Float,但是长度异常");
                }
            }else if(tValueType == VALUE_TYPE_STRING){
                tRealVal += StringHexUtil.bytes2HexString(tValueBytes);
            }else if(tValueType == VALUE_TYPE_NULL){
                tRealVal += StringHexUtil.bytes2HexString(tValueBytes);
            }

            mLogger.debug("(" + StringHexUtil.b2HS(temp5[2]) + StringHexUtil.b2HS(temp5[3]) + ")");
            mLogger.debug(tId + "" + tName + "" + tRealVal );
            mLogger.debug("(" + StringHexUtil.bytes2HexString(tValueBytes)+ "," + tValueLength + "字节,"+tValueType + ")");
            mLogger.debug("");
            if(tId== 1510){
                ytc.setCid(tRealVal);
            }else if(tId== 1511){
                ytc.setsNo(tRealVal);
            }else if(tId== 1600){
                ytc.setTime(tRealVal);
            }else{
                ytc.setDataV(tRealVal);
                ytc.setDataK(tId+"");
            }
            if(null!=ytc.getCid()&&null!=ytc.getsNo()&&null!=ytc.getTime()&&null!=ytc.getDataK()&&null!=ytc.getDataV()){
                /**
                 * 接口存ytc
                 */
            	mLogger.info("	:"+ytc.getCid()+"    接口编号:"+ytc.getsNo()+"    时间戳:"+ytc.getTime()+"    key:"+ytc.getDataK()+"    value:"+ytc.getDataV());
                /**
                 * 封装成PageData对象 2016-8-2 Varro 
                 */
                
                PageData pd = new PageData();
                YingtongQuota quota =new YingtongQuota() ;
                quota.DEVICE_ID.setValue(ytc.getCid());
                quota.PORT_ID.setValue(ytc.getsNo());
                quota.DEVICE_TYPE.setValue(Integer.toString(2));
                if(ytc.getDataK().equals("1040")){
                	quota.CO2.setValue(ytc.getDataV());
	             }
	             if(ytc.getDataK().equals("1050")){
	               quota.NEGATIVE_PRESSURE.setValue(ytc.getDataV());
	             }
	             quota.COLLECT_DATETIME.setValue(DateUtil.getTime() );
	             quota.SOURCE_CODE.setValue(StringHexUtil.bytes2HexString(datas));
                 quota.setDeviceKeyId();
                 pd.put(quota.ID.getValue(),quota);
                 rtPd.putAll(pd);
                
                 mLogger.info("YingtongQuota序列号:"+quota.DEVICE_ID.getValue()+"    YingtongQuota接口编号:"+quota.PORT_ID.getValue()+"co2=间戳:"+quota.CO2.getValue()+" 气压:"+quota.NEGATIVE_PRESSURE.getValue());
                
//                impl.writeData(pd);

            }
            IdBit += tValueLength;

            if((16 + IdBit)==datas.length){
                mLogger.info("Success");
                break;
            }else if((16 + IdBit)>datas.length){
                mLogger.info("Error：数据循环异常结束。");
                break;
            }

            loopId ++;
            if(loopId >99){
                mLogger.info("Error：数据循环超过10次，发生异常。");
                return returnData;
            }
        }
        if(needreturn){
            returnData = genResponseByte(messageType,temp3);
        }


//		SBYincommData mSBYincommData = new SBYincommData();
//		mSBYincommData.setDataDate(dataDate==null?new Date():dataDate);
//		if(messageType == 3){
//			mSBYincommData.setDataType("时间同步");
//		}else{
//			mSBYincommData.setDataType("上报数据");
//		}
//		mSBYincommData.setMmSn(deviceSN);
//		mSBYincommData.setDataSn(dataSN);
//		mSBYincommData.setSensorSnHex(sensorSTR);
//		mSBYincommData.setSensorSn(sensorSN);
//		mSBYincommData.setParaId(dataId);
//		mSBYincommData.setParaValue(new BigDecimal(dataValue));
//		mSBYincommData.setCreateDate(new Date());
//		mSBYincommData.setReceiveData(StringHexUtil.bytes2HexString(datas));
//		mSBYincommData.setConfirmData(StringHexUtil.bytes2HexString(returnData));
//
//		HashMap<String,Object> tPara = new HashMap<String,Object>();
//		tPara.put("SBYincommData", mSBYincommData);
//
//		tSBYincommManager.dealSave(tPara);

        return returnData;
    }

    public static PageData getPageData(){
        return rtPd;
    }

    public void init(){
        ytc=new YingtongControl();
    }
    private byte[] genResponseByte(int messageType,byte[] frameSN){
        byte [] ResponseByte = null;
        if(messageType == 3){
            ResponseByte = new byte[20];
        }else{
            ResponseByte = new byte[16];
        }

        // 起始符 4字节
        ResponseByte[0] = HEADBYTE[0];
        ResponseByte[1] = HEADBYTE[1];
        ResponseByte[2] = HEADBYTE[2];
        ResponseByte[3] = HEADBYTE[3];

        // 帧类型，2字节
        if(messageType == 3){
            // 时间同步
            ResponseByte[4] = 0x44; // 0100 0100
            ResponseByte[5] = TYPE_ACK;
        }else{
            ResponseByte[4] = 0x44; // 0100 0100
            ResponseByte[5] = TYPE_DATA;
        }

        // 帧长度，2字节
        byte[] len = ByteNumUtil.intToBytes(ResponseByte.length);
        ResponseByte[6] = len[2];
        ResponseByte[7] = len[3];
        // 序列号  4字节
        ResponseByte[8] = 0;
        ResponseByte[9] = 0;
        ResponseByte[10] = 0;
        ResponseByte[11] = 0;
        // 帧序号  2字节
        ResponseByte[12] = frameSN[2];
        ResponseByte[13] = frameSN[3];
        // CRC 2字节
        ResponseByte[14] = 0;
        ResponseByte[15] = 0;

        if(messageType == 3){
            int curTimes =  (int)((new Date()).getTime()/1000);
            byte[] curTimeByte = ByteNumUtil.intToBytes(curTimes);

            ResponseByte[16] = curTimeByte[0];
            ResponseByte[17] = curTimeByte[1];
            ResponseByte[18] = curTimeByte[2];
            ResponseByte[19] = curTimeByte[3];
        }

        byte[] crcAck = CRC16_Modbus.getSendBuf(StringHexUtil.bytes2HexString(ResponseByte));
        ResponseByte[14] = crcAck[0];
        ResponseByte[15] = crcAck[1];
        return ResponseByte;
    }
    public void bbbb(){
    	 try {
             String socketTaskName = Const.getSDF().format(new Date()) ;
             mLogger.info("YingtongTaskServiceImpl("+socketTaskName+") start ,线程号====" + Thread.currentThread());

             int dataCount = 1;
             InputStream in = socket.getInputStream();
             ByteArrayOutputStream bo = null;
             byte[] buffer = new byte[1024];
             int length = 0;
             while ((length=in.read(buffer))!=-1) {
                 bo = new ByteArrayOutputStream();
                 bo.write(buffer, 0, length);
                 long longtime = System.currentTimeMillis();
                 byte[] res = bo.toByteArray();

                 mLogger.info(Const.getSDF().format(new Date(longtime))+"接收帧"+dataCount+"："+ StringHexUtil.bytes2HexString(res));

                 byte[] response = dealDatas(res);

                 if(response != null){
                     mLogger.info(Const.getSDF().format(new Date(longtime))+"确认帧"+dataCount+"："+ StringHexUtil.bytes2HexString(response));
                     OutputStream tOutputStream = socket.getOutputStream();
                     tOutputStream.write(response);
                     tOutputStream.flush();
                 }

                 mLogger.info("");
                 dataCount++;
             }
             mLogger.info("YingtongTaskServiceImpl("+socketTaskName+") end   ,线程号====" + Thread.currentThread());
         }catch (Exception e) {
             e.printStackTrace();
         }finally{
             if(socket != null){
                 try {
                     socket.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
    }
    
    
    
}
