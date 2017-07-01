package com.nh.ifarm.analyze.service.impl;

import com.nh.ifarm.analyze.service.DailyService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2016/11/30.
 */
@Service
public class DailyServiceImpl implements DailyService {

    @SuppressWarnings("restriction")
	@Resource(name = "daoSupport")
	private DaoSupport dao;

    public int dailySave(PageData pd) throws Exception {
        int result = 0;
        try {
            int cullingMale = pd.getInteger("culling_num_male");
            int cullingFemale = pd.getInteger("culling_num_female");
            int deathMale = pd.getInteger("death_num_male");
            int deathFemale = pd.getInteger("death_num_female");
            int genderErrorMale = pd.getInteger("gender_error_male");
            int genderErrorFemale = pd.getInteger("gender_error_female");

            PageData temp = (PageData) dao.findForObject("DailyMapper.selectBySpecialDate", pd);
            int checkMale = temp.getInteger("male_count");
            int checkFemale = temp.getInteger("female_count");
            int dayAge = temp.getInteger("age");

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String growthDate = temp.get("growth_date").toString();
            Date specialDate = sdf.parse(growthDate);
            Date curDate = sdf.parse(sdf.format(date));

            int maleCountDiff = checkFemale - cullingFemale - deathFemale;
            int femaleCountDiff = checkMale - deathMale - cullingMale;
            if (maleCountDiff < 0 || femaleCountDiff < 0) {
                result = -1;
            } else {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                pd.put("service_id",df.format(new Date()).toString());
                pd.put("create_date", new Date());
                pd.put("create_time", new Date());
                pd.put("modify_date", new Date());
                pd.put("modify_time", new Date());
                int maleOld = temp.getInteger("male_culling_am") + temp.getInteger("male_death_am") + temp.getInteger("male_culling_pm") + temp.getInteger("male_death_pm");
                int femaleOld = temp.getInteger("female_culling_am") + temp.getInteger("female_death_am") + temp.getInteger("female_culling_pm") + temp.getInteger("female_death_pm");
                int maleCullingOld = temp.getInteger("male_culling_am") + temp.getInteger("male_culling_pm");
                int femaleCullingOld = temp.getInteger("female_culling_am") + temp.getInteger("female_culling_pm");
                int maleDeathOld = temp.getInteger("male_death_am") + temp.getInteger("male_death_pm");
                int femaleDeathOld = temp.getInteger("female_death_am") + temp.getInteger("female_death_pm");
                int genderErrorMaleDiff = temp.getInteger("male_mistake") - genderErrorMale;
                int genderErrorFemaleDiff = temp.getInteger("female_mistake") - genderErrorFemale;
                int diffMale = maleOld - deathMale - cullingMale;
                int diffFemale = femaleOld - deathFemale - cullingFemale;
                pd.put("maleCurCd", deathMale + cullingMale);
                pd.put("femaleCurCd", deathFemale + cullingFemale);
                pd.put("femaleDiff", diffFemale);
                pd.put("maleDiff", diffMale);
                if (cullingFemale + cullingMale != 0) {
                    pd.put("operation_date", growthDate);
                    pd.put("operation_type", "6");
                    pd.put("male_count", maleCullingOld - cullingMale);
                    pd.put("female_count", femaleCullingOld - cullingFemale);
                    pd.put("bak", "损耗数量");
                    dao.save("DailyMapper.insertDaily", pd);
                }
                if (deathFemale + deathMale != 0) {
                    pd.put("operation_date", growthDate);
                    pd.put("operation_type", "5");
                    pd.put("male_count", maleDeathOld - deathMale);
                    pd.put("female_count", femaleDeathOld - deathFemale);
                    pd.put("bak", "淘汰数量");
                    dao.save("DailyMapper.insertDaily", pd);
                }
                if (genderErrorMale + genderErrorFemale != 0) {
                    pd.put("operation_date", growthDate);
                    pd.put("operation_type", "8");
                    pd.put("male_count", genderErrorMaleDiff);
                    pd.put("female_count", genderErrorFemaleDiff);
                    pd.put("bak", "鉴别错误");
                    dao.save("DailyMapper.insertDaily", pd);
                }

                PageData param2 = new PageData();


                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                Date tDate = new Date();
                tDate.setHours(0);
                tDate.setMinutes(0);
                tDate.setSeconds(0);
                param2.put("begin_date", df2.format(tDate));
                tDate.setHours(23);
                tDate.setMinutes(59);
                tDate.setSeconds(59);
                param2.put("end_date", df2.format(tDate));

                if (curDate.equals(specialDate)) {
                    dao.save("DailyMapper.batchCurUpdate", pd);
                    dao.save("DailyMapper.batchCurSave", pd);
                    dao.save("DailyMapper.updateCurrCount", pd);

                    dao.save("BatchManageMapper.exec_SP_BREED_DETAIL", param2);
//                    dao.save("BatchManageMapper.exec_SP_BREED_DETAIL_WEEK", param2);
                } else if (specialDate.before(curDate)) {
                    dao.save("DailyMapper.batchOldUpdate", pd);
                    dao.save("DailyMapper.batchOldSave", pd);
                    dao.save("DailyMapper.updateCurrCount", pd);


                    dao.save("BatchManageMapper.exec_SP_BREED_DETAIL", param2);
//                    dao.save("BatchManageMapper.exec_SP_BREED_DETAIL_WEEK", param2);
                } else if (specialDate.after(curDate)) {
                    result = -2;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            result = -3;
        }
        return result;
    }

    public List<PageData> dailyQuery(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("DailyMapper.selectDailyByHouse", pd);
    }

    public PageData selectBySpecialDate(PageData pd) throws Exception{
        return (PageData) dao.findForObject("DailyMapper.selectBySpecialDate", pd);
    }

    public PageData selectDate(PageData pd) throws Exception{
        return (PageData) dao.findForObject("DailyMapper.selectDate", pd);
    }

    public List<PageData> selectDailyReport(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("DailyMapper.selectDailyReport", pd);
    }

    public List<PageData> selectWeeklyReport(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("DailyMapper.selectWeekReport", pd);
    }
}
