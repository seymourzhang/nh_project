package com.nh.ifarm.breed.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.breed.service.CultivateCodeService;
import com.nh.ifarm.breed.service.SBGrowingStdService;
import com.nh.ifarm.breed.service.SDFileService;
import com.nh.ifarm.breed.service.impl.SDFileServiceImpl;
import com.nh.ifarm.drug.service.DrugService;
import com.nh.ifarm.goods.service.GoogsService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Seymour on 2016/11/2.
 */
@Controller
@RequestMapping("/breed")
public class CultivateCodeAction extends BaseAction implements ServletContextAware {

    @Autowired
    private SDFileService sdFileService;

    @Autowired
    private SBGrowingStdService sbGrowingStdService;

    @Autowired
    private BatchManageService batchManageService;

    @Autowired
    private CultivateCodeService cultivateCodeService;

    @Autowired
    private DrugService drugService;
     @Autowired
    private GoogsService goodsService;

    ServletContext servletContext;

//    private int uploadFileMaxSize = 10 * 1024 * 1024; //10M
//    private String filePath = "modules/file/upload/";
//    private String tempPath = "modules/file/upload/temp";
//    private String batchPath = "modules/file/upload/batch";

    private String[] needReplaceChar = {"[", "]", "{", "}"};

    @RequestMapping("/companyFileView")
    public ModelAndView companyFileView(Page page, HttpSession session) throws Exception {
        ModelAndView mav = this.getModelAndView();
        SDUser user = (SDUser) session.getAttribute(Const.SESSION_USER);
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = getUserRights(pd, session);
        pd.put("ISENABLED", "1");
        pd.put("user_id", user.getId());
        List<PageData> lol = sdFileService.selectByStatus(pd);
        JSONArray a = new JSONArray();
        for (PageData task : lol) {
            String fileName = task.get("file_name").toString();
            fileName = fileName.replace("\\\\", "");
            task.put("file_name", fileName);
            a.put(task);
        }
        mav.addObject("files", a);
        mav.addObject("pd", pd);
        mav.setViewName("modules/breed/companyFile");
        return mav;
    }

    @RequestMapping("/editFileUrl")
    public ModelAndView editFileUrl(Page page, HttpSession session) throws Exception {
        ModelAndView mav = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mav.setViewName("modules/breed/editFile");
        return mav;
    }

    @RequestMapping("/breedStandardView")
    public ModelAndView breedStandard(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        ModelAndView mav = this.getModelAndView();
        PageData pd = this.getPageData();
        List<PageData> ldp = sbGrowingStdService.selectBroilByVarietyId(pd);

        String tabShow = cultivateCodeService.getTabShow(this.getUserId());

        JSONArray a = new JSONArray();
        for (PageData data : ldp) {
            a.put(data);
        }
        PageData param = new PageData();
//        param.put("code_type", "FEED_TYPE");
        mav.addObject("pd", pd);
        mav.addObject("tabShow", tabShow);
        mav.addObject("goodTypeList", goodsService.getGoodsList3(param));//drugService.selectCode(p));
        mav.addObject("varietyName", ldp.size() == 0 ? null : ldp.get(0).get("variety"));
        mav.addObject("standards", ldp.size() == 0 ? null : a);
        mav.setViewName("modules/breed/breedStandard");
        return mav;
    }

