package com.nh.ifarm.breed.service;

import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * Created by Seymour on 2016/11/2.
 */
public interface SDFileService {

    List<PageData> selectByStatus(PageData pd) throws Exception;

    int insert(PageData pd) throws Exception;

    int updateStatus(PageData pd) throws Exception;

    int updateDownloadNum(PageData pd) throws Exception;

    /**
     * 上传文件
     * @param request
     * @param response
     * @param file
     * @throws Exception
     */
    String uploadFile(HttpServletRequest request, HttpServletResponse response, MultipartFile file, String upload_file_type, Json j) throws Exception;
    
    void uploadFile2(HttpServletRequest request, HttpServletResponse response, MultipartFile file,PageData pd , Json j) throws Exception;

    List<PageData> selectAndUpdateReportFileStatus(List<File> fileList)throws Exception;

    List<PageData> selectReportFileStatus(List<File> fileList)throws Exception;
}
