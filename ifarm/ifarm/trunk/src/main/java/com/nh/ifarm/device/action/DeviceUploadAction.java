package com.nh.ifarm.device.action;

import com.nh.ifarm.device.service.impl.RotemNetDataServiceImpl;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.common.PubFun;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Yin Guo Xiang on 2017/2/20.
 */
@Controller
@RequestMapping("/DeviceUploadActionMobile")
public class DeviceUploadAction {

    private static Logger mLogger = Logger.getLogger(DeviceUploadAction.class);
    @Autowired
    private RotemNetDataServiceImpl rotemNetDataServiceImpl;

    @RequestMapping("/rotemNetUpload")
    public void rotemNetUpload(HttpServletRequest request,
                               HttpServletResponse response) {
        mLogger.info("=====Now start executing DeviceUpload.rotemNetUpload");

        String dealRes = null;

        try {
            int columnLength = 50;
            boolean isNull = false;
            PageData tPageData = new PageData();
            StringBuilder tempData = new StringBuilder();
            for(int i = 1; i <= columnLength; i++ ){
                String paraName = PubFun.excelColIndexToStr(i);
                String paraCol = "";
                if(paraName.length()>1){
                    paraCol = paraName.substring(0, 1) + paraName.substring(1).toLowerCase();
                }else{
                    paraCol = paraName ;
                }
                String paraValue = request.getParameter(paraName);

                if(!StringUtils.isEmpty(paraValue)){
                    paraValue = URLDecoder.decode(paraValue.replaceAll("%","%25"),"UTF-8");
                    tempData.append("col" + paraCol + "=").append(paraValue).append(";");
                    tPageData.put("col" + paraCol, paraValue);
                    isNull = true;
                }
            }
            mLogger.info("excel.data: " + tempData);
            String colDesc = request.getParameter("colDesc");

            if(!StringUtils.isEmpty(colDesc)){
//                colDesc = URLDecoder.decode(colDesc,"UTF-8");
                mLogger.info("col_Desc:" + colDesc);
//                tPageData.put("col_Desc", colDesc);
//                tPageData.put("col_Desc", "A-Farm Name B-Collect Date C-Collect Time D-House Name E-House Number F-Flock Number G-警报 H-生长日期 I-时间 J-温度曲线 开启/关闭 K-鸡舍模式 L-通风模式 M-通风级别 N-设置温度 O-目标压力 P-循环 Q-平均温度 R-外部温度 S-内部湿度 T-静压 U-温度传感器1 V-温度传感器2 W-温度传感器3 X-温度传感器4 Y-每日水量 Z-加热器 AA-通道风扇 AB-排气风扇 AC-制冷板冲洗 AD-空气通风1位置（%） AE-灯光1 AF-通道风帘位置 1（%）");
//                isNull = true;
            }

            if(isNull){
                HashMap<String, Object> mParas = new HashMap<String, Object>();
                tPageData.put("makeTime", new Date());
                rotemNetDataServiceImpl.insertRotemNet(tPageData);
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
        mLogger.info("=====Now end executing DeviceUpload.rotemNetUpload");
    }
}
