package com.nh.ifarm.batch.service.impl;

import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.code.service.CodeManageService;
import com.nh.ifarm.farm.service.FarmService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import com.nh.ifarm.util.service.ModuleService;
import com.nh.ifarm.util.service.OrganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LeLe on 11/2/2016.
 * 批次管理服务类
 */
@Service
public class BatchManageServiceImpl implements BatchManageService {
    @SuppressWarnings("restriction")
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    FarmService farmService;

    @Autowired
    OrganService organService;
    
    @Autowired
    ModuleService moduleService;
    
    @Autowired
    CodeManageService codeManageService;
    
    /**
     * 获取发雏数据
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getFaChuData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectFaChuData", pd);
    }
    
    /**
     * 获取发料数据
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getFaLiaoData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectFaLiaoData", pd);
    }
    
    /**
     * 获取发药数据
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getFaYaoData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectFaYaoData", pd);
    }

    /**
     * 获取创建批次数据
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getCreateBatchData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectCreateBatchData", pd);
    }
    
    /**
     * 获取出栏日龄
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getOverBatchAge(PageData pd) throws Exception{
    	pd.put("batch_id",getBatchId(pd));
    	List<PageData> list = (List<PageData>) dao.findForList("BatchManageMapper.selectOverBatchAge", pd);
    	return list;
    }
    
    /**
     * 获取调出数量
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getBatchDataCount(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectBatchDataCount", pd);
    }
    
    /**
     * 保存发雏数据
     * @return
     * @throws Exception
     */
    public PageData saveFaChuData(PageData pd) throws Exception{
    	PageData rt = new PageData();
    	int i = 0;
    	i = (Integer) dao.save("BatchManageMapper.insertFaChuData", pd);
    	if(i == 1){
            rt.put("result",true);
            rt.put("msg","新建发雏成功！");
        } else{
            rt.put("result",false);
            rt.put("msg","数据有误，请重新输入！");
        }
    	return rt;
    }
    
    /**
     * 保存发料/发药数据
     * @return
     * @throws Exception
     */
    public PageData saveFaLiaoData(PageData pd) throws Exception{
    	PageData rt = new PageData();
    	int i = 0;
    	i = (Integer) dao.save("BatchManageMapper.insertFaLiaoData", pd);
    	if(i == 1){
            rt.put("result",true);
            rt.put("msg","新建发料/发药成功！");
        } else{
            rt.put("result",false);
            rt.put("msg","数据有误，请重新输入！");
        }
    	return rt;
    }
    
    /**
     * 保存发雏数据
     * @return
     * @throws Exception
     */
    public PageData editFaChuData(PageData pd) throws Exception{
    	PageData rt = new PageData();
    	int i = 0;
    	i = (Integer) dao.update("BatchManageMapper.updateFaChuData", pd);
    	if(i == 1){
            rt.put("result",true);
            rt.put("msg","修改发雏记录成功！");
        } else{
            rt.put("result",false);
            rt.put("msg","数据有误，请重新输入！");
        }
    	return rt;
    }
    
    /**
     * 删除发雏数据
     * @return
     * @throws Exception
     */
    public PageData delFaChuData(PageData pd) throws Exception{
    	PageData rt = new PageData();
    	int i = 0;
    	i = (Integer) dao.delete("BatchManageMapper.deleteFaChuData", pd);
    	if(i == 1){
            rt.put("result",true);
            rt.put("msg","删除成功！");
        } else{
            rt.put("result",false);
            rt.put("msg","删除失败！");
        }
    	return rt;
    }
    
    /**
     * 保存发料/发药数据
     * @return
     * @throws Exception
     */
    public PageData editFaLiaoData(PageData pd) throws Exception{
    	PageData rt = new PageData();
    	int i = 0;
    	i = (Integer) dao.update("BatchManageMapper.updateFaLiaoData", pd);
    	if(i == 1){
            rt.put("result",true);
            rt.put("msg","修改发料/发药记录成功！");
        } else{
            rt.put("result",false);
            rt.put("msg","数据有误，请重新输入！");
        }
    	return rt;
    }
    
    /**
     * 删除发料/发药数据
     * @return
     * @throws Exception
     */
    public PageData delFaLiaoData(PageData pd) throws Exception{
    	PageData rt = new PageData();
    	int i = 0;
    	i = (Integer) dao.delete("BatchManageMapper.deleteFaLiaoData", pd);
    	if(i == 1){
            rt.put("result",true);
            rt.put("msg","删除成功！");
        } else{
            rt.put("result",false);
            rt.put("msg","删除失败！");
        }
    	return rt;
    }