    @RequestMapping("/searchBreedStandard")
    public void searchBreedStandard(HttpSession session, HttpServletResponse response) throws Exception {
        Json j = new Json();
        PageData pd = this.getPageData();
        String goodsType = pd.get("goodTypeId").toString();
        if(goodsType == null || goodsType.equals("")){
            goodsType = "1";
        }

//        String houseTypeId = "";
//        String tabShow = cultivateCodeService.getTabShow(this.getUserId());
//        if(tabShow.equals("3")){
//            houseTypeId = "3";
//        }else{
//            houseTypeId = pd.get("houseTypeId").toString();
//        }
        try{


            String houseTypeId = pd.get("houseTypeId").toString();

            List<PageData> lpd = new ArrayList<>();

            if ("1".equals(houseTypeId)) {
                pd.put("variety_id", goodsType);
                lpd = sbGrowingStdService.selectBroilByVarietyId(pd);
            }
            if ("2".equals(houseTypeId)) {
                pd.put("variety_id", goodsType);
                lpd = sbGrowingStdService.selectByVarietyId(pd);
            }
            if ("3".equals(houseTypeId)) {
                pd.put("variety_id", goodsType);//处理肉鸡的品种问题
                    lpd = sbGrowingStdService.selectCultivateStandardMeatData(pd);
            }
            if ("4".equals(houseTypeId)) {
                lpd = cultivateCodeService.selectGrowSettingList(goodsType);
            }
            if ("5".equals(houseTypeId)) {
                lpd = cultivateCodeService.selectEggSettingList(goodsType);
            }
            if ("6".equals(houseTypeId)) {
                lpd = cultivateCodeService.selectMeatSettingList(goodsType);
            }
            if ("7".equals(houseTypeId)) {
                lpd = cultivateCodeService.selectGrowSettingList(goodsType);
            }
            if ("8".equals(houseTypeId)) {
                lpd = cultivateCodeService.selectEggSettingList(goodsType);
            }
            if ("9".equals(houseTypeId)) {
                lpd = cultivateCodeService.selectMeatSettingList(goodsType);
            }

            if (lpd.size() != 0) {
                j.setObj(lpd);
                j.setSuccess(true);
            } else {
                j.setMsg("暂无数据！");
                j.setSuccess(false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        super.writeJson(j, response);
    }


    @RequestMapping("/getColumnFileRateArray")
    public void getColumnFileRateArray(HttpSession session, HttpServletResponse response) throws Exception {
        Json j = new Json();
        PageData pd = this.getPageData();
        String rateString = pd.getString("columnFieldArray");
        String type = pd.getString("type");
        String goodsType = pd.getString("goodsTypeId");
        JSONObject jsonObject = JSON.parseObject(rateString);
        HashMap<String, Double> rateMap = getRateMap(jsonObject);
        List<PageData> resList = new ArrayList<PageData>();
        boolean isWarn = false;
        boolean isAlarm = false;
        try{
            List<PageData> lpd = new ArrayList<>();

            if ("4".equals(type)) {
                lpd = cultivateCodeService.selectGrowSettingList(goodsType);
                isWarn = true;
            }
            if ("5".equals(type)) {
                lpd = cultivateCodeService.selectEggSettingList(goodsType);
                isWarn = true;
            }
            if ("6".equals(type)) {
                lpd = cultivateCodeService.selectMeatSettingList(goodsType);
                isWarn = true;
            }
            if ("7".equals(type)) {
                lpd = cultivateCodeService.selectGrowSettingList(goodsType);
                isAlarm = true;
            }
            if ("8".equals(type)) {
                lpd = cultivateCodeService.selectEggSettingList(goodsType);
                isAlarm = true;
            }
            if ("9".equals(type)) {
                lpd = cultivateCodeService.selectMeatSettingList(goodsType);
                isAlarm = true;
            }

            if (lpd.size() != 0) {
                PageData pageData = lpd.get(0);
                Iterator it = rateMap.keySet().iterator();
                while(it.hasNext()){
                    String key = (String) it.next();
                    String dataKey = "";
                    if(isWarn){
                        dataKey = key + "_warm_rate";
                    }else if(isAlarm){
                        dataKey = key + "_alarm_rate";
                    }
                    String rateString2 = pageData.getString(dataKey);
                    double rate = rateString2 == "" ? 0D : Double.valueOf(rateString2);
                    String resRate = String.valueOf(rate) + "%";
                    PageData resPageData = new PageData();
                    resPageData.put(key, resRate);
                    resList.add(resPageData);
                }
                j.setObj(resList);
                j.setSuccess(true);
            } else {
                Iterator it = rateMap.keySet().iterator();
                while(it.hasNext()){
                    String key = (String) it.next();
                    PageData resPageData = new PageData();
                    String resRate = "";
                    if(isWarn){
                        resRate = String.valueOf(cultivateCodeService.getWarnRate()) + "%";
                    }else if(isAlarm){
                        resRate = String.valueOf(cultivateCodeService.getAlarmRate()) + "%";
                    }
                    resPageData.put(key, resPageData);
                    resList.add(resPageData);
                }
                j.setObj(resList);
                j.setSuccess(true);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        super.writeJson(j, response);
    }

    @RequestMapping("/newUpload")
    public void upload(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "eFiles", required = false) MultipartFile file) {
        Json j = new Json();
        PageData pd = this.getPageData();
        try {
            sdFileService.uploadFile(request, response, file, pd.getString("upload_file_type"), j);
        } catch (Exception e) {
            logger.error(e.getMessage());
            j.setMsg(e.getMessage());
            j.setSuccess(false);
        } finally {
            super.writeJson(j, response);
        }
    }

    @RequestMapping("/saveTips")
    public void saveTips(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception {
        Json j = new Json();
        PageData pd = this.getPageData();
        SDUser user = (SDUser) session.getAttribute(Const.SESSION_USER);
        String realpath = request.getSession().getServletContext().getRealPath("") + SDFileServiceImpl.filePath;
        String tPath = request.getSession().getServletContext().getRealPath("") + SDFileServiceImpl.tempPath;
        String escapePath = StringUtils.replace(realpath, "\\", "\\\\");
        String fileName = pd.get("file_name").toString();
        File f = new File(tPath + "/" + fileName);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatStr = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = formatStr.format(date);
        String[] name = fileName.split("\\.");
        File realFile = new File(realpath + name[0] + "_" + dateStr + "." + name[1]);
        FileUtils.copyFile(f, realFile);
        String reName = fileName;
        Date curTime = new Date();
        if (!fileName.isEmpty()) {
            for (String s : needReplaceChar) {
                reName = StringUtils.replace(reName, s, "\\\\" + s);
                name[0] = StringUtils.replace(name[0], s, "\\\\" + s);
            }
            pd.put("file_name", reName);
            pd.put("file_path", escapePath + name[0] + "_" + dateStr + "." + name[1]);
            pd.put("download_num", 0);
            pd.put("create_person", user.getId());
            pd.put("create_date", formatterDate.format(curTime));
            pd.put("create_time", formatter.format(curTime));
            pd.put("modify_person", user.getId());
            pd.put("modify_date", formatterDate.format(curTime));
            pd.put("modify_time", formatter.format(curTime));
            int i = sdFileService.insert(pd);
            List<PageData> lcd = sdFileService.selectByStatus(pd);
            j.setObj(lcd);
            j.setMsg("1");
            j.setSuccess(true);
        } else {
            j.setMsg("文件名为空！");
            j.setSuccess(false);
        }
        super.writeJson(j, response);
    }

    @RequestMapping("/deleteRecord")
    public void deleteRecord(HttpServletResponse response, HttpSession session) throws Exception {
        Json j = new Json();
        PageData pd = this.getPageData();
        SDUser user = (SDUser) session.getAttribute(Const.SESSION_USER);
        pd.put("user_id", user.getId());
        int i = sdFileService.updateStatus(pd);
        List<PageData> lcd = new ArrayList<>();
        if (i >= 1) {
            PageData pageData = new PageData();
            pageData.put("ISENABLED", "1");
            lcd = sdFileService.selectByStatus(pageData);
            j.setSuccess(true);
            j.setObj(lcd);
            j.setMsg("1");
        } else {
            j.setSuccess(false);
            j.setMsg("删除失败！");
        }
        super.writeJson(j, response);
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception {
        String fileId = request.getParameter("id");

        String path = servletContext.getRealPath("/");
        String fileName = request.getParameter("fileName");
        String dirName = request.getParameter("dirName");
        String dir = request.getParameter("direct");
//        String filePath =  path + "modules/file/" + dirName + "/" + fileName;
        dir = StringUtils.replace(StringUtils.replace(dir, "\\\\", "\\"), "\\]", "]");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        InputStream fis = null;
        File uploadFile = new File(dir);
        fis = new FileInputStream(uploadFile);
        bis = new BufferedInputStream(fis);
        response.reset();
        fos = response.getOutputStream();
        bos = new BufferedOutputStream(fos);

        fileName = new String(fileName.getBytes("gbk"), "iso8859-1");
        response.setContentType("octets/stream");
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

//        response.setContentType("text/plain;charset=UTF-8");
//        response.setHeader("Content-disposition","attachment; filename=\""+uploadFile.getName() + "\"");
        FileCopyUtils.copy(fis, bos);//spring工具类直接流拷贝
        bos.flush();
        fis.close();
        bis.close();
        fos.close();
        bos.close();

        PageData pd = new PageData();
        Date curTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
        pd.put("id", fileId);
        SDUser user = (SDUser) session.getAttribute(Const.SESSION_USER);
        pd.put("user_id", user.getId());
        sdFileService.updateDownloadNum(pd);
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 下载养殖标准导入模板文件
     *
     * @param response
     * @param request
     * @param session
     * @throws Exception
     * @Author LAVEN
     */
    @RequestMapping("/downloadStandardTemplate")
    public void downloadStandardTemplate(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception {
        session.setAttribute("fileName", "养殖标准模板文件.xlsx");
        super.download(response, request, session);
    }

    /**
     * 下载养殖标准导入模板文件
     *
     * @param response
     * @param request
     * @param session
     * @throws Exception
     * @Author LAVEN
     */
    @RequestMapping("/downloadStandardMeatTemplate")
    public void downloadStandardMeatTemplate(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception {
        session.setAttribute("fileName", "肉鸡养殖标准模板文件.xlsx");
        super.download(response, request, session);
    }

    /**
     * @Description: 上传功能养殖标准文件导入
     * @Params: request
     * @Author: laven
     * @Date: 2017/6/5 下午2:35
     */
    @RequestMapping(value = "/uploadCultivateStandard")
    public void uploadCultivateStandard(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "eFiles", required = false) MultipartFile file) throws Exception {
        Json resJson = new Json();
        PageData pageData = this.getPageData();
        try {
            resJson = cultivateCodeService.uploadCultivateStandard(request, response, file, pageData);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setSuccess(false);
            resJson.setObj(pageData);
        } finally {
            super.writeJson(resJson, response);
        }
    }

    @RequestMapping(value = "/settingWarnAndAlarmRate")
    public void settingWarnAndAlarmRate(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception{
        Json resJson = new Json();

        try{
            PageData pd = this.getPageData();
            String rateString = pd.getString("rateData");
            String type = pd.getString("type");
            String goodsType = pd.getString("goodsTypeId");
            JSONObject jsonObject = JSON.parseObject(rateString);
            HashMap<String, Double> rateMap = getRateMap(jsonObject);
            rateMap.put("type", Double.valueOf(type));
            Json json = cultivateCodeService.settingWarnAndAlarmData(null, rateMap, goodsType);
            if (1 >= 1) {
                resJson.setSuccess(true);
                resJson.setMsg("设置操作成功!");
            } else {
                resJson.setSuccess(false);
                resJson.setMsg("设置操作失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        super.writeJson(resJson, response);
    }

    /**
     * 根据前端传来的jsonObject转换成我们需要的数据
     * @param jsonObject
     * @return
     */
    private HashMap<String,Double> getRateMap(JSONObject jsonObject) {
        HashMap<String, Double> resMap = new HashMap<String, Double>();
        Iterator it = jsonObject.keySet().iterator();
        while (it.hasNext()){
            String key = String.valueOf(it.next());
            String value = String.valueOf(jsonObject.get(key)).replace("%", "");
            double valueD = Double.valueOf("".equals(value) ? "0" : value);
            resMap.put(key, valueD);
        }
        return resMap;
    }

//    public static void main(String[] args) {
//        Double d = new Double(2);
//        BigDecimal b = new BigDecimal(3);
//        Map<String, Object> map = new HashMap<>();
//        map.put("1", d);
//        map.put("2", b);
//        Object o = map.get("2");
//        System.out.println(o.getClass());
//    }
}
