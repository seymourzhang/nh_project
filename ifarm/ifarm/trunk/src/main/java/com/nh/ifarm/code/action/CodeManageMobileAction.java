package com.nh.ifarm.code.action;

import com.nh.ifarm.code.service.CodeManageService;
import com.nh.ifarm.goods.service.GoogsService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import com.sun.tools.javac.jvm.Code;
import net.sf.json.JSONSerializer;
import net.sf.json.test.JSONAssert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Seymour on 2017/6/15.
 */
@Controller
@RequestMapping("/sys/codeMobile")
public class CodeManageMobileAction extends BaseAction {

    @Autowired
    private CodeManageService codeManageService;

    @Autowired
    private GoogsService googsService;

    @RequestMapping("/getVariety")
    public void getVariety(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        try {
            JSONObject jsonObject = new JSONObject(aa);
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int BizCode = tUserJson.optInt("biz_code");
            String CodeType = tUserJson.optString("CodeType");
            pd.put("code_type", CodeType);
            List<PageData> codeList = codeManageService.getCodeList(pd);
            JSONArray codes = new JSONArray();
            if ("FEED_TYPE".equals(CodeType) && BizCode != -1) {
                pd.put("good_type", "1");
                pd.put("good_id", BizCode);
                pd.put("mobileFlag", "app");
                List<PageData> lpd = googsService.getCorporationGood(pd);
                for (PageData data : lpd) {
                    JSONObject code = new JSONObject();
                    code.put("corporation_id", data.get("corporation_id"));
                    code.put("corporation_name", data.get("corporation"));
                    codes.put(code);
                }
                resJson.put("Error", "");
                resJson.put("Result", "Success");
            } else {
                if (codeList.size() != 0) {
                    for (PageData data : codeList) {
                        JSONObject code = new JSONObject();
                        code.put(data.get("biz_code").toString(), data.get("code_name").toString());
                        codes.put(code);
                    }
                    resJson.put("Result", "Success");
                    resJson.put("Error", "");
                } else {
                    JSONObject code = new JSONObject();
                    code.put("-1", "暂无");
                    codes.put(code);
                    resJson.put("Result", "Fail");
                }
            }
            resJson.put("CodeData", codes);
        } catch (Exception e) {
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