    /**
     * 保存创建批次数据
     * @return
     * @throws Exception
     */
    public PageData saveCreateBatchData(PageData pd) throws Exception{
        PageData rt = new PageData();
        pd.put("batch_id",getBatchId(pd));
        pd.put("breed_days",getBreedDays(pd.getString("house_type")));
        pd.put("service_id","0");
        pd.put("operation_type","2");
        int i = 0;
        List<PageData> list = (List<PageData>) dao.findForList("BatchManageMapper.selectBatchId", pd);
        List<PageData> list2 = getOverBatchData(pd);
        if(list.toArray().length == 1 && (Long)(list.get(0).get("num")) == 0){
            i = (Integer) dao.delete("BatchManageMapper.deleteCreateBatchDataFromCurr", pd);
            i = (i == 0 || i ==1 )?1:0;
            i *= (Integer) dao.save("BatchManageMapper.insertCreateBatchDataToCurr", pd);
            i *= (Integer) dao.save("BatchManageMapper.insertCreateBatchDataToHis", pd);
            dao.save("BatchManageMapper.exec_SP_INIT_DAILY_REPORT", pd);
            
            PageData param2 = new PageData();
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            Date date = new Date();
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            param2.put("begin_date", df2.format(date));
            date.setHours(23);
            date.setMinutes(59);
            date.setSeconds(59);
            param2.put("end_date", df2.format(date));
            dao.save("BatchManageMapper.exec_SP_BREED_DETAIL", param2);
//            dao.save("BatchManageMapper.exec_SP_BREED_DETAIL_WEEK", param2);
            PageData farm = new PageData();
            String farmId =  pd.getString("farm_id");
            String farmName = pd.getString("farm_name");
            if(farmName.indexOf("(")>0){
                farmName = farmName.substring(0,farmName.indexOf("("));
                farm.put("farm_name",farmName + "(" + pd.getString("batch_no") + ")");
                farm.put("id", farmId);
                farm.put("org_id", farmId);
                farm.put("org_name_updated",farmName + "(" + pd.getString("batch_no") + ")" );
                farm.put("modify_person", pd.getInteger("user_id"));
                farmService.editFarm(farm);
                organService.updateOrg(farm);
            }
            if(i == 1){
                rt.put("result",true);
                rt.put("msg","新建批次成功！");
            } else{
                rt.put("result",false);
                rt.put("msg","未知错误，请联系管理员！");
            }
        } else{
            rt.put("result",false);
            rt.put("msg","重复批次号，请重新输入！");
        }
        return rt;
    }

    /**
     * 获取修改批次数据
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getEditBatchData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectEditBatchData", pd);
    }

    /**
     * 保存修改批次数据
     * @return
     * @throws Exception
     */
    public PageData saveEditBatchData(PageData pd) throws Exception{
        PageData rt= new PageData();
        PageData param = new PageData();
        //获取调出批次信息
        param.put("farm_id",pd.getString("farm_id"));
        param.put("house_code",pd.getString("house_code"));
        List<PageData> outList = (List<PageData>) dao.findForList("BatchManageMapper.selectBatchDataCount", param);

        //判断调出数量是否足够
        if(Integer.valueOf(outList.get(0).get("male_count").toString()).intValue() >= Integer.valueOf(pd.get("male_count").toString()).intValue() &&  Integer.valueOf(outList.get(0).get("female_count").toString()).intValue() >= Integer.valueOf(pd.get("female_count").toString()).intValue()){
            //获取调入批次信息
            param = new PageData();
            param.put("farm_id",pd.getString("inFarm_id"));
            param.put("house_code",pd.getString("house_code_target"));
            List<PageData> inList = (List<PageData>) dao.findForList("BatchManageMapper.selectCreateBatchData", param);
            if(outList.toArray().length == 1 && inList.toArray().length == 1){
                //初始化变量参数
                param = new PageData();
                param.putAll(pd);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                param.put("service_id",df.format(new Date()).toString());
                param.putAll(outList.get(0));
                param.put("farm_id",pd.getString("farm_id"));
                param.put("inFarm_id",pd.getString("inFarm_id"));
                param.put("batchId",outList.get(0).getString("batch_id"));
                param.put("batchId_target",inList.get(0).getString("batchId"));
                param.put("house_code_target",inList.get(0).getInteger("houseId").toString());
                param.put("house_name_target",inList.get(0).getString("house"));
                param.put("male_count",pd.getString("male_count"));
                param.put("female_count",pd.getString("female_count"));
                param.put("operation_date",pd.getString("operation_date"));
                param.put("bak",pd.getString("bak"));

                //修改批次数据入历史表
                int i = (Integer) dao.save("BatchManageMapper.insertEditBatchDataToHis", param);
                i = (i == 2)?1:0;

                //调出批次数据更新当前表
                try{
                    i *= (Integer) dao.update("BatchManageMapper.updateEditBatchDataToCurr", param);
                } catch (Exception e){
                    e.printStackTrace();
                }
                i = (i == 2)?1:0;
                
                PageData param2 = new PageData();
                param2.put("male_cur_amount", Integer.valueOf(param.get("male_count").toString()).intValue()*-1);
                param2.put("female_cur_amount", Integer.valueOf(param.get("female_count").toString()).intValue()*-1);
                param2.put("operation_date", param.get("operation_date"));
                param2.put("farm_id", pd.get("farm_id"));
                param2.put("house_id", pd.get("house_code"));
                dao.update("BatchManageMapper.reUpdateBreedDetail", param2);
                
                param2.put("male_cur_amount", Integer.valueOf(param.get("male_count").toString()).intValue());
                param2.put("female_cur_amount", Integer.valueOf(param.get("female_count").toString()).intValue());
                param2.put("farm_id", pd.get("inFarm_id"));
                param2.put("house_id", pd.get("house_code_target"));
                dao.update("BatchManageMapper.reUpdateBreedDetail", param2);
                
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                Date date = new Date();
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                param2.put("begin_date", df2.format(date));
                date.setHours(23);
                date.setMinutes(59);
                date.setSeconds(59);
                param2.put("end_date", df2.format(date));
                dao.save("BatchManageMapper.exec_SP_BREED_DETAIL", param2);
//                dao.save("BatchManageMapper.exec_SP_BREED_DETAIL_WEEK", param2);
                if(1==i){
                    rt.put("result",true);
                    rt.put("msg","修改批次成功！");
                }
            } else{
                rt.put("result",false);
                rt.put("msg","调出栋或调入栋没有批次，请先进鸡！");
            }

        } else{
            rt.put("result",false);
            rt.put("msg","调出数量超出存栏数量，请重新输入数量！");
        }

        return rt;
    }
    
