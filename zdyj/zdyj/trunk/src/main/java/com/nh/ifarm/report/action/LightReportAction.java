package com.nh.ifarm.report.action;

import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by LeLe on 12/14/2016.
 */
@Controller
@RequestMapping("/lightReport")
public class LightReportAction extends BaseAction {

    @RequestMapping("/showLightReport")
    public ModelAndView showLightReport() throws Exception {
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/report/lightReport");
        mv.addObject("pd",pd);
        return mv;
    }
}
