package com.nh.ifarm.report.action;

import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * Created by LeLe on 1/20/2017.
 */
@Controller
@RequestMapping("/surroundingsReport")
public class surroundingsAction extends BaseAction {
    @RequestMapping("/showSurroundingsReport")
    public ModelAndView showSurroundingsReport() throws Exception {
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        String[] tabs = new String[]{"active","","","",""};

        if(null == pd.getString("tabId") || "".equals(pd.getString("tabId"))){
            pd.put("tabId","0");
        }
        if(!"0".equals(pd.getString("tabId"))){
            int i = Integer.valueOf(pd.getString("tabId")) ;
            tabs[i] ="active";
            tabs[0]="";
        }
        pd.put("tabs",tabs);
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/report/surroundingsReport");
        mv.addObject("pd",pd);
        return mv;
    }
}
