package com.nh.ifarm.breed.service.impl;

import com.nh.ifarm.breed.service.SDFileService;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2016/11/2.
 */
@Service
public class SDFileServiceImpl implements SDFileService {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    private int uploadFileMaxSize = 10 * 1024 * 1024; //10M
    public static final String filePath = "modules/file/upload/";
    public static final String tempPath = "modules/file/upload/temp";
    public static final String batchPath = "modules/file/upload/batch";
    public static final String reportPath = "modules/file/upload/report";
    public static final String CULTIVATESTANDARD = "modules/file/upload/cultivateStandard";

    /***
     * 查询有效状态记录
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> selectByStatus(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("SDfileMapper.selectByStatus", pd);
    }

    /***
     * 更改状态
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public int updateStatus(PageData pd) throws Exception {
        return (int) dao.update("SDfileMapper.updateStatus", pd);
    }

    /***
     * 插入上传记录
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public int insert(PageData pd) throws Exception {
        return (int) dao.save("SDfileMapper.insert", pd);
    }

    /***
     * 修改下载次数
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public int updateDownloadNum(PageData pd) throws Exception{
        return (int) dao.update("SDfileMapper.updateDownloadNum", pd);
    }

    /**
     * 上传文件
     *
     * @param request
     * @param response
     * @param file
     * @param fileType: 上传文件类型:
     *                  1: 发雏文件
     *                  2: 发料发药
     *                  3: 养殖标准
     *                  null或空: 公司政策文件
     * @throws Exception
     */
    public String uploadFile(HttpServletRequest request, HttpServletResponse response, MultipartFile file, String fileType, Json resJson) throws Exception {
        boolean isSuccess = false;
        String realPath = request.getSession().getServletContext().getRealPath("");
        String fileExtendName = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        if ("1".equals(fileType) || "2".equals(fileType)) {
            realPath += batchPath;
            fileExtendName = "_" + simpleDateFormat.format(new Date());
        } else if ("3".equals(fileType) || "4".equals(fileType)) {
            realPath = realPath + CULTIVATESTANDARD;
            fileExtendName = "_" + simpleDateFormat.format(new Date());
        } else {
            realPath += tempPath;
        }

        String fileName = file.getOriginalFilename().replace(".", fileExtendName + ".");
        File newFile = new File(realPath + "/" + fileName);
        String[] typechoose = fileName.split("\\.");
        int ichoose = typechoose.length;
        String type = ichoose > 1 ? typechoose[ichoose - 1] : "";
        if ((type.toLowerCase().equals("docx") || type.toLowerCase().equals("pdf") || type.toLowerCase().equals("xlsx") || type.toLowerCase().equals("xls") || type.toLowerCase().equals("doc")) && file.getSize() <= uploadFileMaxSize) {
            FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
            isSuccess = true;
        } else if (!(type.toLowerCase().equals("docx") || type.toLowerCase().equals("pdf") || type.toLowerCase().equals("xlsx") || type.toLowerCase().equals("xls") || type.toLowerCase().equals("doc"))) {
            resJson.setMsg("上传文件仅支持doc、docx、xls、xlsx、pdf格式！");
        } else if (file.getSize() > uploadFileMaxSize) {
            resJson.setMsg("您上传文件大于 " + uploadFileMaxSize / 1024 / 1024 + "MB ！");
        }
        resJson.setSuccess(isSuccess);
        return  realPath + "/" + fileName;
    }
    
    /**
     * 上传文件
     * @param request
     * @param response
     * @param file
     * @throws Exception
     */
    public void uploadFile2(HttpServletRequest request, HttpServletResponse response, MultipartFile file,PageData pd , Json j) throws Exception{
//        String uploadFileType = pd.getString("upload_file_type"); // 上传文件类型   null或空：3: 上传报表文件
    	try{
        String realpath = request.getSession().getServletContext().getRealPath("");
        String fileExtendName = "";
//        File[]  filelist = new File(realpath + "modules/file/upload/report").listFiles();
//        String st = filelist[0].getName();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String fileName ="";
        	//上传报表文件
            realpath += reportPath;
            fileExtendName =pd.get("farm_name").toString()+"_" + pd.get("farm_id").toString()+"_" + df.format(new Date());
            fileName = file.getOriginalFilename().replace(file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf(".")),fileExtendName );
        
        File f = new File(realpath + "/" + fileName);
        String Msg = "";
        String[] typechoose = fileName.split("\\.");
        int ichoose = typechoose.length;
        String type = ichoose > 1 ? typechoose[ichoose - 1] : "";
        if (type.toLowerCase().equals("zip") && file.getSize() <= uploadFileMaxSize) {
//                SimpleDateFormat smat = new SimpleDateFormat("yyyyMMddHHmmss");
            FileUtils.copyInputStreamToFile(file.getInputStream(), f);
            Msg = "1";
            j.setSuccess(true);
        } else if (!type.toLowerCase().equals("zip")) {
            Msg = "上传文件仅支持zip格式！";
            j.setSuccess(false);
        } else if (file.getSize() > uploadFileMaxSize) {
            Msg = "您上传文件大于 " + uploadFileMaxSize / 1024 / 1024 + "MB ！";
            j.setSuccess(false);
        }
        j.setMsg(Msg);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
//        return  (String)(realpath + "/" + fileName);
    }


