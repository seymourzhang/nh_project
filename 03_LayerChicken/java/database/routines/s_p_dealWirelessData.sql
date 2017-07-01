
DROP PROCEDURE s_p_dealWirelessData;
CREATE PROCEDURE `s_p_dealWirelessData`()
  label_at_start:BEGIN

    /*声明变量，用于异常情况的捕捉*/
    DECLARE p_res VARCHAR(5) DEFAULT 'Succ';
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET p_res = 'Fail'; -- 异常捕获
    START TRANSACTION ;

    /*
    一、准备新数据
    */
        --  清除工作表
        TRUNCATE table s_b_monitor_curr_wireless01 ;
        TRUNCATE table s_b_monitor_curr_wireless02 ;
        --  准备数据
        INSERT into s_b_monitor_curr_wireless01 (farm_id, houseid, collect_datetime, update_datetime, deal_status, date_age, week_age,
                                  inside_temp1, inside_temp2, inside_temp3,
                                               /*inside_temp4, */
                                               inside_temp5, inside_temp6, inside_temp7, inside_temp8, inside_temp9, inside_temp10,
                                  inside_avg_temp, inside_set_temp, high_alarm_temp, low_alarm_temp, outside_temp, inside_humidity,
                                  point_temp_diff, target_humidity, co2, power_status, lux_1)
        SELECT
          c.farm_id, c.house_id,now(),now(),0,ifnull(s_f_getDayAgeByHouseId(c.house_id, CURDATE()),0) AS age,null,
          (select s_f_get_formatNum(sddc.inside_temp1) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1000_1' LIMIT 1) as temp1,
          (select s_f_get_formatNum(sddc.inside_temp2) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1000_2' LIMIT 1) as temp2,
          (select s_f_get_formatNum(sddc.inside_temp3) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1000_3' LIMIT 1) as temp3,
          (select s_f_get_formatNum(sddc.inside_temp4) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1000_4' LIMIT 1) as temp5,
          (select s_f_get_formatNum(sddc.inside_temp5) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1000_5' LIMIT 1) as temp6,
          (select s_f_get_formatNum(sddc.inside_temp6) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1000_6' LIMIT 1) as temp7,
          (select s_f_get_formatNum(sddc.inside_temp7) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1000_7' LIMIT 1) as temp8,
          (select s_f_get_formatNum(sddc.inside_temp8) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1000_8' LIMIT 1) as temp9,
          (select s_f_get_formatNum(sddc.inside_temp9) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1000_9' LIMIT 1) as temp10,
          null as avg_temp,
          d.target_temp AS set_temp,
          d.max_temp AS high_alarm_temp,
          d.min_temp AS low_alarm_temp,
          (select s_f_get_formatNum(sddc.outside_temp) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1005_1' LIMIT 1) as temp_out,
          (select s_f_get_formatNum(sddc.humidity) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1010_1' LIMIT 1) as humidity,
          null as point_temp_diff,
          0 AS set_humidity,
          (select s_f_get_formatNum(sddc.CO2) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1040_1' LIMIT 1) as CO2,
          (SELECT case when substr(right(sdd.power_status,2),1,1) = 0 then 1 else 0 end from s_d_device sdd where sdd.device_code = c.device_code LIMIT 1) as power_status,
          (select s_f_get_formatNum(sddc.lux) from s_d_device_data_cur sddc where sddc.device_id = c.device_code and port_id = '1110_1' LIMIT 1) as lux
        FROM s_b_devi_house c
          LEFT JOIN s_b_layer_house_alarm d ON d.house_id = c.house_id
        WHERE 1=1
        and exists(select 1 from s_d_device sdd where sdd.device_code = c.device_code and sdd.device_type = '4' and sdd.modify_date > date_sub(now(),INTERVAL 10 MINUTE)) ;

        -- 补充 平均、高报、点温差、断电状态字段
        INSERT into s_b_monitor_curr_wireless02 (farm_id, houseid, collect_datetime, update_datetime, deal_status, date_age, week_age,
                                  inside_temp1, inside_temp2, inside_temp3,inside_temp4,inside_temp5, inside_temp6, inside_temp7, inside_temp8, inside_temp9, inside_temp10,
                                  inside_avg_temp, inside_set_temp, high_alarm_temp, low_alarm_temp, outside_temp, inside_humidity,
                                  point_temp_diff, target_humidity, co2, power_status, lux_1)
        SELECT
          a.farm_id,a.houseid,a.collect_datetime,a.update_datetime,a.deal_status,a.date_age,a.week_age,
          a.inside_temp1,a.inside_temp2,a.inside_temp3,a.inside_temp4,a.inside_temp5,a.inside_temp6,a.inside_temp7,a.inside_temp8,a.inside_temp9,a.inside_temp10,
          (ifnull(a.t1,0) + ifnull(a.t2,0) + ifnull(a.t3,0) + ifnull(a.t4,0) + ifnull(a.t5,0) + ifnull(a.t6,0)) / a.probe_nums as inside_avg_temp,
          a.inside_set_temp,a.ha,a.low_alarm_temp, a.outside_temp, a.inside_humidity,
          CASE when
              greatest(ifnull(a.t1,-999),ifnull(a.t2,-999),ifnull(a.t3,-999),ifnull(a.t4,-999),ifnull(a.t5,-999),ifnull(a.t6,-999)) -
              least(ifnull(a.t1,999),ifnull(a.t2,999),ifnull(a.t3,999),ifnull(a.t4,999),ifnull(a.t5,999),ifnull(a.t6,999)) >= 0
                then
              greatest(ifnull(a.t1,-999),ifnull(a.t2,-999),ifnull(a.t3,-999),ifnull(a.t4,-999),ifnull(a.t5,-999),ifnull(a.t6,-999)) -
              least(ifnull(a.t1,999),ifnull(a.t2,999),ifnull(a.t3,999),ifnull(a.t4,999),ifnull(a.t5,999),ifnull(a.t6,999))
          else null end as point_temp_diff,
          a.target_humidity, a.co2, a.power_status, a.lux_1
        from (
          SELECT
          CASE when locate('tempLeft1',probe.probeinfos) > 0 then sbw.inside_temp1 else null end as t1,
          CASE when locate('tempLeft2',probe.probeinfos) > 0 then sbw.inside_temp2 else null end as t2,
          CASE when locate('tempMiddle1',probe.probeinfos) > 0 then sbw.inside_temp3 else null end as t3,
          CASE when locate('tempMiddle2',probe.probeinfos) > 0 then sbw.inside_temp4 else null end as t4,
          CASE when locate('tempRight1',probe.probeinfos) > 0 then sbw.inside_temp5 else null end as t5,
          CASE when locate('tempRight2',probe.probeinfos) > 0 then sbw.inside_temp6 else null end as t6,
          CASE when sba.temp_cpsation = 1 and sbw.outside_temp is not null then greatest(sbw.outside_temp + sba.temp_cordon,sbw.high_alarm_temp) else sbw.high_alarm_temp end as ha,
          (
            CASE WHEN locate('tempLeft1', probe.probeinfos) > 0 and sbw.inside_temp1 is not NULL
              THEN 1
            ELSE 0 END +
            CASE WHEN locate('tempLeft2', probe.probeinfos) > 0 and sbw.inside_temp2 is not NULL
              THEN 1
            ELSE 0 END +
            CASE WHEN locate('tempMiddle1', probe.probeinfos) > 0 and sbw.inside_temp3 is not NULL
              THEN 1
            ELSE 0 END +
            CASE WHEN locate('tempMiddle2', probe.probeinfos) > 0 and sbw.inside_temp4 is not NULL
              THEN 1
            ELSE 0 END +
            CASE WHEN locate('tempRight1', probe.probeinfos) > 0 and sbw.inside_temp5 is not NULL
              THEN 1
            ELSE 0 END +
            CASE WHEN locate('tempRight2', probe.probeinfos) > 0 and sbw.inside_temp6 is not NULL
              THEN 1
            ELSE 0 END
          ) as probe_nums,
            sbw.*
        from s_b_monitor_curr_wireless01 sbw
          LEFT JOIN (select e.house_id, group_concat(DISTINCT e.probe_code) as probeinfos from s_b_house_probe e GROUP BY e.house_id) as probe on 1=1 and probe.house_id = sbw.houseid
          LEFT JOIN s_b_layer_house_alarm sba on sba.house_id = sbw.houseid
        ) as a;

        COMMIT;

    /*
      二、插入 s_b_monitor_curr 表
    */

    -- 删除就数据
    delete from s_b_monitor_curr where 1=1
           and exists(
              SELECT 1 from s_b_monitor_curr_wireless02 c where house_id = c.houseid
           );

    INSERT into s_b_monitor_curr
    (farm_id, house_id, collect_datetime, update_datetime, deal_status, date_age, week_age, inside_temp1, inside_temp2, inside_temp3, inside_temp4, inside_temp5, inside_temp6, inside_temp7, inside_temp8, inside_temp9, inside_temp10, inside_temp11, inside_temp12, inside_temp13, inside_temp14, inside_temp15, inside_temp16, inside_temp17, inside_temp18, inside_temp19, inside_temp20, inside_avg_temp, inside_set_temp, high_alarm_temp, low_alarm_temp, outside_temp, inside_humidity, inside_humidity2, inside_humidity3, point_temp_diff, target_humidity, co2, co2_2, co2_3, power_status, lux_1, lux_2, lux_3, NH4_1, NH4_2, NH4_3, pressure_1, pressure_2, pressure_3)
    SELECT farm_id, houseid, collect_datetime, update_datetime, deal_status, date_age, week_age, inside_temp1, inside_temp2, inside_temp3, inside_temp4, inside_temp5, inside_temp6, inside_temp7, inside_temp8, inside_temp9, inside_temp10, inside_temp11, inside_temp12, inside_temp13, inside_temp14, inside_temp15, inside_temp16, inside_temp17, inside_temp18, inside_temp19, inside_temp20, inside_avg_temp, inside_set_temp, high_alarm_temp, low_alarm_temp, outside_temp, inside_humidity, inside_humidity2, inside_humidity3, point_temp_diff, target_humidity, co2, co2_2, co2_3, power_status, lux_1, lux_2, lux_3, NH4_1, NH4_2, NH4_3, pressure_1, pressure_2, pressure_3
    from s_b_monitor_curr_wireless02 ;

    INSERT into s_b_monitor_hist
      SELECT * from s_b_monitor_curr where 1=1 and exists(
              SELECT 1 from s_b_monitor_curr_wireless02 c where house_id = c.houseid
           );

    IF p_res = 'Fail' THEN
        ROLLBACK;
    ELSE
        COMMIT;
    END IF;

    select p_res;
END