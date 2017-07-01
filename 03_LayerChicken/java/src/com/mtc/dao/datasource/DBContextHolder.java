/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.mtc.dao.datasource;

/**
 * @ClassName: DBContextHolder
 * @Description: 
 * @Date 2016年5月4日 下午2:39:12
 * @Author Yin Guo Xiang
 * 
 */
public class DBContextHolder {
    /**
     * 线程threadlocal
     */
    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private static String DB_TYPE_BROILER = "dataSource_broiler";
    private static String DB_TYPE_LAYER = "dataSource_layer";

    public static String getDbType() {
        String db = contextHolder.get();
        if (db == null) {
            db = DB_TYPE_LAYER;// 默认是读写库
        }
        return db;
    }

    /**
     * 
     * 设置本线程的dbtype
     * 
     * @param str
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static void setDbType(String str) {
        contextHolder.set(str);
    }

    /**
     * clearDBType
     * 
     * @Title: clearDBType
     * @Description: 清理连接类型
     */
    public static void clearDBType() {
        contextHolder.remove();
    }
}