    /**
     * 获取淘汰批次数据
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getTaoTaiData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectTaoTaiData", pd);
    }
    
    /**
     * 保存淘汰批次数据
     * @return
     * @throws Exception
     */
    public PageData saveTaoTaiData(PageData pd) throws Exception{
        PageData rt = new PageData();
        pd.put("batch_id",getBatchId(pd));
        pd.put("service_id","0");
        //获取淘汰批次信息
        List<PageData> outList = (List<PageData>) dao.findForList("BatchManageMapper.selectBatchDataCount", pd);
        if(outList.toArray().length == 1){
        //判断淘汰数量是否超出
        int currMaleCount = Integer.valueOf(outList.get(0).get("male_count").toString()).intValue();
        int currFemaleCount = Integer.valueOf(outList.get(0).get("female_count").toString()).intValue();
        int weed_out_total_count = Integer.valueOf(pd.get("weed_out_total_count").toString()).intValue();
        int weed_out_male_total_count = Integer.valueOf(pd.get("weed_out_male_total_count").toString()).intValue();
        List<PageData> taoTaiList = getTaoTaiData(pd);
        int taoTaiFemaleCount = 0,taoTaiMaleCount = 0;
        List<PageData> overBatchList = getOverBatchData(pd);
//        int maleAndFemaleCount = 0;
        for(int i=0;i<taoTaiList.size();i++){
        	taoTaiFemaleCount = taoTaiFemaleCount + Integer.valueOf(taoTaiList.get(i).get("weed_out_total_count").toString()).intValue();
        	taoTaiMaleCount = taoTaiMaleCount + Integer.valueOf(taoTaiList.get(i).get("weed_out_male_total_count").toString()).intValue();
        }
//        if(overBatchList.size() !=0){
//        maleAndFemaleCount = Integer.valueOf(overBatchList.get(0).get("male_count").toString()).intValue() + Integer.valueOf(overBatchList.get(0).get("female_count").toString()).intValue();
//        }
        if(overBatchList.size()==0 && currMaleCount >= (taoTaiMaleCount + weed_out_male_total_count) && currFemaleCount >= (taoTaiFemaleCount + weed_out_total_count)) {
                    int i = 0;
//                    pd.put("batch_no",getBatchId(pd));
                    //淘汰数据入历史表
                    i = (Integer) dao.save("BatchManageMapper.insertTaoTaiDataToHis", pd);
                    //淘汰数据更新到当前表
                    if((currMaleCount + currFemaleCount) == (taoTaiMaleCount + taoTaiFemaleCount + weed_out_total_count + weed_out_male_total_count)){
                        i *= (Integer) dao.update("BatchManageMapper.updateTaoTaiDataToCurr", pd);
                        //获取进鸡数据
                        List<PageData> list3 = getCreateBatchData(pd);
                        pd.put("male_count", list3.get(0).get("male_count"));
                        pd.put("female_count", list3.get(0).get("female_count"));
                        pd.put("male_weight", 0);
                        pd.put("female_weight", 0);
                        PageData overBatchAge = getOverBatchAge(pd).get(0);
                        pd.put("grow_age", overBatchAge.get("age"));
                        pd.put("bak", null);
                        i = (Integer) dao.save("BatchManageMapper.insertOverBatchDataToHis", pd);
                        i *= (Integer) dao.update("BatchManageMapper.updateOverBatchDataToCurr", pd);
                        i *= (Integer) dao.update("BatchManageMapper.updateOverBatchDataToDetail", pd);
                        pd.put("house_code", null);
                        //获取进鸡数据
                        List<PageData> list2 = getCreateBatchData(pd);
                        int gross_chicken_number=0,gross_male_chicken_number=0;
                        float gross_chicken_weight=0,gross_male_chicken_weight=0;
                        //获取出栏数据
                        pd.put("house_code", null);
                        List<PageData> list4 = getOverBatchData(pd);
                        for(int j=0;j<list4.size();j++){
                        	if(Integer.valueOf(list4.get(j).get("female_count").toString()).intValue()!=Integer.valueOf(list4.get(j).get("weed_out_total_count").toString()).intValue()){
                        	gross_chicken_number = gross_chicken_number+Integer.valueOf(list4.get(j).get("female_count").toString()).intValue()+Integer.valueOf(list4.get(j).get("weed_out_total_count").toString()).intValue();
                        	gross_male_chicken_number = gross_male_chicken_number+Integer.valueOf(list4.get(j).get("male_count").toString()).intValue()+Integer.valueOf(list4.get(j).get("weed_out_male_total_count").toString()).intValue();
                        	}else{
                        		gross_chicken_number = gross_chicken_number+Integer.valueOf(list4.get(j).get("female_count").toString()).intValue();
                        		gross_male_chicken_number = gross_male_chicken_number+Integer.valueOf(list4.get(j).get("male_count").toString()).intValue();
                        	}
                        	gross_chicken_weight = gross_chicken_weight+Float.valueOf(list4.get(j).get("female_weight").toString()).floatValue()*Integer.valueOf(list4.get(j).get("female_count").toString()).intValue()+Float.valueOf(list4.get(j).get("weed_out_total_weight").toString()).floatValue();
                        	gross_male_chicken_weight = gross_male_chicken_weight+Float.valueOf(list4.get(j).get("male_weight").toString()).floatValue()*Integer.valueOf(list4.get(j).get("male_count").toString()).intValue()+Float.valueOf(list4.get(j).get("weed_out_male_total_weight").toString()).floatValue();
                        }
                        if(list2.size()==0){
                        	pd.put("out_datetime", pd.get("operation_date"));
                        	pd.put("gross_chicken_number", gross_chicken_number);
                        	pd.put("gross_chicken_weight", gross_chicken_weight);
                        	pd.put("gross_male_chicken_number", gross_male_chicken_number);
                        	pd.put("gross_male_chicken_weight", gross_male_chicken_weight);
                        	i *= (Integer) dao.save("BatchManageMapper.insertSettleData", pd);
                        	List<PageData> list6 = getSettleData2(pd);
                        	PageData pd3 = new PageData();
                        	pd3.put("settle_id", list6.get(0).get("id"));
                    		pd3.put("farm_id", pd.get("farm_id"));
                    		pd3.put("settle_type", 1);
                    		pd3.put("good_munber", pd.get("gross_chicken_number"));
                    		pd3.put("good_weight", pd.get("gross_chicken_weight"));
                    		pd3.put("f_m_flag", 0);
                    		List<PageData> corporationGoodList3 = getCorporationGood(pd3);
                    		if(corporationGoodList3.size()>0){
                    		pd3.put("good_price", corporationGoodList3.get(0).get("chicken_price"));
                    		pd3.put("good_total_price", Float.valueOf(corporationGoodList3.get(0).get("price").toString()).floatValue()*Float.valueOf(pd.get("gross_chicken_weight").toString()).floatValue());
                    		}
                    		pd3.put("user_id", pd.get("user_id"));
                    		i *= (Integer) dao.save("BatchManageMapper.insertSettleSubData", pd3);
                        	
                        	pd.put("send_batch_no", pd.get("batch_no"));
                        	List<PageData> list5 = selectSumByGoodId(pd);
                        	for(int g =0;g<list5.size();g++){   
                        		PageData pd2 = new PageData();
                        		pd2.put("settle_id", list6.get(0).get("id"));
                        		pd2.put("farm_id", pd.get("farm_id"));
                        		if(Integer.valueOf(list5.get(g).get("good_type").toString()).intValue()==6){
                        			pd2.put("settle_type", 3);
                        		}else{
                        			pd2.put("settle_type", 4);
                        		}
                        		pd2.put("good_id", list5.get(g).get("goods_id"));
                        		List<PageData> list7 = getGoodsList(pd2);
                        		pd2.put("good_name", list7.get(0).get("good_name"));
                        		pd2.put("good_munber", list5.get(g).get("sumCount"));
                        		pd2.put("f_m_flag", 0);
                        		List<PageData> corporationGoodList = moduleService.service("googsServiceImpl","getCorporationGood",new Object[]{pd2});
                        		if(corporationGoodList.size()>0){
                        		pd2.put("good_price", corporationGoodList.get(0).get("price"));
                        		pd2.put("good_total_price", Float.valueOf(corporationGoodList.get(0).get("price").toString()).floatValue()*Float.valueOf(list5.get(g).get("sumCount").toString()).floatValue());
                        		pd2.put("good_unit", corporationGoodList.get(0).get("unit"));
                        		pd2.put("spec", corporationGoodList.get(0).get("spec"));
                        		}
                        		pd2.put("user_id", pd.get("user_id"));
                        		i *= (Integer) dao.save("BatchManageMapper.insertSettleSubData", pd2);
                        	}
                        	
                        	pd.put("child_batch_no", pd.get("batch_no"));
                        	PageData list8 = selectSumjimiao(pd);
                        	if(list8 != null){
                        		PageData pd2 = new PageData();
                        		pd2.put("settle_id", list6.get(0).get("id"));
                        		pd2.put("farm_id", pd.get("farm_id"));
                        		pd2.put("settle_type", 2);
                        		pd2.put("good_id", list8.get("variety_id"));
//                        		List<PageData> list7 = getGoodsList(pd2);
                        		pd2.put("good_name", list8.get("variety"));
                        		pd2.put("good_munber", list8.get("sumCount"));
                        		pd2.put("f_m_flag", 0);
                        		List<PageData> corporationGoodList = getCorporationGood(pd2);
                        		if(corporationGoodList.size()>0){
                        		pd2.put("good_price", corporationGoodList.get(0).get("price"));
                        		pd2.put("good_total_price", Float.valueOf(corporationGoodList.get(0).get("price").toString()).floatValue()*Float.valueOf(list8.get("sumCount").toString()).floatValue());
                        		pd2.put("good_unit", corporationGoodList.get(0).get("unit"));
                        		pd2.put("spec", corporationGoodList.get(0).get("spec"));
                        		}
                        		pd2.put("user_id", pd.get("user_id"));
                        		i *= (Integer) dao.save("BatchManageMapper.insertSettleSubData", pd2);
                        	}
                        	
                        	
                        	
                        }
                    }else{
                    	pd.put("female_count", pd.get("weed_out_total_count"));
                    	pd.put("male_count", pd.get("weed_out_male_total_count"));
                    	i *= (Integer) dao.update("BatchManageMapper.updateTaoTaiDataToCurr", pd);
                    }
                    
                    PageData param2 = new PageData();
                    param2.put("male_cur_amount", weed_out_male_total_count*-1);
                    param2.put("female_cur_amount", weed_out_total_count*-1);
                    param2.put("operation_date", pd.get("operation_date"));
                    param2.put("farm_id", pd.get("farm_id"));
                    param2.put("house_id", pd.get("house_code"));
                    dao.update("BatchManageMapper.reUpdateBreedDetail", param2);
                    
                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    Date date = new Date();
                    date.setHours(0);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    param2.put("begin_date", df2.format(date));
                    date.setHours(23);
                    date.setMinutes(59);
                    date.setSeconds(59);
                    param2.put("end_date", df2.format(date));
                    dao.save("BatchManageMapper.exec_SP_BREED_DETAIL", param2);
//                    dao.save("BatchManageMapper.exec_SP_BREED_DETAIL_WEEK", param2);
                    if(i == 1){
                        rt.put("result",true);
                        rt.put("msg","淘汰批次成功！");
                    } else{
                        rt.put("result",false);
                        rt.put("msg","未知错误，请联系管理员！");
                    }
        } else{
            rt.put("result",false);
            rt.put("msg","出栏数量及淘汰总数量和不等于存栏数量，请重新输入数量！");
        }
        } else{
            rt.put("result",false);
            rt.put("msg","淘汰栋没有批次，请先进鸡！");
        }
        return rt;
    }

