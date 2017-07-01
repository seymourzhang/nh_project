package com.nh.ifarm.analyze.action;

import com.nh.ifarm.breed.service.SDFileService;
import com.nh.ifarm.system.service.CodeService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeLe on 11/9/2016.
 * 分析报表控制类
 */
@Controller
@RequestMapping("/analyze")
public class AnalyzeReportAction extends BaseAction {

	@Autowired
    private SDFileService sdFileService;

	@Autowired
    private CodeService codeService;
	
    @RequestMapping(value="/showGoods")
    public ModelAndView showGoods(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/goods");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showBenefit")
    public ModelAndView showBenefit(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/showBenefit");

        PageData pdCodeFeedType = codeService.getCodeForFeedType(pd);
        if(pdCodeFeedType == null){
            mv.setViewName("modules/util/addfarm");
            return mv;
        }
        String mi = pdCodeFeedType.getString("feed_type");
        mv.addObject("FeedType",mi.replace(" ","").toString());
        mv.addObject("FeedTypeValue",pdCodeFeedType.getString("mapping_type"));

        String[] tabs = new String[]{"active","","","","","","","","",""};
        if(null == pd.getString("tabId") || "".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(mi.split(",")[0]);
            if(mi.contains("10")){
                i=10;
            }
            pd.put("tabId", i.toString());
            tabs[i - 1] ="active";
            tabs[0]="";
        }else if(!"0".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(pd.getString("tabId")) ;
            pd.put("tabId", i.toString());
            tabs[i-1] ="active";
            tabs[0]="";
        }
        pd.put("tabs",tabs);
        mv.addObject("pd",pd);
        return mv;
    }


