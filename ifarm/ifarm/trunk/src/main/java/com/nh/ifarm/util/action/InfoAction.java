package com.nh.ifarm.util.action;

import com.nh.ifarm.breed.service.SDFileService;
import com.nh.ifarm.util.common.*;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import com.nh.ifarm.util.service.TestService;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by LeLe on 3/2/2017.
 */
@Controller
@RequestMapping("/info")
public class InfoAction extends BaseAction {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    TestService testService;

    @Autowired
    private SDFileService sdFileService;

    /**
     * 版本信息页面
     * raymon 2016-10-18
     * @return
     */
    @RequestMapping(value="/version")
    public ModelAndView showVersion(){
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            pd.putAll(getInfoData(pd));
        } catch (Exception e){
            logger.error(e.getMessage());
        } finally {
            mv.addObject("pd",pd);
            mv.setViewName("modules/util/info");
        }
        return mv;
    }

    /**
     * 获取天气信息
     * raymon 2017-05-17
     * @return
     */
    @RequestMapping(value="/weather")
    public void getWeatherInfo(HttpServletResponse response, HttpServletRequest request, HttpSession session){
        Json j=new Json();
        PageData pd = this.getPageData();
        String rt = "";
        try{
            rt = InfoAction.getWeatherString("http://wthrcdn.etouch.cn/weather_mini?citykey=" + pd.getString("citykey"));
            j.setSuccess(true);
            j.setObj(rt);
        } catch (Exception e){
            j.setSuccess(false);
            j.setObj(rt);
            j.setMsg(e.getMessage());
        } finally {
            super.writeJson(j, response);
        }
    }

    public static String getWeatherString(String url) throws UnsupportedEncodingException {
        URL realUrl = null;
        ByteArrayOutputStream out = null;

        try {
            //真实地址
            realUrl = new URL(url);

            //打开连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

            //设置连接属性
            connection.setRequestProperty("accept", "application/xhtml+xml,application/json,application/xml;charset=utf-8, text/javascript, */*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("contentType", "utf-8");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");

            //FileOutputStream out = new FileOutputStream("e:/text.txt");
            //这里获取的数据时压缩格式的数据所以用gzip进行解压缩
            GZIPInputStream gip = new GZIPInputStream(connection.getInputStream());
            out = new ByteArrayOutputStream();
            //缓冲
            byte []buffer = new byte[1024];
            int len ;
            while((len = gip.read(buffer))!=-1){
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }finally{
            //关闭流
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        //把字节数据转化为字符串返回回去
        return (new String(out.toByteArray(), "utf-8"));
    }

    /**
     * 状态信息页面
     * raymon 2016-10-18
     * @return
     */
    @RequestMapping(value="/status")
    public ModelAndView showStatus(){
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            pd.putAll(getInfoData(pd));
        } catch (Exception e){
            logger.error(e.getMessage());
        } finally {
            mv.addObject("pd",pd);
            mv.setViewName("modules/util/info");
        }
        return mv;
    }

    /**
     * 获取系统类别
     * raymon 2017-05-26
     * @return
     * @throws FileNotFoundException 
     */
    @RequestMapping(value="/getSystemKind")
    public void getSystemKind(HttpServletRequest request){
        //1）获取配置 systemKind
    	String mi = PubFun.getPropertyValue("System.Kind");
        //2）根据systemKind 生成对应的ico文件名 包括文件路径
    	
    	String url1 = request.getSession().getServletContext().getRealPath("")+"framework/image/favicon"+mi+".ico";
    	String url2 = request.getSession().getServletContext().getRealPath("")+"framework/image/favicon.ico";
    	url1 = StringUtils.replace(StringUtils.replace(url1, "\\\\", "\\"), "\\]", "]");
    	url2 = StringUtils.replace(StringUtils.replace(url2, "\\\\", "\\"), "\\]", "]");
        //3）把2）文件覆盖成 favicon.ico
    	File srcFile = new File(url1);
    	File destFile = new File(url2);
    	// 准备复制文件
        int byteread = 0;// 读取的位数
        InputStream in = null;
        OutputStream out = null;
        try {
            // 打开原文件
            in = new FileInputStream(srcFile);
            // 打开连接到目标文件的输出流
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            // 一次读取1024个字节，当byteread为-1时表示文件已经读完
            while ((byteread = in.read(buffer)) != -1) {
                // 将读取的字节写入输出流
                out.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            // 关闭输入输出流，注意先关闭输出流，再关闭输入流
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        String url3 = request.getSession().getServletContext().getRealPath("")+"modules/user/image/background"+mi+".jpg";
    	String url4 = request.getSession().getServletContext().getRealPath("")+"modules/user/image/background.jpg";
    	url3 = StringUtils.replace(StringUtils.replace(url3, "\\\\", "\\"), "\\]", "]");
    	url4 = StringUtils.replace(StringUtils.replace(url4, "\\\\", "\\"), "\\]", "]");
        //3）把2）文件覆盖成 favicon.ico
    	File srcFile3 = new File(url3);
    	File destFile4 = new File(url4);
    	// 准备复制文件
        int byteread1 = 0;// 读取的位数
        InputStream in3 = null;
        OutputStream out4 = null;
        try {
            // 打开原文件
            in3 = new FileInputStream(srcFile3);
            // 打开连接到目标文件的输出流
            out4 = new FileOutputStream(destFile4);
            byte[] buffer1 = new byte[1024];
            // 一次读取1024个字节，当byteread为-1时表示文件已经读完
            while ((byteread1 = in3.read(buffer1)) != -1) {
                // 将读取的字节写入输出流
                out4.write(buffer1, 0, byteread1);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            // 关闭输入输出流，注意先关闭输出流，再关闭输入流
            if (out4 != null) {
                try {
                    out4.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in3 != null) {
                try {
                    in3.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        //1）获取配置 Logo.name
    	String mi1 = PubFun.getPropertyValue("Logo.name");
        //2）根据Logo.name 生成对应的png文件名 包括文件路径
        String url5 = request.getSession().getServletContext().getRealPath("")+"framework/image/"+mi1+".png";
    	String url6 = request.getSession().getServletContext().getRealPath("")+"framework/image/logo.png";
    	url5 = StringUtils.replace(StringUtils.replace(url5, "\\\\", "\\"), "\\]", "]");
    	url6 = StringUtils.replace(StringUtils.replace(url6, "\\\\", "\\"), "\\]", "]");
        //3）把3）文件覆盖成 logo.png
    	File srcFile5 = new File(url5);
    	File destFile6 = new File(url6);
    	// 准备复制文件
        int byteread2 = 0;// 读取的位数
        InputStream in5 = null;
        OutputStream out6 = null;
        try {
            // 打开原文件
            in5 = new FileInputStream(srcFile5);
            // 打开连接到目标文件的输出流
            out6 = new FileOutputStream(destFile6);
            byte[] buffer2 = new byte[1024];
            // 一次读取1024个字节，当byteread为-1时表示文件已经读完
            while ((byteread2 = in5.read(buffer2)) != -1) {
                // 将读取的字节写入输出流
                out6.write(buffer2, 0, byteread2);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            // 关闭输入输出流，注意先关闭输出流，再关闭输入流
            if (out6 != null) {
                try {
                    out6.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in5 != null) {
                try {
                    in5.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        


    }

    /**
     * 系统数据库更新页面
     * raymon 2017-05-31
     * @return
     */
    @RequestMapping(value="/updateDb")
    public void updateDb(HttpServletResponse response, HttpServletRequest request, HttpSession session){
        Json j=new Json();
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        Connection conn =null;
        int fileListCount = 1;
        String maxVersion = "";

        try{
            String projectPath =  PubFun.getWEBINFpath().replace("WEB-INF/","");
            //获取数据库版本信息
            String dbVersionPatchDirPath = projectPath + "/update/db";
            String dbVersionPatchBackupDirPath = projectPath + "/update/backup";
            String dbUpdateSQLFileExtendName = ".sql";
            File file = new File(dbVersionPatchDirPath);
            List<String> dbUpdateFileList = new ArrayList<>();
            getDirectory(file, dbUpdateFileList);
            Collections.sort(dbUpdateFileList);

            SqlSessionFactory sqlSessionFactory = dao.getSqlSessionFactory();
            conn = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource().getConnection();
            ScriptRunner runner = new ScriptRunner(conn);
            Resources.setCharset(Charset.forName("UTF-8")); //设置字符集,不然中文乱码插入错误
            runner.setLogWriter(null);//设置是否输出日志
            //从文件更新数据库
            for(String filePath : dbUpdateFileList){
                if(filePath.indexOf(dbUpdateSQLFileExtendName) >= 0){
                    FileInputStream in = new FileInputStream(filePath);
                    BufferedReader br = new BufferedReader(new FileReader(filePath));
                    runner.runScript(br);
                    int idx = filePath.lastIndexOf("\\");
                    String fileName = filePath.substring(idx+1);
                    int idxDir = filePath.substring(0,idx).lastIndexOf("\\");
                    String dirName = filePath.substring(idxDir+1).replace("\\" + fileName, "");
                    if(maxVersion.compareTo(dirName) <=0){
                        maxVersion = dirName;
                    }
                    FileOutputStream out=new FileOutputStream(dbVersionPatchBackupDirPath + "/" + dirName + "." + fileName);
                    Tools.copyFile(in, out);
                    br.close();
                    in.close();
                    out.close();
                }
            }
            runner.closeConnection();
            conn.close();
            file = null;

            //删除原文件
            for(String filePath : dbUpdateFileList){
                if(filePath.indexOf(dbUpdateSQLFileExtendName) >= 0){
                    File f = new File(filePath);
                    if(f.exists())
                        f.delete();
                    f = null;
                }
            }

            j.setSuccess(true);
            j.setMsg("数据库更新成功！");
        } catch (Exception e){
            logger.error(e.getMessage());
            j.setSuccess(false);
            j.setMsg("数据库更新失败！(" + e.getMessage() + ")");
        } finally {
            super.writeJson(j, response);
        }
    }



    PageData getInfoData(PageData pd) throws Exception{
            String projectPath =  PubFun.getWEBINFpath().replace("WEB-INF/","");
            //获取系统版本信息
            String versionPatchFilePath = projectPath + "version.patch";
            String version = getModifiedTime(versionPatchFilePath);
            pd.put("version",version);
            pd.put("status","系统正常");

            //获取系统数据库版本信息
            String dbVersionPatchBackupDirPath = projectPath + "/update/backup";
            List<String> dbUpdateFileList = new ArrayList<>();
            File file = new File(dbVersionPatchBackupDirPath);
            getDirectory(file, dbUpdateFileList);
            if(dbUpdateFileList.size()>0){
                Collections.sort(dbUpdateFileList);
                int idx = dbUpdateFileList.get(dbUpdateFileList.size()-1).lastIndexOf("\\");
                String fileName = dbUpdateFileList.get(dbUpdateFileList.size()-1).substring(idx+1);
                fileName = fileName.substring(0, fileName.indexOf("."));
                pd.put("db_version", fileName);
            } else{
                pd.put("db_version","");
            }
            return pd;
    }

    // 递归遍历
    private void getDirectory(File file, List<String> dbUpdateFileList) {
        File flist[] = file.listFiles();
        if (flist == null || flist.length == 0) {
            return;
        }
        for (File f : flist) {
            if (f.isDirectory()) {
                //这里将列出所有的文件夹
//                System.out.println("Dir==>" + f.getAbsolutePath());
                getDirectory(f, dbUpdateFileList);
            } else {
                //这里将列出所有的文件
//                System.out.println("file==>" + f.getAbsolutePath());
                dbUpdateFileList.add(f.getAbsolutePath());
            }
        }
    }


    @RequestMapping("/name")
    public void getSystemName(HttpServletResponse response){
        Json j=new Json();
        PageData pd = this.getPageData();
        j.setSuccess(true);
        j.setObj(pd);
        super.writeJson(j, response);
    }

    String getModifiedTime(String patchFilePath){
        File f = new File(patchFilePath);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(f.lastModified());
        return  sdf.format(cal.getTime());
    }

    /**
     * 测试页面
     * raymon 2017-03-16
     * @return
     */
    @RequestMapping(value="/test")
    public ModelAndView test(HttpServletRequest request, HttpServletResponse response)throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        try{
            pd.putAll(getInfoData(pd));
        } catch (Exception e){
            logger.error(e.getMessage());
        } finally {
            mv.addObject("pd",pd);
            mv.setViewName("modules/util/test");
        }
        return mv;
    }

    /**
     * 上传文件（测试用）
     * raymon 2017-05-10
     * @return
     */
    @RequestMapping(value="/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "eFiles", required = false) MultipartFile file)throws Exception{
        Json j = new Json();
        PageData pd = this.getPageData();
        String filePath = null;
        try{
            ResourceBundle conf = ResourceBundle.getBundle("pro/excelFormat_batchSend");
            filePath = sdFileService.uploadFile(request, response, file, pd.getString("upload_file_type"), j);
            Office of = new Office();
            Workbook wb = of.openFile(filePath);
            pd.putAll(of.checkExcelDataVaild(wb, conf, pd.getString("upload_file_type")));
            j.setSuccess(true);
            j.setObj(pd);
        } catch (Exception e){
            logger.error(e.getMessage());
            j.setSuccess(false);
        } finally {
            super.writeJson(j, response);
        }
    }

    @RequestMapping(value="/updateReportFilesStatus")
    public void updateReportFilesStatus(HttpServletRequest request, HttpServletResponse response)throws Exception{
        Json j = new Json();
        List<PageData> list = new ArrayList<>();
        PageData param = this.getPageData();
        String farmId = "10012";
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
            list = sdFileService.selectAndUpdateReportFileStatus(fileList);

        } catch (Exception e){
            j.setMsg(e.getMessage());
            j.setSuccess(false);
        } finally {
            j.setObj(list);
            super.writeJson(j, response);
        }
    }

}
