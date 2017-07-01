package com.nh.ifarm.breed.action;

import com.nh.ifarm.breed.service.SBGrowingStdService;
import com.nh.ifarm.breed.service.SDFileService;
import com.nh.ifarm.drug.service.DrugService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2016/11/2.
 */
@Controller
@RequestMapping("/breed")
public class BreedValueBook extends BaseAction implements ServletContextAware {

    @Autowired
    private SDFileService sdFileService;

    @Autowired
    private SBGrowingStdService sbGrowingStdService;

    @Autowired
    private DrugService drugService;

    ServletContext servletContext;

    private int uploadFileMaxSize = 10 * 1024 * 1024; //10M

    private String filePath = "modules/file/upload/";

    private String tempPath = "modules/file/upload/temp";

    private String[] needReplaceChar = {"[", "]", "{", "}"};

    @RequestMapping("/companyFileView")
    public ModelAndView companyFileView(Page page, HttpSession session) throws Exception {
        ModelAndView mav = this.getModelAndView();
        SDUser user = (SDUser) session.getAttribute(Const.SESSION_USER);
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = getUserRights(pd,session);
        pd.put("ISENABLED", "1");
        pd.put("user_id",user.getId());
        List<PageData> lol = sdFileService.selectByStatus(pd);
        JSONArray a = new JSONArray();
        for (PageData task : lol) {
            String fileName = task.get("file_name").toString();
            fileName = fileName.replace("\\\\", "");
            task.put("file_name", fileName);
            a.put(task);
        }
        mav.addObject("files", a);
        mav.addObject("pd",pd);
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
    public ModelAndView breedStandard(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
        ModelAndView mav = this.getModelAndView();
        PageData pd = this.getPageData();
        List<PageData> ldp = sbGrowingStdService.selectBroilByVarietyId(pd);
        JSONArray a = new JSONArray();
        for (PageData data : ldp) {
            a.put(data);
        }
        PageData p = new PageData();
        p.put("code_type","FEED_TYPE");
        mav.addObject("pd",pd);
        mav.addObject("goodTypeList",drugService.selectCode(p));
        mav.addObject("varietyName", ldp.size() == 0 ? null : ldp.get(0).get("variety"));
        mav.addObject("standards", ldp.size() == 0 ? null : a);
        mav.setViewName("modules/breed/breedStandard");
        return mav;
    }

    @RequestMapping("/searchBreedStandard")
    public void searchBreedStandard(HttpSession session, HttpServletResponse response) throws Exception{
        Json j = new Json();
        PageData pd = this.getPageData();
        String goodTypeId = pd.get("goodTypeId").toString();
        String houseTypeId = pd.get("houseTypeId").toString();

        List<PageData> lpd = new ArrayList<>();

        if ("1".equals(houseTypeId)) {
            pd.put("variety_id", goodTypeId);
            lpd = sbGrowingStdService.selectBroilByVarietyId(pd);
        }
        if ("2".equals(houseTypeId)) {
            pd.put("variety_id", goodTypeId);
            lpd = sbGrowingStdService.selectByVarietyId(pd);
        }

        if (lpd.size() != 0) {
            j.setObj(lpd);
            j.setSuccess(true);
        }else{
            j.setMsg("暂无数据！");
            j.setSuccess(false);
        }
        super.writeJson(j, response);
    }

    @RequestMapping("/newUpload")
    public void upload(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "eFiles", required = false) MultipartFile file) {
        Json j = new Json();
        String realpath = request.getSession().getServletContext().getRealPath("") + tempPath;
        String fileName = file.getOriginalFilename();
        File f = new File(realpath + "/" + fileName);
        String Msg = "";
        try {
            String[] typechoose = fileName.split("\\.");
            int ichoose = typechoose.length;
            String type = ichoose > 1 ? typechoose[ichoose - 1] : "";
            if ((type.toLowerCase().equals("docx")
                    || type.toLowerCase().equals("pdf")
                    || type.toLowerCase().equals("xlsx")
                    || type.toLowerCase().equals("xls")
                    || type.toLowerCase().equals("doc")
                    ) && file.getSize() <= uploadFileMaxSize) {
//                SimpleDateFormat smat = new SimpleDateFormat("yyyyMMddHHmmss");
                FileUtils.copyInputStreamToFile(file.getInputStream(), f);
                Msg = "1";
                j.setSuccess(true);
            } else if (!(type.toLowerCase().equals("docx") || type.toLowerCase().equals("pdf") || type.toLowerCase().equals("xlsx")|| type.toLowerCase().equals("xls")|| type.toLowerCase().equals("doc"))) {
                Msg = "上传文件仅支持doc、docx、xls、xlsx、pdf格式！";
                j.setSuccess(false);
            } else if (file.getSize() > uploadFileMaxSize) {
                Msg = "您上传文件大于 " + uploadFileMaxSize / 1024 / 1024 + "MB ！";
                j.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
            j.setSuccess(false);
        }
        j.setMsg(Msg);
        super.writeJson(j, response);
    }

    @RequestMapping("/saveTips")
    public void saveTips(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception {
        Json j = new Json();
        PageData pd = this.getPageData();
        SDUser user = (SDUser) session.getAttribute(Const.SESSION_USER);
        String realpath = request.getSession().getServletContext().getRealPath("") + filePath;
        String tPath = request.getSession().getServletContext().getRealPath("") + tempPath;
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
            pd.put("file_path", escapePath + name[0] +  "_" + dateStr + "." + name[1]);
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
        if (i  >= 1) {
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
        this.servletContext=servletContext;
    }
}