    /**
     * 获取上传报表文件的导入状态
     * @return
     * @throws Exception
     */
    public List<PageData> selectAndUpdateReportFileStatus(List<File> fileList)throws Exception{
        List<PageData> pdList = new ArrayList<>();
        List<PageData> tmpList = new ArrayList<>();
        List<PageData> rtList = new ArrayList<>();

        //过滤文件列表，只处理未处理过状态的文件
        for(File file : fileList){
            if(file.getName().substring(file.getName().lastIndexOf(".")+1).equals("old")){
                PageData pd = new PageData();
                String[] st = file.getName().split("_");
//                if(st[1].equals(farmId)){
                String[] ct = st[2].split("\\.");
                pd.put("farm_id", st[1]);
                pd.put("create_time", ct[0]);
                pdList.add(pd);
//                }
            }
        }

        if(pdList.size() == 0){
            pdList = null;
        }
        tmpList = (List<PageData>) dao.findForList("SDfileMapper.selectReportFileStatus", pdList);


        //修改文件状态
        for(File file : fileList){
            PageData pdItem = new PageData();
            String[] st = file.getName().split("_");
            boolean flag = true;

            for(PageData pd : tmpList){
                if(pd.getString("farm_id").equals(st[1])){
                    file.renameTo(new File(file.getPath()+".FAILED"));
                    flag = false;
                    pdItem.put("status","导入失败");
                }
            }

            if(flag){
                if(file.getName().substring(file.getName().lastIndexOf(".")+1).equals("old") )
                    file.renameTo(new File(file.getPath()+".SUCCESS"));
                if(file.getName().substring(file.getName().lastIndexOf(".")+1).equals("zip") )
                    pdItem.put("status","待导入");
                if(file.getName().substring(file.getName().lastIndexOf(".")+1).equals("SUCCESS") )
                    pdItem.put("status","导入成功");
                if(file.getName().substring(file.getName().lastIndexOf(".")+1).equals("FAILED") )
                    pdItem.put("status","导入失败");
            }

            pdItem.put("file_name", file.getName().substring(0, file.getName().indexOf(".old")));
            String[] st2 = st[2].split("\\.");
            StringBuilder  sb = new StringBuilder (st2[0]);
            sb.insert(4, "-");
            sb.insert(7, "-");
            sb.insert(10, " ");
            sb.insert(13, ":");
            sb.insert(16, ":");
            pdItem.put("create_date", sb.toString());
            rtList.add(pdItem);
        }

        return rtList;
    }


    /**
     * 获取上传报表文件的导入状态
     * @return
     * @throws Exception
     */
    public List<PageData> selectReportFileStatus(List<File> fileList)throws Exception{
        List<PageData> rtList = new ArrayList<>();
        for(File file : fileList){
            PageData pdItem = new PageData();
            String[] st = file.getName().split("_");

            if(file.getName().substring(file.getName().lastIndexOf(".")+1).equals("zip") )
                pdItem.put("status","待导入");
            if(file.getName().substring(file.getName().lastIndexOf(".")+1).equals("SUCCESS") )
                pdItem.put("status","导入成功");
            if(file.getName().substring(file.getName().lastIndexOf(".")+1).equals("FAILED") )
                pdItem.put("status","导入失败");
            if(file.getName().substring(file.getName().lastIndexOf(".")+1).equals("old") )
                pdItem.put("status","正在导入");

            pdItem.put("file_name", file.getName().substring(0, file.getName().indexOf(".old")));
            String[] st2 = st[2].split("\\.");
            StringBuilder  sb = new StringBuilder (st2[0]);
            sb.insert(4, "-");
            sb.insert(7, "-");
            sb.insert(10, " ");
            sb.insert(13, ":");
            sb.insert(16, ":");
            pdItem.put("create_date", sb.toString());
            rtList.add(pdItem);
        }
        return rtList;
    }
}
