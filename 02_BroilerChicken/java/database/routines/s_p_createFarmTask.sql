DROP PROCEDURE IF EXISTS sparrow.s_p_createFarmTask;
CREATE DEFINER=`phoe`@`%` PROCEDURE `s_p_createFarmTask`(in_apply_flag VARCHAR(20) ,in in_farm_id int)
label_at_start:BEGIN
    /*����˵����
        1��in_apply_flag='All'������ȫ��ũ��; in_apply_flag='Single'�����õ���ũ��;
        2��in_FarmId����in_apply_flagΪAllʱ���˱����������ã����Դ�null����in_apply_flagΪSingleʱ����Ҫ����ũ��Id
    */
    DECLARE p_cur_time DATETIME;
    /*���������������쳣����Ĳ�׽*/
    DECLARE p_res VARCHAR(5) DEFAULT 'Succ';
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION ,SQLWARNING ,NOT FOUND SET p_res = 'Fail'; -- �쳣����
    START TRANSACTION ;
        
    IF in_apply_flag = 'All' then
        INSERT INTO s_b_farm_task(id, farm_id, task_source, task_code, task_name, task_type, task_status, age_infos, bak1, bak2, bak3, bak4, create_person, create_date, create_time, modify_person, modify_date, modify_time)
        SELECT null,b.id,'01',a.task_code,a.task_name,a.task_type,a.task_status,a.age_infos,a.bak1,a.bak2,a.bak3,a.bak4,0,curdate(),now(),0,curdate(),now()
        from s_d_task a INNER join s_d_farm b on b.freeze_status = 0
        where 1=1
        and not exists(SELECT 1 from s_b_farm_task e where a.task_code = e.task_code and e.farm_id = b.id)
        and a.task_status = 'Y' ;
    ELSEIF in_apply_flag = 'Single' THEN
        INSERT INTO s_b_farm_task(id, farm_id, task_source, task_code, task_name, task_type, task_status, age_infos, bak1, bak2, bak3, bak4, create_person, create_date, create_time, modify_person, modify_date, modify_time)
        SELECT null,in_farm_id,'01',a.task_code,a.task_name,a.task_type,a.task_status,a.age_infos,a.bak1,a.bak2,a.bak3,a.bak4,0,curdate(),now(),0,curdate(),now()
        from s_d_task a where 1=1
        and not exists(SELECT 1 from s_b_farm_task b where a.task_code = b.task_code and b.farm_id = in_farm_id)
        and a.task_status = 'Y' ;
    END IF ;
        
    IF p_res = 'Fail' THEN
        ROLLBACK;
    ELSE
        COMMIT;
    END IF;
    select p_res;
END;