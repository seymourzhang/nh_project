package com.nh.ifarm.util.common;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 办公文档处理类
 * Created by LeLe on 2017-05-10.
 */
public class Office {

    private static final String RESULT = "result";
    private static final String DATA = "data";
    private static final String ERROR_TYPE = "error_type";   //-1：无错误 0：无数据 1：系统配置错误 2：Excel标签页错误 3：开始行或列错误或列名为空 4：列头错误 5：列数错误 6：单元格类型错误 7:数据保存错误
    private static final String LENGTH_ROW = "length_row";
    private static final String LENGTH_COL = "length_col";
    private static final String LENGTH_HEAD = "length_head";
    private static final String LENGTH_DEFINED_COL = "length_defined_col";
    private static final int IS_HEAD = 0;
    private static final String FIELD_NAME = "field_name";

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    private static final String KEY_SHEET_INDEX = "Sheet.Index";
    private static final String KEY_ROW_INDEX_BEGIN = "Row.Index.Begin";
    private static final String KEY_COLUMN_INDEX_BEGIN = "Column.Index.Begin";
    private static final String KEY_HEAD_COUNT = "Head.Count";
    private static final String KEY_COLUMN_COUNT = "Column.Count";
    private static final String KEY_CELL_TYPE = "Cell.Type";
    private static final String KEY_CELL_DB_RELATION = "Cell.Db.Relation";
    private static final String KEY_CELL_DB_RELATION2 = "Cell.Db.Relation2";


    protected Logger logger = Logger.getLogger(this.getClass());

    /**
     * 判断Excel的版本,获取Workbook
     * @param in
     * @return
     */
    public Workbook getWorkbok(InputStream in, File file) throws IOException {
        Workbook wb = null;
        if(file.getName().endsWith(EXCEL_XLS)){  //Excel 2003
            wb = new HSSFWorkbook(in);
        }else if(file.getName().endsWith(EXCEL_XLSX)){  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 判断文件是否是excel
     * @throws Exception
     */
    public void checkExcelVaild(File file) throws Exception{
        if(!file.exists()){
            throw new Exception("文件不存在");
        }
        if(!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))){
            throw new Exception("文件不是Excel");
        }
    }

    /**
     * 打开文件
     * @param filePath 文件路径
     * @return
     */
    public Workbook openFile(String filePath) throws Exception{
        Workbook workbook = null;
        // 同时支持Excel 2003、2007
        File excelFile = new File(filePath); // 创建文件对象
        FileInputStream is = new FileInputStream(excelFile); // 文件流
        checkExcelVaild(excelFile);
        workbook = getWorkbok(is,excelFile);
        return workbook;
    }

    /**
     * 获取sheet标签
     * @param wb
     * @return
     */
    public Sheet getSheet(Workbook wb, int index){
        return wb.getSheetAt(index);
    }

