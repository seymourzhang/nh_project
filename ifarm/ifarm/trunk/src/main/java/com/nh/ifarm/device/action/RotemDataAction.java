package com.nh.ifarm.device.action;

import com.nh.ifarm.device.service.RotemDataService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Seymour on 2017/2/22.
 */
@Controller
@RequestMapping("/deviceMobile")
public class RotemDataAction extends BaseAction {

    @Autowired
    private RotemDataService rotemDataService;

    @RequestMapping("/showData")
    public void showData(HttpServletResponse response) throws Exception{
        Json j = new Json();
        List<PageData> lpd = rotemDataService.selectRotemData();
        j.setObj(lpd);
        super.writeJson(j, response);
    }
}
