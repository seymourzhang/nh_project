alter table s_d_sp_log add COLUMN BAK1 varchar(200) COMMENT '备用字段1';
alter table s_d_sp_log add COLUMN BAK2 varchar(200) COMMENT '备用字段2';
alter table s_d_sp_log add COLUMN BAK3 varchar(200) COMMENT '备用字段3';
alter table s_d_sp_log add COLUMN BAK4 varchar(200) COMMENT '备用字段4';
alter table s_d_sp_log add COLUMN BAK5 varchar(200) COMMENT '备用字段5';


create PROCEDURE SP_RECORD_LOG(in spName VARCHAR(50), in beginTime DATETIME, in endTime DATETIME, in result VARCHAR(50), in errCode VARCHAR(5), in errMsg VARCHAR(400), in bak1 VARCHAR(200), in bak2 VARCHAR(200), in bak3 VARCHAR(200), in bak4 VARCHAR(200), in bak5 VARCHAR(200))
  BEGIN
    INSERT INTO s_d_sp_log(SP_NAME, BEGIN_TIME, END_TIME, RESULT, ERR_CODE, ERR_MSG, BAK1, BAK2, BAK3, BAK4, BAK5)
      VALUES (spName, date_format(beginTime,"%Y-%m-%d %H:%i:%s"), date_format(endTime,"%Y-%m-%d %H:%i:%s"), result, errCode, errMsg, bak1, bak2, bak3, bak4, bak5);
    COMMIT ;
  END;