    /**
     * 获取出栏批次数据
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getOverBatchData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectOverBatchData", pd);
    }

    /**
     * 保存出栏批次数据
     * @return
     * @throws Exception
     */
    public PageData saveOverBatchData(PageData pd) throws Exception{
        PageData rt = new PageData();
        pd.put("batch_id",getBatchId(pd));
        pd.put("service_id","0");

        List<PageData> outList = (List<PageData>) dao.findForList("BatchManageMapper.selectBatchDataCount", pd);

        //判断出栏数量是否足够
        int currMaleCount = Integer.valueOf(outList.get(0).get("male_count").toString()).intValue();
        int currFemaleCount = Integer.valueOf(outList.get(0).get("female_count").toString()).intValue();
        int maleCount = Integer.valueOf(pd.get("male_count").toString()).intValue();
        int femaleCount = Integer.valueOf(pd.get("female_count").toString()).intValue();
//        int weed_out_total_count = Integer.valueOf(pd.getString("weed_out_total_count"));

//        DecimalFormat dDf =new java.text.DecimalFormat("#.00");
//        double maleWeight = Double.valueOf(dDf.format(Double.valueOf(pd.getString("male_weight"))));
//        double femaleWeight = Double.valueOf(dDf.format(Double.valueOf(pd.getString("female_weight"))));
//        double weed_out_total_weight = Double.valueOf(dDf.format(Double.valueOf(pd.getString("weed_out_total_weight"))));
//        List<PageData> taoTaiList = getTaoTaiData(pd);
//        int taoTaiCount = 0;
//        for(int i=0;i<taoTaiList.size();i++){
//        	taoTaiCount = taoTaiCount + Integer.valueOf(taoTaiList.get(i).get("weed_out_total_count").toString()).intValue();
//        }

        if( (currMaleCount + currFemaleCount) >= (maleCount + femaleCount)) {
//                if((currMaleCount*maleWeight + currFemaleCount*femaleWeight) == (maleCount*maleWeight + femaleCount*femaleWeight + weed_out_total_weight)){
                    int i = 0;
//                    pd.put("batch_no",getBatchId(pd));
                    i = (Integer) dao.save("BatchManageMapper.insertOverBatchDataToHis", pd);
                    i *= (Integer) dao.update("BatchManageMapper.updateOverBatchDataToCurr", pd);
                    dao.update("BatchManageMapper.updateOverBatchDataToDetail", pd);
                    if(i == 1){
                        rt.put("result",true);
                        rt.put("msg","出栏批次成功！");
                        pd.put("house_code", null);
                        //获取进鸡数据
                        List<PageData> list2 = getCreateBatchData(pd);
                        int gross_chicken_number=0,gross_male_chicken_number=0;
                        float gross_chicken_weight=0,gross_male_chicken_weight=0;
                        //获取出栏数据
                        pd.put("house_code", null);
                        List<PageData> list4 = getOverBatchData(pd);
                        for(int j=0;j<list4.size();j++){
                        	if(Integer.valueOf(list4.get(j).get("female_count").toString()).intValue() != Integer.valueOf(list4.get(j).get("weed_out_total_count").toString()).intValue()){
                        	gross_chicken_number = gross_chicken_number+Integer.valueOf(list4.get(j).get("female_count").toString()).intValue()+Integer.valueOf(list4.get(j).get("weed_out_total_count").toString()).intValue();
                        	gross_male_chicken_number = gross_male_chicken_number+Integer.valueOf(list4.get(j).get("male_count").toString()).intValue()+Integer.valueOf(list4.get(j).get("weed_out_male_total_count").toString()).intValue();
                        	}else{
                        		gross_chicken_number = gross_chicken_number+Integer.valueOf(list4.get(j).get("female_count").toString()).intValue();
                        		gross_male_chicken_number = gross_male_chicken_number+Integer.valueOf(list4.get(j).get("male_count").toString()).intValue();
                        	}
                        	gross_chicken_weight = gross_chicken_weight+Float.valueOf(list4.get(j).get("female_weight").toString()).floatValue()*Integer.valueOf(list4.get(j).get("female_count").toString()).intValue()+Float.valueOf(list4.get(j).get("weed_out_total_weight").toString()).floatValue();
                        	gross_male_chicken_weight = gross_male_chicken_weight+Float.valueOf(list4.get(j).get("male_weight").toString()).floatValue()*Integer.valueOf(list4.get(j).get("male_count").toString()).intValue()+Float.valueOf(list4.get(j).get("weed_out_male_total_weight").toString()).floatValue();
                        }
                        if(list2.size()==0){
                        	pd.put("out_datetime", pd.get("operation_date"));
                        	pd.put("gross_chicken_number", gross_chicken_number);
                        	pd.put("gross_chicken_weight", gross_chicken_weight);
                        	pd.put("gross_male_chicken_number", gross_male_chicken_number);
                        	pd.put("gross_male_chicken_weight", gross_male_chicken_weight);
                        	i *= (Integer) dao.save("BatchManageMapper.insertSettleData", pd);
                        	List<PageData> list6 = getSettleData2(pd);
                        	PageData pd3 = new PageData();
                        	pd3.put("settle_id", list6.get(0).get("id"));
                    		pd3.put("farm_id", pd.get("farm_id"));
                    		pd3.put("settle_type", 1);
                    		pd3.put("good_munber", pd.get("gross_chicken_number"));
                    		pd3.put("good_weight", pd.get("gross_chicken_weight"));
                    		pd3.put("f_m_flag", 0);
                    		List<PageData> corporationGoodList3 = getCorporationGood(pd3);
                    		if(corporationGoodList3.size()>0){
                    		pd3.put("good_price", corporationGoodList3.get(0).get("chicken_price"));
                    		pd3.put("good_total_price", Float.valueOf(corporationGoodList3.get(0).get("price").toString()).floatValue()*Float.valueOf(pd.get("gross_chicken_weight").toString()).floatValue());
                    		}
                    		pd3.put("user_id", pd.get("user_id"));
                    		i *= (Integer) dao.save("BatchManageMapper.insertSettleSubData", pd3);
                        	
                        	pd.put("send_batch_no", pd.get("batch_no"));
                        	List<PageData> list5 = selectSumByGoodId(pd);
                        	for(int g =0;g<list5.size();g++){
                        		PageData pd2 = new PageData();
                        		pd2.put("settle_id", list6.get(0).get("id"));
                        		pd2.put("farm_id", pd.get("farm_id"));
                        		if(Integer.valueOf(list5.get(g).get("good_type").toString()).intValue()==6){
                        			pd2.put("settle_type", 3);
                        		}else{
                        			pd2.put("settle_type", 4);
                        		}
                        		pd2.put("good_id", list5.get(g).get("goods_id"));
                        		List<PageData> list7 = getGoodsList(pd2);
                        		pd2.put("good_name", list7.get(0).get("good_name"));
                        		pd2.put("good_munber", list5.get(g).get("sumCount"));
                        		pd2.put("f_m_flag", 0);
                        		List<PageData> corporationGoodList = moduleService.service("googsServiceImpl","getCorporationGood",new Object[]{pd2});
                        		if(corporationGoodList.size()>0){
                        		pd2.put("good_price", corporationGoodList.get(0).get("price"));
                        		pd2.put("good_total_price", Float.valueOf(corporationGoodList.get(0).get("price").toString()).floatValue()*Float.valueOf(list5.get(g).get("sumCount").toString()).floatValue());
                        		pd2.put("good_unit", corporationGoodList.get(0).get("unit"));
                        		pd2.put("spec", corporationGoodList.get(0).get("spec"));
                        		}
                        		pd2.put("user_id", pd.get("user_id"));
                        		i *= (Integer) dao.save("BatchManageMapper.insertSettleSubData", pd2);
                        	}
                        	
                        	pd.put("child_batch_no", pd.get("batch_no"));
                        	PageData list8 = selectSumjimiao(pd);
                        	if(list8 != null){
                        		PageData pd2 = new PageData();
                        		pd2.put("settle_id", list6.get(0).get("id"));
                        		pd2.put("farm_id", pd.get("farm_id"));
                        		pd2.put("settle_type", 2);
                        		pd2.put("good_id", list8.get("variety_id"));
//                        		List<PageData> list7 = getGoodsList(pd2);
                        		pd2.put("good_name", list8.get("variety"));
                        		pd2.put("good_munber", list8.get("sumCount"));
                        		pd2.put("f_m_flag", 0);
                        		List<PageData> corporationGoodList = getCorporationGood(pd2);
                        		if(corporationGoodList.size()>0){
                        		pd2.put("good_price", corporationGoodList.get(0).get("price"));
                        		pd2.put("good_total_price", Float.valueOf(corporationGoodList.get(0).get("price").toString()).floatValue()*Float.valueOf(list8.get("sumCount").toString()).floatValue());
                        		pd2.put("good_unit", corporationGoodList.get(0).get("unit"));
                        		pd2.put("spec", corporationGoodList.get(0).get("spec"));
                        		}
                        		pd2.put("user_id", pd.get("user_id"));
                        		i *= (Integer) dao.save("BatchManageMapper.insertSettleSubData", pd2);
                        	}
                        	
                        	if(i != 1){
                        		rt.put("result",false);
                                rt.put("msg","未知错误，请联系管理员！");
                        	}
                        }
                    } else{
                        rt.put("result",false);
                        rt.put("msg","未知错误，请联系管理员！");
                    }
//                } else{
//                    rt.put("result",false);
//                    rt.put("msg","出栏重量及淘汰重量不等于存栏重量，请重新输入重量！");
//                }
        } else{
            rt.put("result",false);
            rt.put("msg","出栏数量及淘汰数量不等于存栏数量，请重新输入数量！");
        }
        return rt;
    }
    