    /**
     * 检查文件数据是否正确
     * @throws Exception
     */
    public PageData checkExcelDataVaild(Workbook wb, ResourceBundle conf, String upload_file_type) {
        List<PageData> dataList = new ArrayList<>();
        PageData rt = new PageData();
        rt.put(RESULT, true);
        int rowCount = 0;
        int colCount = 0;
        boolean cellFlag = true;
        int realRowCount = 0;
        int realColCount = 0;

        try{
            rt.put(ERROR_TYPE,1);
            //获取配置信息
            Integer confSheetIndex = Integer.valueOf(String.valueOf(conf.getObject(KEY_SHEET_INDEX)) ) ;
            Integer confRowIndexBegin = Integer.valueOf(String.valueOf(conf.getObject(KEY_ROW_INDEX_BEGIN)));
            Integer confColumnIndexBegin = Integer.valueOf(String.valueOf(conf.getObject(KEY_COLUMN_INDEX_BEGIN)))  ;
            Integer confHeadCount = 1 ;
            Integer confColumnCount = Integer.valueOf(String.valueOf(conf.getObject(KEY_COLUMN_COUNT)))  ;
            String confCellType = conf.getString(KEY_CELL_TYPE);
            String confCellDbRelation = "";
            if(upload_file_type.equals("1")){
            	confCellDbRelation = conf.getString(KEY_CELL_DB_RELATION);
            }else{
            	confCellDbRelation = conf.getString(KEY_CELL_DB_RELATION2);
            }

            PageData pdCellType = propToPageData(confCellType);
            PageData pdCellDbRelation =  propToPageData(confCellDbRelation);

            rt.put(ERROR_TYPE,2);
            //获取sheet页
            Sheet sheet = wb.getSheetAt(confSheetIndex-1);

            PageData pd = new PageData();
            PageData headPd = new PageData();
            //获取行
            for (Row row : sheet) {
                // 判断开始行
                if(rowCount+1 < confRowIndexBegin){
                    rowCount ++;
                    continue;
                }

                pd = new PageData();
                //获取列
                for (Cell cell : row) {
                    // 判断开始列
                    if(colCount+1 < confColumnIndexBegin){
                        colCount ++;
                        continue;
                    }
                    // 如果当前单元格没有数据，跳出循环
                    if(cell.toString().equals("") && cellFlag){
                        rt.put(ERROR_TYPE,3);
                        rt.put(DATA, "[" + rowCount+1 + "," + colCount+1 + "]");
                        throw new Exception("开始行或列错误或列名为空[" + rowCount+1 + "," + colCount+1 + "]");
                    } else{
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if(realRowCount==0){
                            headPd.put(colCount+1, cell.getRichStringCellValue().getString());
                        } else{
                            rt.put(ERROR_TYPE,6);
                            pd.put(colCount+1, cell.getRichStringCellValue().getString());
                        }
                    }
                    colCount ++;
                    if(realColCount < confColumnCount){
                        realColCount ++;
                    }
                }

                if(colCount!=0){
                    if(cellFlag){
                        appendDataMapping(headPd, pdCellDbRelation);
                        headPd.put(IS_HEAD, true);
                        dataList.add(headPd);
                    }
                    cellFlag = false;
                }
                if(pd.size()>0){
                    convertDataType(pd, pdCellType);
                    appendDataMapping2(pd, pdCellDbRelation);
                    pd.put(IS_HEAD, false);
                    dataList.add(pd);
                }

                colCount=0;
                rowCount ++;
                realRowCount ++;
            }
            
            rt.put(LENGTH_ROW, realRowCount);
            rt.put(LENGTH_COL, realColCount);
            rt.put(LENGTH_HEAD, confHeadCount);
            rt.put(LENGTH_DEFINED_COL, confColumnCount);
            rt.put(RESULT, true);
            rt.put(ERROR_TYPE,-1);
            rt.put(DATA,dataList);
        } catch (Exception e){
            rt.put(RESULT, false);
            logger.error(e.getMessage());
        } finally {
            return rt;
        }



    }


    /**
     * 属性值转换成Pd对象
     * @param v
     * @return
     */
    public PageData propToPageData(String v){
        PageData rt = new PageData();
        for(String tmp : v.split(",")){
            String[] t = tmp.split(":");
            if(t.length==2){
                rt.put(t[0].trim(), t[1].trim());
            }
        }
        return rt;
    }

    /**
     * 转换数据类型
     * @param pdData
     * @param lookupType
     * @throws Exception
     */
    public void convertDataType(PageData pdData, PageData lookupType) throws Exception{
        for(Object tmp : pdData.keySet()){
            String pdKey = String.valueOf(tmp);
            int iPdKey = Integer.valueOf(pdKey);
            for(Object tmp2 : lookupType.keySet()){
                String lookupKey = (String)tmp2;
                String lookupValue = lookupType.getString(lookupKey);
                if(lookupKey.equals(pdKey)){
                    switch (lookupValue) {
                        case "DATE":
                            DateFormat df = new SimpleDateFormat("yyyyMMdd");
                            pdData.put(iPdKey, df.parse(pdData.getString(iPdKey)));
                            break;
                        case "INT":
                            pdData.put(iPdKey, Integer.valueOf(pdData.getString(iPdKey)));
                            break;
                    }
                }
            }
        }

    }

    /**
     * 新增数据映射关系
     * @param pdData
     * @param lookupType
     * @throws Exception
     */
    public void appendDataMapping(PageData pdData, PageData lookupType) throws Exception{
        for(Object tmp : pdData.keySet()){
            String pdKey = String.valueOf(tmp);
            int iPdKey = Integer.valueOf(pdKey);
            String pdValue = pdData.getString(iPdKey);
                for(Object tmp2 : lookupType.keySet()){
                    String lookupKey = String.valueOf(tmp2);
                    String lookupValue = lookupType.getString(lookupKey);
                    if (lookupKey.equals(pdKey)) {
                        pdData.put(iPdKey, lookupValue);
                    }
                }
        }

    }
    
    public void appendDataMapping2(PageData pdData, PageData lookupType) throws Exception{
    	PageData pdData2 = new PageData();
        for(Object tmp : pdData.keySet()){
            String pdKey = String.valueOf(tmp);
            int iPdKey = Integer.valueOf(pdKey);
            String pdValue = pdData.get(iPdKey).toString();
                for(Object tmp2 : lookupType.keySet()){
                    String lookupKey = String.valueOf(tmp2);
                    String lookupValue = lookupType.getString(lookupKey);
                    if (lookupKey.equals(pdKey)) {
                        pdData2.put(lookupValue, pdData.get(iPdKey));
                    }
                }
        }
        pdData.clear();
        pdData.putAll(pdData2);
    }

}
