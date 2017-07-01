drop table if exists s_d_authority_sh;

/*==============================================================*/
/* Table: s_d_authority_sh                                      */
/*==============================================================*/
create table s_d_authority_sh
(
   ID                   int not null auto_increment comment 'ID',
   authority_name       varchar(50) not null comment '级别名称',
   authority_desc       varchar(100) not null comment '级别内容',
   create_person        int not null comment '创建人',
   create_date          datetime not null comment '创建日期',
   create_time          timestamp not null comment '创建时间',
   modify_person        int not null comment '修改人',
   modify_date          datetime not null comment '修改日期',
   modify_time          timestamp not null comment '修改时间',
   primary key (ID)
);


drop table if exists s_d_user_role_sh;

/*==============================================================*/
/* Table: s_d_user_role_sh                                      */
/*==============================================================*/
create table s_d_user_role_sh
(
   id                   int not null auto_increment comment '序列号',
   user_id              int not null comment '用户id',
   user_code            varchar(50),
   role_id              int not null comment '角色id',
   author_id            int,
   bak                  varchar(100),
   create_person        int not null comment '创建人',
   create_date          datetime not null comment '创建日期',
   create_time          timestamp not null comment '创建时间',
   modify_person        int not null comment '修改人',
   modify_date          datetime not null comment '修改日期',
   modify_time          timestamp not null comment '修改时间',
   primary key (id)
);


drop table if exists s_d_roles_sh;

/*==============================================================*/
/* Table: s_d_roles_sh                                          */
/*==============================================================*/
create table s_d_roles_sh
(
   role_id              int not null auto_increment comment '角色id',
   role_name            varchar(50) not null comment '角色名称',
   create_person        int not null comment '创建人',
   create_date          datetime not null comment '创建日期',
   create_time          timestamp not null comment '创建时间',
   modify_person        int not null comment '修改人',
   modify_date          datetime not null comment '修改日期',
   modify_time          timestamp not null comment '修改时间',
   primary key (role_id)
);


drop table if exists s_b_user_house_sh;

/*==============================================================*/
/* Table: s_b_user_house_sh                                     */
/*==============================================================*/
create table s_b_user_house_sh
(
   id                   int not null auto_increment comment '序列号',
   user_id              int not null comment '用户id',
   user_code            varchar(50) comment '用户编码',
   farm_id              int not null comment '农场id',
   house_id             int not null comment '栋舍id',
   create_person        int not null comment '创建人',
   create_date          datetime not null comment '创建日期',
   create_time          timestamp not null comment '创建时间',
   modify_person        int not null comment '修改人',
   modify_date          datetime not null comment '修改日期',
   modify_time          timestamp not null comment '修改时间',
   primary key (id)
);


drop table if exists s_d_user_sh;

/*==============================================================*/
/* Table: s_d_user_sh                                           */
/*==============================================================*/
create table s_d_user_sh
(
   id                   int not null comment '用户id',
   user_code            varchar(50) not null comment '用户名',
   user_real_name       varchar(500) comment '中文名',
   user_real_name_en    varchar(500) comment '英文名',
   user_password        varchar(50) not null comment '密码',
   user_mobile_1        varchar(50) comment '手机1',
   user_mobile_2        varchar(50) comment '手机2',
   user_tel_1           varchar(50) comment '固话1',
   user_tel_2           varchar(50) comment '固话2',
   user_email           varchar(500) comment '邮箱',
   user_status          varchar(50) not null comment '用户状态
            1-正常',
   freeze_status        varchar(50) not null comment '冻结状态
            1-已删除
            0-未删除',
   memo_1               varchar(500) comment '备用字段1',
   memo_2               varchar(500) comment '备用字段2',
   memo_3               varchar(500) comment '备用字段3',
   memo_4               varchar(500) comment '备用字段4',
   create_person        int not null comment '创建人',
   create_date          datetime not null comment '创建日期',
   create_time          timestamp not null comment '创建时间',
   modify_person        int not null comment '修改人',
   modify_date          datetime not null comment '修改日期',
   modify_time          timestamp not null comment '修改时间',
   primary key (id)
);


drop table if exists s_b_user_farm_sh;

/*==============================================================*/
/* Table: s_b_user_farm_sh                                      */
/*==============================================================*/
create table s_b_user_farm_sh
(
   id                   int not null auto_increment comment '序列号',
   user_id              int not null comment '用户id',
   user_code            varchar(50) comment '用户编码',
   farm_id              int not null comment '农场id',
   create_person        int not null comment '创建人',
   create_date          datetime not null comment '创建日期',
   create_time          timestamp not null comment '创建时间',
   modify_person        int not null comment '修改人',
   modify_date          datetime not null comment '修改日期',
   modify_time          timestamp not null comment '修改时间',
   primary key (id)
);