    /**
     * 获取结算数据
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getSettleData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectSettleData", pd);
    }
    
    public List<PageData> getSettleData2(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectSettleData2", pd);
    }
    
    /**
     * 保存结算数据
     * @return
     * @throws Exception
     */
    public int saveSettleData(PageData pd) throws Exception{
      return (int) dao.save("BatchManageMapper.insertSettleData", pd);
    }
    
    /**
     * 修改结算数据
     * @return
     * @throws Exception
     */
    public int editSettleData(PageData pd) throws Exception{
      return (int) dao.save("BatchManageMapper.updateSettleData", pd);
    }
    
    /**
     * 获取结算子数据
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getSettleSubData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("BatchManageMapper.selectSettleSubData", pd);
    }
    
    /**
     * 保存结算子数据
     * @return
     * @throws Exception
     */
    public int saveSettleSubData(PageData pd) throws Exception{
      return (int) dao.save("BatchManageMapper.insertSettleSubData", pd);
    }
    
    /**
     * 修改结算子数据
     * @return
     * @throws Exception
     */
    public int editSettleSubData(PageData pd) throws Exception{
      return (int) dao.save("BatchManageMapper.updateSettleSubData", pd);
    }
    
    public void deleteSettleSubData(PageData pd) throws Exception{
		   dao.delete("BatchManageMapper.deleteSettleSubData", pd);
	}
    
