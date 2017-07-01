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
import java.text.SimpleDateFormat;
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

    /***
     * 查询有效状态记录
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> selectByStatus(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("SDfileMapper.selectByStatus", pd);
    }

    /***
     * 更改状态
     * @param pd
     * @return
     * @throws Exception
     */
    public int updateStatus(PageData pd) throws Exception{
        return (int) dao.update("SDfileMapper.updateStatus", pd);
    }

    /***
     * 插入上传记录
     * @param pd
     * @return
     * @throws Exception
     */
    public int insert(PageData pd) throws Exception{
        return (int) dao.save("SDfileMapper.insert", pd);
    }

    /***
     * 修改下载次数
     * @param pd
     * @return
     * @throws Exception
     */
    public int updateDownloadNum(PageData pd) throws Exception{
        return (int) dao.update("SDfileMapper.updateDownloadNum", pd);
    }

    /**
     * 上传文件
     * @param request
     * @param response
     * @param file
     * @throws Exception
     */
    public String uploadFile(HttpServletRequest request, HttpServletResponse response, MultipartFile file, String upload_file_type, Json j) throws Exception{
        String uploadFileType = upload_file_type; // 上传文件类型   null或空：公司政策文件 ；  1：发雏文件  2: 发料发药
        String realpath = request.getSession().getServletContext().getRealPath("");
        String fileExtendName = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        if("1".equals(uploadFileType) || "2".equals(uploadFileType)){
            //发雏文件
            realpath += batchPath;
            fileExtendName ="_" + df.format(new Date());
        } else {
            realpath += tempPath;
        }

        String fileName = file.getOriginalFilename().replace(".",fileExtendName + ".");
        File f = new File(realpath + "/" + fileName);
        String Msg = "";
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
        j.setMsg(Msg);
        return  realpath + "/" + fileName;
    }
}
