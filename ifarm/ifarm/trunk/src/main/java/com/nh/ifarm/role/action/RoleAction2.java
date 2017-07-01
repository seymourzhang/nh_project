package com.nh.ifarm.role.action;

import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:角色管理请求分发类
 * @Author: laven
 * @Date: 2017/6/12 下午3:22
 */
@Controller
@RequestMapping(value="/role2")
public class RoleAction2 extends BaseAction {

    @RequestMapping(value="/rolePage")
    public ModelAndView rolePage()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        mv.setViewName("/modules/role/roleList");
        mv.addObject("pd",pd);
        return mv;
    }
}