    /**
     * 结算总金额
     * @return
     * @throws Exception
     */
    public PageData priceCount(PageData pd) throws Exception{
      return (PageData) dao.findForObject("BatchManageMapper.priceCount", pd);
    }
    
    /**
     * 结算总金额
     * @return
     * @throws Exception
     */
    public List<PageData> priceCount2(PageData pd) throws Exception{
      return (List<PageData>) dao.findForList("BatchManageMapper.priceCount2", pd);
    }
    
    /**
     * 结算总数量和总重量
     * @return
     * @throws Exception
     */
    public PageData dataSum(PageData pd) throws Exception{
      return (PageData) dao.findForObject("BatchManageMapper.dataSum", pd);
    }

    /**
     * 获取预计饲养天数
     * @param houseType
     * @return
     */
    int getBreedDays(String houseType){
        return ("1".equals(houseType))?175:(("2".equals(houseType))?455:80);
    }

    /**
     * 生成批次ID
     * @param pd
     * @return
     */
    String getBatchId(PageData pd){
        return pd.getString("farm_id") + "-" + pd.getString("house_code") + "-" + pd.getString("batch_no");
    }

    public PageData selectBatchDataForMobile(PageData pd) throws Exception{
        return (PageData) dao.findForObject("BatchManageMapper.selectBatchDataForMobile", pd);
    }
    
