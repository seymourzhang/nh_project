DROP PROCEDURE IF EXISTS sparrow.s_p_createHouseTask;
CREATE DEFINER=`phoe`@`%` PROCEDURE `s_p_createHouseTask`(in in_farm_id int,in_apply_flag VARCHAR(20),in in_temp_id int)
label_at_start:BEGIN
    /*����˵����
        1��in_farm_id : ũ��Id
        2��in_apply_flag:  All-����ȫ������
                           TempTask-������ʱ����
                           Single-���õ������� 
        3��in_temp_id  :  ��in_apply_flagΪ All ʱ���˱����������ã����Դ�0
                           ��in_apply_flagΪ TempTask ʱ����Ҫ������ʱ����Id
                           ��in_apply_flagΪ Single ʱ����Ҫ���붰��Id
    */
    DECLARE p_cur_time DATETIME;
    DECLARE p_farmBreedId INT;-- ũ����������Id
    
    /*���������������쳣����Ĳ�׽*/
    DECLARE p_res VARCHAR(5) DEFAULT 'Succ';
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION ,SQLWARNING ,NOT FOUND SET p_res = 'Fail'; -- �쳣����
    START TRANSACTION ;
    
    SELECT ifnull(max(id),0) INTO p_farmBreedId from s_b_farm_breed where farm_id = in_farm_id and batch_status = '01' ;
    
    IF in_apply_flag = 'All' then
        INSERT INTO s_b_house_breed_tsk (id, house_id, farm_breed_id,
                                 age, task_id, task_code, task_name, task_type,deal_status, deal_time, deal_log,
                                 bak1, bak2, bak3, bak4,
                                 create_person, create_date, create_time, modify_person, modify_date, modify_time)
        SELECT
            null as id,sdh.id as houseid,p_farmBreedId as farmbreedid,
            (CASE when a.age_infos = '**' then
             b.IncreID-1
             else substring_index(substring_index(a.age_infos,'#',b.IncreID),'#',-1) end) as age,
             a.id as taskid,a.task_code,a.task_name,a.task_type,'00' as dealstatus,null as dealtime,null as deallog,a.bak1,a.bak2,a.bak3,a.bak4,
             0 as createperson,curdate() as createdate,now() as createtime,0 as modifyperson,curdate() as modifydate,now() as modifytime
        from s_b_farm_task a
        join s_d_house sdh on sdh.farm_id = in_farm_id and sdh.freeze_status = 0
        join s_d_serialno b on b.IncreID <= (CASE when a.age_infos = '**' then 99 else (length(a.age_infos)-length(replace(a.age_infos,'#',''))+1) end)
        where 1=1
        and a.farm_id = in_farm_id
        and NOT exists(SELECT 1 from s_b_house_breed_tsk sbh where sbh.farm_breed_id = p_farmBreedId and sbh.house_id = sdh.id and sbh.task_id = a.id)
        and a.task_status = 'Y'
        ORDER BY sdh.id,a.id,age ;
    ELSEIF in_apply_flag = 'TempTask' THEN
        -- ��ɾ����������
        DELETE from s_b_house_breed_tsk where task_id = in_temp_id and farm_breed_id = p_farmBreedId and 
                    exists(SELECT 1 from s_d_house sdh where sdh.id = house_id and sdh.farm_id = in_farm_id) ;
        -- �������µ�
        INSERT INTO s_b_house_breed_tsk (id, house_id, farm_breed_id,
                                 age, task_id, task_code, task_name, task_type,deal_status, deal_time, deal_log,
                                 bak1, bak2, bak3, bak4,
                                 create_person, create_date, create_time, modify_person, modify_date, modify_time)
        SELECT
            null as id,sdh.id as houseid,p_farmBreedId as farmbreedid,
            (CASE when a.age_infos = '**' then
             b.IncreID-1
             else substring_index(substring_index(a.age_infos,'#',b.IncreID),'#',-1) end) as age,
             a.id as taskid,a.task_code,a.task_name,a.task_type,'00' as dealstatus,null as dealtime,null as deallog,a.bak1,a.bak2,a.bak3,a.bak4,
             0 as createperson,curdate() as createdate,now() as createtime,0 as modifyperson,curdate() as modifydate,now() as modifytime
        from s_b_farm_task a
        join s_d_house sdh on sdh.farm_id = in_farm_id and sdh.freeze_status = 0
        join s_d_serialno b on b.IncreID <= (CASE when a.age_infos = '**' then 99 else (length(a.age_infos)-length(replace(a.age_infos,'#',''))+1) end)
        where 1=1
        and a.farm_id = in_farm_id and a.id = in_temp_id
        and a.task_status = 'Y'
        and NOT exists(SELECT 1 from s_b_house_breed_tsk sbh where sbh.farm_breed_id = p_farmBreedId and sbh.house_id = sdh.id and sbh.task_id = in_temp_id)
        ORDER BY sdh.id,a.id,age ;
        
    ELSEIF in_apply_flag = 'Single' THEN
        INSERT INTO s_b_house_breed_tsk (id, house_id, farm_breed_id,
                                 age, task_id, task_code, task_name, task_type, deal_status, deal_time, deal_log,
                                 bak1, bak2, bak3, bak4,
                                 create_person, create_date, create_time, modify_person, modify_date, modify_time)
        SELECT
            null as id,sdh.id as houseid,p_farmBreedId as farmbreedid,
            (CASE when a.age_infos = '**' then
             b.IncreID-1
             else substring_index(substring_index(a.age_infos,'#',b.IncreID),'#',-1) end) as age,
             a.id as taskid,a.task_code,a.task_name,a.task_type,'00' as dealstatus,null as dealtime,null as deallog,a.bak1,a.bak2,a.bak3,a.bak4,
             0 as createperson,curdate() as createdate,now() as createtime,0 as modifyperson,curdate() as modifydate,now() as modifytime
        from s_b_farm_task a
        join s_d_house sdh on sdh.farm_id = in_farm_id and sdh.freeze_status = 0 and sdh.id = in_temp_id
        join s_d_serialno b on b.IncreID <= (CASE when a.age_infos = '**' then 99 else (length(a.age_infos)-length(replace(a.age_infos,'#',''))+1) end)
        where 1=1
        and a.farm_id = in_farm_id
        and NOT exists(SELECT 1 from s_b_house_breed_tsk sbh where sbh.farm_breed_id = p_farmBreedId and sbh.house_id = sdh.id and sbh.task_id = a.id)
        and a.task_status = 'Y'
        ORDER BY sdh.id,a.id,age ;
    END IF;
    
    IF p_res = 'Fail' THEN
        ROLLBACK;
    ELSE
        COMMIT;
    END IF;
    select p_res;
END;