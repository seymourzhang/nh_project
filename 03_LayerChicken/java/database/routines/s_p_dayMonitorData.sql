CREATE PROCEDURE `s_p_dayMonitorData`()
  label_at_start:BEGIN

    /*声明变量，用于异常情况的捕捉*/
    DECLARE p_res VARCHAR(5) DEFAULT 'Succ';
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION ,NOT FOUND SET p_res = 'Fail'; -- 异常捕获
    START TRANSACTION ;

    -- 先删除，后新增
    DELETE from s_b_monitor_hist_day where collect_date = curdate();

    insert into s_b_monitor_hist_day(maketime, farm_id, house_id, collect_date, inside_temp1, inside_temp2, inside_temp3, inside_temp4, inside_temp5, inside_temp6, inside_avg_temp, inside_set_temp, outside_temp, inside_humidity, point_temp_diff, co2, lux_1)
      SELECT now(), tdata.farm_id, tdata.house_id, tdata.timeId, avg(tdata.inside_temp1), avg(tdata.inside_temp2), avg(tdata.inside_temp3), avg(tdata.inside_temp4), avg(tdata.inside_temp5), avg(tdata.inside_temp6),
        avg(tdata.inside_avg_temp), avg(tdata.inside_set_temp), avg(tdata.outside_temp), avg(tdata.inside_humidity), avg(tdata.point_temp_diff), avg(tdata.co2), avg(tdata.lux_1) from (
        SELECT date_format(a.collect_datetime, '%Y-%m-%d') AS timeId,a.* from s_b_monitor_hist a where 1=1
        and a.collect_datetime >= curdate()
        and a.collect_datetime < date_add(curdate(),INTERVAL 1 day)
    )as tdata
    GROUP BY tdata.farm_id,tdata.house_id,tdata.timeId ;


    IF p_res = 'Fail' THEN
        ROLLBACK;
    ELSE
        COMMIT;
    END IF;

    select p_res;
END