    public List<PageData> selectSBCode(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("BatchManageMapper.selectSBCode", pd);
	}
    
    public List<PageData> getGoodsList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getGoodsList", pd);
	}
    
    public List<PageData> getGoodId(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.selectGoodId", pd);
	}
    
    /**
     * 鸡苗数量
     * @return
     * @throws Exception
     */
    public PageData selectSumjimiao(PageData pd) throws Exception{
      return (PageData) dao.findForObject("BatchManageMapper.selectSumjimiao", pd);
    }
    
    /**
     * 根据农场、批次、品种id查询数量
     * @return
     * @throws Exception
     */
    public List<PageData> selectSumByGoodId(PageData pd) throws Exception{
      return (List<PageData>) dao.findForList("BatchManageMapper.selectSumByGoodId", pd);
    }
    
    /**
     * 饲料数量
     * @return
     * @throws Exception
     */
    public PageData selectSumsiliao(PageData pd) throws Exception{
      return (PageData) dao.findForObject("BatchManageMapper.selectSumsiliao", pd);
    }
    
    /**
     * 苗药数量
     * @return
     * @throws Exception
     */
    public PageData selectSummiaoyao(PageData pd) throws Exception{
      return (PageData) dao.findForObject("BatchManageMapper.selectSummiaoyao", pd);
    }
    
    /*** 养殖主数据    */
    public List<PageData> getGoodsList2(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getGoodsList2", pd);
	}
    
    public PageData saveGoods(PageData pd) throws Exception {
    	PageData rt = new PageData();
    	rt.put("result",true);
        rt.put("msg",1);
    	int i = 0;
		i = (Integer)dao.save("BatchManageMapper.saveGoods", pd);
		pd.put("code_type", "FEED_TYPE");
		pd.put("biz_code", pd.get("good_code"));
		pd.put("code_name", pd.get("good_name"));
		pd.put("bak1", "");
		pd.put("bak2", "");
		pd.put("code_desc", "饲养品种");
		i *= (Integer)dao.save("SDCodeMapper.saveCode", pd);
		if(i != 1){
    		rt.put("result",false);
            rt.put("msg",0);
    	}
		return rt;
	}
	
    public PageData updateGoods(PageData pd) throws Exception {
    	PageData rt = new PageData();
    	rt.put("result",true);
        rt.put("msg",1);
    	int i = 0;
    	i = (Integer)dao.update("BatchManageMapper.editGoods", pd);
    	   dao.update("BatchManageMapper.editCorporationGood", pd);
    	pd.put("code_type", "FEED_TYPE");
    	pd.put("biz_code", pd.get("good_code"));
    	PageData code = codeManageService.findCodeInfo(pd);
    	pd.putAll(code);
    	pd.put("code_name", pd.get("good_name"));
    	i *= (Integer)dao.update("SDCodeMapper.editCode", pd);
    	if(i != 1){
    		rt.put("result",false);
            rt.put("msg",0);
    	}
    	return rt;
    	
	}
    
    public List<PageData> getCorporationGood(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getCorporationGood", pd);
	}
    
    public int saveCorporationGoods(PageData pd) throws Exception {
		return (Integer) dao.save("BatchManageMapper.saveCorporationGoods", pd);
	}
    
    public int editCorporationGood(PageData pd) throws Exception {
		return (Integer)dao.update("BatchManageMapper.editCorporationGood", pd);
	}
    
    public void deleteCorporationGoods(PageData pd) throws Exception{
		   dao.delete("BatchManageMapper.deleteCorporationGoods", pd);
	}
    
    public List<PageData> getSpec(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getSpec", pd);
	}
	
	public List<PageData> getUnit(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getUnit", pd);
	}
	
	public List<PageData> getCorporation(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getCorporation", pd);
	}
	
	public List<PageData> getCorporation2(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getCorporation2", pd);
	}
	
	public List<PageData> getCorporation3(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getCorporation3", pd);
	} 
	
	public List<PageData> getFactory(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getFactory", pd);
	}
	public List<PageData> getFactory2(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getFactory2", pd);
	}
	public List<PageData> getFactory3(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BatchManageMapper.getFactory3", pd);
	}

    @Override
    public HashMap<String, Integer> getGoodsMap() throws Exception{
        List<PageData> goodslist = getGoodsList(new PageData());
        HashMap<String, Integer> goodsMap = new HashMap<String, Integer>();
        for (int i = 0; i < goodslist.size(); i++) {
            PageData pageData2 = goodslist.get(i);
            int goodsId = Integer.valueOf(String.valueOf(pageData2.get("good_code")));
            String goodsName = String.valueOf(pageData2.get("good_name"));
            goodsMap.put(goodsName, goodsId);
        }
        return goodsMap;
    }

}