    @RequestMapping(value="/showDrugs")
    public ModelAndView showDrugs(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/drug");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showWeeklyReport")
    public ModelAndView showWeeklyReport(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/weeklyReport");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showDailyProductionReport")
    public ModelAndView showDailyProductionReport(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/dailyProductionReport");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showWeeklyProductionReport")
    public ModelAndView showWeeklyProductionReport(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/weeklyProductionReport");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showCurveAnalysis")
    public ModelAndView showCurveAnalysis(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/curveAnalysis");

        PageData pdCodeFeedType = codeService.getCodeForFeedType(pd);
        if(pdCodeFeedType == null){
            mv.setViewName("modules/util/addfarm");
            return mv;
        }
        String mi = pdCodeFeedType.getString("feed_type");
        mv.addObject("FeedType",mi.replace(" ","").toString());
        mv.addObject("FeedTypeValue",pdCodeFeedType.getString("mapping_type"));

        String[] tabs = new String[]{"active","","","","","","","","",""};
        if(null == pd.getString("tabId") || "".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(mi.split(",")[0]);
            if(mi.contains("10")){
                i=10;
            }
            pd.put("tabId", i.toString());
            tabs[i-1] ="active";
            tabs[0]="";
        }else if(!"0".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(pd.getString("tabId"));
            pd.put("tabId", i.toString());
            tabs[i-1] ="active";
            tabs[0]="";
        }
        pd.put("tabs",tabs);
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showBatchSettlement")
    public ModelAndView showBatchSettlement(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/batchSettlementReport");

        PageData pdCodeFeedType = codeService.getCodeForFeedType(pd);
        if(pdCodeFeedType == null){
            mv.setViewName("modules/util/addfarm");
            return mv;
        }
        String mi = pdCodeFeedType.getString("feed_type");
        mv.addObject("FeedType",mi.replace(" ","").toString());
        mv.addObject("FeedTypeValue",pdCodeFeedType.getString("mapping_type"));

        String[] tabs = new String[]{"active","","","","","","","","",""};
        if(null == pd.getString("tabId") || "".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(mi.split(",")[0]) ;
            if(mi.contains("10")){
                i=10;
            }
            pd.put("tabId", i.toString());
            tabs[i- 1] ="active";
            tabs[0]="";
        } else if(!"0".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(pd.getString("tabId"));
            pd.put("tabId", i.toString());
            tabs[i-1] ="active";
            tabs[0]="";
        }
        pd.put("tabs",tabs);


        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showBatch")
    public ModelAndView showBatch(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/batchReport");

        PageData pdCodeFeedType = codeService.getCodeForFeedType(pd);
        if(pdCodeFeedType == null){
            mv.setViewName("modules/util/addfarm");
            return mv;
        }
        String mi = pdCodeFeedType.getString("feed_type");
        mv.addObject("FeedType",mi.replace(" ","").toString());
        mv.addObject("FeedTypeValue",pdCodeFeedType.getString("mapping_type"));

        String[] tabs = new String[]{"active","","","","","","","","",""};
        if(null == pd.getString("tabId") || "".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(mi.split(",")[0]) ;
            if(mi.contains("10")){
                i=10;
            }
            pd.put("tabId", i.toString());
            tabs[i- 1] ="active";
            tabs[0]="";
        }else if(!"0".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(pd.getString("tabId"));
            pd.put("tabId", i.toString());
            tabs[i-1] ="active";
            tabs[0]="";
        }
        pd.put("tabs",tabs);

        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showMultiFieldCurveAnalysis")
    public ModelAndView showMultiFieldCurveAnalysis(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/multiFieldCurveAnalysisReport");

        PageData pdCodeFeedType = codeService.getCodeForFeedType(pd);
        if(pdCodeFeedType == null){
            mv.setViewName("modules/util/addfarm");
            return mv;
        }
        String mi = pdCodeFeedType.getString("feed_type");
        mv.addObject("FeedType",mi.replace(" ","").toString());
        mv.addObject("FeedTypeValue",pdCodeFeedType.getString("mapping_type"));

        String[] tabs = new String[]{"active","","","","","","","","",""};
        if(null == pd.getString("tabId") || "".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(mi.split(",")[0]) ;
            if(mi.contains("10")){
                i=10;
            }
            pd.put("tabId", i.toString());
            tabs[i- 1] ="active";
            tabs[0]="";
        } else if(!"0".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(pd.getString("tabId"));
            pd.put("tabId", i.toString());
            tabs[i-1] ="active";
            tabs[0]="";
        }
        pd.put("tabs",tabs);

        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showMultiFieldHouseCompare")
    public ModelAndView showMultiFieldHouseCompare(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/multiFieldHouseCompareReport");



        PageData pdCodeFeedType = codeService.getCodeForFeedType(pd);
        if(pdCodeFeedType == null){
            mv.setViewName("modules/util/addfarm");
            return mv;
        }
        String mi = pdCodeFeedType.getString("feed_type");
        mv.addObject("FeedType",mi.replace(" ","").toString());
        mv.addObject("FeedTypeValue",pdCodeFeedType.getString("mapping_type"));


        String[] tabs = new String[]{"active","","","","","","","","",""};
        if(null == pd.getString("tabId") || "".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(mi.split(",")[0]) ;
            if(mi.contains("10")){
                i=10;
            }
            pd.put("tabId", i.toString());
            tabs[i- 1] ="active";
            tabs[0]="";
        } else if(!"0".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(pd.getString("tabId"));
            pd.put("tabId", i.toString());
            tabs[i-1] ="active";
            tabs[0]="";
        }
        pd.put("tabs",tabs);
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showMultiFieldWeeklyReport")
    public ModelAndView showMultiFieldWeeklyReport(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/weeklyReport");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showMultiIndexAnalysis")
    public ModelAndView showMultiIndexAnalysis(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/multiIndexAnalysis");

        PageData pdCodeFeedType = codeService.getCodeForFeedType(pd);
        if(pdCodeFeedType == null){
            mv.setViewName("modules/util/addfarm");
            return mv;
        }
        String mi = pdCodeFeedType.getString("feed_type");
        mv.addObject("FeedType",mi.replace(" ","").toString());
        mv.addObject("FeedTypeValue",pdCodeFeedType.getString("mapping_type"));

        String[] tabs = new String[]{"active","","","","","","","","",""};
        if(null == pd.getString("tabId") || "".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(mi.split(",")[0]) ;
            if(mi.contains("10")){
                i=10;
            }
            pd.put("tabId", i.toString());
            tabs[i- 1] ="active";
            tabs[0]="";
        } else if(!"0".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(pd.getString("tabId"));
            pd.put("tabId", i.toString());
            tabs[i-1] ="active";
            tabs[0]="";
        }
        pd.put("tabs",tabs);
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showDayilyReport")
    public ModelAndView showDayilyReport(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/dayilyReport");
        mv.addObject("pd",pd);
        return mv;
    }
    
    @RequestMapping(value="/showUploadReportFile")
    public ModelAndView showUploadReportFile(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/uploadReportFile");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping("/getFiles")
    public void getFiles(HttpServletRequest request, HttpServletResponse response) {
        Json j = new Json();
        List<PageData> list = new ArrayList<>();
        PageData param = this.getPageData();
        String farmId = param.getString("farm_id");
        try{
            String filePath = request.getSession().getServletContext().getRealPath("") + "modules/file/upload/report";
            File[]  files = new File(filePath).listFiles();
            List<File> fileList = new ArrayList<>();

            //过滤文件列表，只处理符合当前Farm Id的文件
            for(int i =0; i<files.length; i++){
                String[] st = files[i].getName().split("_");
                if(st[1].equals(farmId)){
                    fileList.add(files[i]);
                }
            }

            //处理文件状态
            list = sdFileService.selectReportFileStatus(fileList);

        } catch (Exception e){
            j.setMsg(e.getMessage());
            j.setSuccess(false);
        } finally {
            j.setObj(list);
            super.writeJson(j, response);
        }

    }
    
    @RequestMapping("/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "eFiles", required = false) MultipartFile file) {
        Json j = new Json();
        PageData pd = this.getPageData();
        try{
            String filePath = request.getSession().getServletContext().getRealPath("") + "modules/file/upload/report";
            File[]  files = new File(filePath).listFiles();
            String farmId = pd.get("farm_id").toString();
            boolean flag = true;

            //过滤文件列表，只处理符合当前Farm Id的文件
            for(int i =0; i<files.length; i++){
                String[] st = files[i].getName().split("_");
                boolean a = files[i].getName().substring(files[i].getName().lastIndexOf(".")+1).equals("zip");
                String b = st[1];
                if(st[1].equals(farmId) && files[i].getName().substring(files[i].getName().lastIndexOf(".")+1).equals("zip")){
                    flag = false;
                }
            }
            if(flag)
                sdFileService.uploadFile2(request, response, file, pd, j);
            else {
                j.setMsg("存在未导入的报表文件，请稍后再上传新的文件！");
                j.setSuccess(false);
            }
        } catch (Exception e){
            logger.error(e.getMessage());
            j.setMsg(e.getMessage());
            j.setSuccess(false);
        } finally {
            super.writeJson(j, response);
        }

    }
    
    @RequestMapping("/download")
    public void download(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception {

        String fileName = "报表模板文件.zip";
        String dir = request.getSession().getServletContext().getRealPath("")+"modules/file/download/报表模版文件.zip";
//        String filePath =  path + "modules/file/" + dirName + "/" + fileName;
        dir = StringUtils.replace(StringUtils.replace(dir, "\\\\", "\\"), "\\]", "]");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        InputStream fis = null;
        File downloadFile = new File(dir);
        fis = new FileInputStream(downloadFile);
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
    }

    @RequestMapping(value="/showSystemStat")
    public ModelAndView showSystemStat(Page page)throws Exception{
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/systemStatReport");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showFarmStatReport")
    public ModelAndView showFarmStatReport(Page page)throws Exception{
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/farmStatReport");
        mv.addObject("pd",pd);
        return mv;
    }


    @RequestMapping(value="/showProductionReport")
    public ModelAndView showProductionReport(Page page)throws Exception{
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());

        ModelAndView mv = this.getModelAndView();
        PageData pdCodeFeedType = codeService.getCodeForFeedType(pd);
        if(pdCodeFeedType == null){
            mv.setViewName("modules/util/addfarm");
            return mv;
        }
        String mi = pdCodeFeedType.getString("feed_type");
        mv.addObject("FeedType",mi.replace(" ","").toString());
        mv.addObject("FeedTypeValue",pdCodeFeedType.getString("mapping_type"));
        String[] tabs = new String[]{"active","","","","","","","","",""};

        if(null == pd.getString("tabId") || "".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(mi.split(",")[0]) ;
            if(mi.contains("10")){
                i=10;
            }
            pd.put("tabId", i.toString());
            tabs[i- 1] ="active";
            tabs[0]="";
        } else if(!"0".equals(pd.getString("tabId"))){
            Integer i = Integer.valueOf(pd.getString("tabId"));
            pd.put("tabId", i.toString());
            tabs[i-1] ="active";
            tabs[0]="";
        }
        pd.put("tabs",tabs);


        mv.setViewName("modules/analyze/productionReport");
        mv.addObject("pd",pd);

        return mv;
    }


    /*猪场系统用 开始*/
    @RequestMapping(value="/showPerformanceOverview")
    public ModelAndView showPerformanceOverview(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/performanceOverview");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showPerformanceBearCountAll")
    public ModelAndView showPerformanceBearCountAll(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/performanceBearCountAll");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showPerformanceBearCount0")
    public ModelAndView showPerformanceBearCount0(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/performanceBearCount0");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showChildbirth")
    public ModelAndView showChildbirth(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/childbirth");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showPerformanceBearCount")
    public ModelAndView showPerformanceBearCount(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/performanceBearCount");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showTechnicalPersonnelProductionDataState")
    public ModelAndView showTechnicalPersonnelProductionDataState(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/technicalPersonnelProductionDataState");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showBackupPigFirstPairDays")
    public ModelAndView showBackupPigFirstPairDays(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/backupPigFirstPairDays");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showWeedOutReason")
    public ModelAndView showWeedOutReason(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/weedOutReason");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showWarning")
    public ModelAndView showWarning(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/warning");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showPerformanceOverviewForComplexFarm")
    public ModelAndView showPerformanceOverviewForComplexFarm(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/performanceOverviewForComplexFarm");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showPerformanceBearCount0ForComplexFarm")
    public ModelAndView showPerformanceBearCount0ForComplexFarm(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/performanceBearCount0ForComplexFarm");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showUnChildbirthForComplexFarm")
    public ModelAndView showUnChildbirthForComplexFarm(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/unChildbirthForComplexFarm");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showPerformanceBearCountForComplexFarm")
    public ModelAndView showPerformanceBearCountForComplexFarm(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/performanceBearCountForComplexFarm");
        mv.addObject("pd",pd);
        return mv;
    }

    @RequestMapping(value="/showWeedOutReasonForComplexFarm")
    public ModelAndView showWeedOutReasonForComplexFarm(Page page)throws Exception{
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("user_id", getUserId());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/analyze/weedOutReasonForComplexFarm");
        mv.addObject("pd",pd);
        return mv;
    }
    /*猪场系统用 结束*/
}
