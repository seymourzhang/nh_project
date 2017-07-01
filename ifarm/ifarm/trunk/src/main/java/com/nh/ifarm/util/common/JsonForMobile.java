package com.nh.ifarm.util.common;

import java.io.Serializable;

/**
 * JSON模型 (移动专用对象)
 *
 * Created by LeLe on 7/30/2016.
 */
public class JsonForMobile implements Serializable {
    private SubClassResponseDetail ResponseDetail = new SubClassResponseDetail();
    private int ResponseStatus = 0;


    public SubClassResponseDetail getResponseDetail() {
        return ResponseDetail;
    }
    public void setResponseDetail(SubClassResponseDetail ResponseDetail) {
        this.ResponseDetail = ResponseDetail;
    }

    public int getResponseStatus() {
        return ResponseStatus;
    }
    public void setResponseStatus(int ResponseStatus) {
        this.ResponseStatus = ResponseStatus;
    }



    public class SubClassResponseDetail implements Serializable {
        private Object MonitorData =null;
        private String Result = "";

        public Object getMonitorData() {
            return MonitorData;
        }
        public void setMonitorData(Object MonitorData) {
            this.MonitorData = MonitorData;
        }

        public String getResult() {
            return Result;
        }
        public void setResult(String Result) {
            this.Result = Result;
        }

    }



}
