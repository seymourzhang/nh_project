package com.mtc.device;

import com.mtc.common.util.PubFun;
import com.mtc.entity.app.SBBbfar;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by guoxiang on 2017/6/12.
 */
public class BbfarWiredManage {

    private static HashMap<String,SBBbfar> tWiredDataMap ;

    private static BbfarWiredManage tBbfarWiredManage;

    private BbfarWiredManage(){
        tWiredDataMap = new HashMap<String,SBBbfar>();
    }

    public static BbfarWiredManage getInstance(){
        if(tBbfarWiredManage == null){
            tBbfarWiredManage = new BbfarWiredManage();
        }
        return tBbfarWiredManage;
    }

    /**
     *    核心处理
     */
    public SBBbfar dealData(SBBbfar tSBBbfar) {
        try {
            String keyId = tSBBbfar.getKeyid();

            if(tWiredDataMap.containsKey(keyId)){
                SBBbfar lastSBBbfar = tWiredDataMap.get(keyId);
                if(tSBBbfar.getDateTime().getTime() - lastSBBbfar.getDateTime().getTime() < 1000 * 60 * 10){
                    tSBBbfar = getRightValue(lastSBBbfar, tSBBbfar);
                }else{
                    tSBBbfar = useDefaultValue(tSBBbfar);
                }
            }else{
                tSBBbfar = useDefaultValue(tSBBbfar);
            }
            tWiredDataMap.put(keyId,tSBBbfar);
            return tSBBbfar;
        } catch (Exception e) {
            e.printStackTrace();
            tSBBbfar = useDefaultValue(tSBBbfar);
            return tSBBbfar;
        }
    }

    /**
     *    不进行历史比较，直接使用数据
     */
    private SBBbfar useDefaultValue(SBBbfar tSBBbfar){
        tSBBbfar.setT1(dealValue(tSBBbfar.getT1O()));
        tSBBbfar.setT2(dealValue(tSBBbfar.getT2O()));
        tSBBbfar.setT3(dealValue(tSBBbfar.getT3O()));
        tSBBbfar.setT4(dealValue(tSBBbfar.getT4O()));
        tSBBbfar.setT5(dealValue(tSBBbfar.getT5O()));
        tSBBbfar.setT6(dealValue(tSBBbfar.getT6O()));
        tSBBbfar.setH1(dealValue(tSBBbfar.getH1O()));
        tSBBbfar.setH2(dealValue(tSBBbfar.getH2O()));
        return tSBBbfar;
    }

    /**
     *    当前后两次数值在 maxErrorNum 以内时，返回 true
     */
    private boolean compareValue(String value1,String value2){
        int maxErrorNum = 10;

        //  直接取本次数据的情况：1、当上一次值为空  2、上次数据异常
        if(PubFun.isNull(value1) || !PubFun.isNumber(value1)
                || new BigDecimal(85).compareTo(new BigDecimal(value1)) == 0
                || new BigDecimal(0).compareTo(new BigDecimal(value1)) == 0
                ){
            return true;
        }

        //  当本次值为空，或异常时，直接取上次数据
        if(PubFun.isNull(value2) || !PubFun.isNumber(value2) || "-0.1".equals(value2)
                || new BigDecimal(value2).compareTo(new BigDecimal("60")) >= 0){
            return false;
        }

        if(new BigDecimal(value1).subtract(new BigDecimal(value2)).abs().compareTo(new BigDecimal(maxErrorNum)) < 0 ){
            return true;
        }else{
            return false;
        }
    }

    /**
     *    lastSBBbfar    最近一次设备的数据,T1-处理过的数值，T1O-真实上传数据
     *    curSBBbfar     当前设备的数据,T1-处理过的数值，T1O-真实上传数据
     */
    private SBBbfar getRightValue(SBBbfar lastSBBbfar, SBBbfar curSBBbfar){
        String rightT1 = null;
        String rightT2 = null;
        String rightT3 = null;
        String rightT4 = null;
        String rightT5 = null;
        String rightT6 = null;
        String rightH1 = null;
        String rightH2 = null;

        // 当前后两次数据差值在 10 度以内，取本次设备数据，否则都取上次数据
        if(compareValue(lastSBBbfar.getT1O(),curSBBbfar.getT1O())){
            rightT1 = dealValue(curSBBbfar.getT1O());
        }else{
            rightT1 = dealValue(lastSBBbfar.getT1());
        }
        if(compareValue(lastSBBbfar.getT2O(),curSBBbfar.getT2O())){
            rightT2 = dealValue(curSBBbfar.getT2O());
        }else{
            rightT2 = dealValue(lastSBBbfar.getT2());
        }
        if(compareValue(lastSBBbfar.getT3O(),curSBBbfar.getT3O())){
            rightT3 = dealValue(curSBBbfar.getT3O());
        }else{
            rightT3 = dealValue(lastSBBbfar.getT3());
        }
        if(compareValue(lastSBBbfar.getT4O(),curSBBbfar.getT4O())){
            rightT4 = dealValue(curSBBbfar.getT4O());
        }else{
            rightT4 = dealValue(lastSBBbfar.getT4());
        }
        if(compareValue(lastSBBbfar.getT5O(),curSBBbfar.getT5O())){
            rightT5 = dealValue(curSBBbfar.getT5O());
        }else{
            rightT5 = dealValue(lastSBBbfar.getT5());
        }
        if(compareValue(lastSBBbfar.getT6O(),curSBBbfar.getT6O())){
            rightT6 = dealValue(curSBBbfar.getT6O());
        }else{
            rightT6 = dealValue(lastSBBbfar.getT6());
        }
        if(compareValue(lastSBBbfar.getH1O(),curSBBbfar.getH1O())){
            rightH1 = dealValue(curSBBbfar.getH1O());
        }else{
            rightH1 = dealValue(lastSBBbfar.getH1());
        }
        if(compareValue(lastSBBbfar.getH2O(),curSBBbfar.getH2O())){
            rightH2 = dealValue(curSBBbfar.getH2O());
        }else{
            rightH2 = dealValue(lastSBBbfar.getH2());
        }

        curSBBbfar.setT1(rightT1);
        curSBBbfar.setT2(rightT2);
        curSBBbfar.setT3(rightT3);
        curSBBbfar.setT4(rightT4);
        curSBBbfar.setT5(rightT5);
        curSBBbfar.setT6(rightT6);
        curSBBbfar.setH1(rightH1);
        curSBBbfar.setH2(rightH2);

        return curSBBbfar ;
    }

    /**
     *    对数据值进行处理
     */
    private String dealValue(String inStr){
        String outStr = null;
        if(inStr != null && PubFun.isNumber(inStr)){
            if(new BigDecimal(85).compareTo(new BigDecimal(inStr)) != 0
                    && new BigDecimal(0).compareTo(new BigDecimal(inStr)) != 0
                    && !"-0.1".equals(inStr)){
                outStr = inStr;
            }
        }
        return outStr;
    }
}
