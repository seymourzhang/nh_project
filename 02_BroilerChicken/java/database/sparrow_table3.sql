drop table if exists s_b_house_breed_tsk;

/*==============================================================*/
/* Table: s_b_house_breed_tsk                                   */
/*==============================================================*/
create table s_b_house_breed_tsk
(
   id                   int not null auto_increment,
   house_id             int not null,
   farm_breed_id        int not null,
   age                  int,
   task_id              int not null,
   task_code            varchar(8) not null,
   task_name            varchar(500) not null,
   task_type            varchar(2) not null comment '01-���ǰ׼��
            02-������ճ�����
            03-���ճ���
            04-������ҩ����
            05-������ʱ����',
   deal_status          varchar(2) not null comment '00-δ����
            01-���
            02-�ӳ�
            03-ȡ��',
   deal_time            datetime,
   deal_log             varchar(500),
   bak1                 varchar(20),
   bak2                 varchar(20),
   bak3                 varchar(500),
   bak4                 varchar(500),
   create_person        int not null,
   create_date          datetime not null,
   create_time          timestamp not null default CURRENT_TIMESTAMP,
   modify_person        int not null,
   modify_date          datetime not null,
   modify_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   primary key (id)
);


drop table if exists s_b_farm_task;

/*==============================================================*/
/* Table: s_b_farm_task                                         */
/*==============================================================*/
create table s_b_farm_task
(
   id                   int not null auto_increment,
   farm_id              int not null,
   task_source          varchar(2) not null comment '01-��׼ģ��
            02-�Զ���',
   task_code            varchar(8) not null,
   task_name            varchar(500) not null,
   task_type            varchar(2) not null comment '01-���ǰ׼��
            02-������ճ�����
            03-���ճ���
            04-������ҩ����
            05-������ʱ����',
   task_status          varchar(1) not null comment 'Y-��Ч
            N-��Ч',
   age_infos            varchar(500) not null,
   bak1                 varchar(20),
   bak2                 varchar(20),
   bak3                 varchar(500),
   bak4                 varchar(500),
   create_person        int not null,
   create_date          datetime not null,
   create_time          timestamp not null default CURRENT_TIMESTAMP,
   modify_person        int not null,
   modify_date          datetime not null,
   modify_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   primary key (id)
);

drop table if exists s_d_task;

/*==============================================================*/
/* Table: s_d_task                                              */
/*==============================================================*/
create table s_d_task
(
   task_code            varchar(8) not null,
   task_name            varchar(500) not null,
   task_type            varchar(2) not null comment '01-���ǰ׼��
            02-������ճ�����
            03-���ճ���
            04-������ҩ����
            05-������ʱ����',
   task_status          varchar(1) not null comment 'Y-��Ч
            N-��Ч',
   age_infos            varchar(500) not null,
   bak1                 varchar(20),
   bak2                 varchar(20),
   bak3                 varchar(500),
   bak4                 varchar(500),
   create_person        int not null,
   create_date          datetime not null,
   create_time          timestamp not null default CURRENT_TIMESTAMP,
   modify_person        int not null,
   modify_date          datetime not null,
   modify_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   primary key (task_code)
);

drop table if exists s_d_serialno;

/*==============================================================*/
/* Table: s_d_serialno                                          */
/*==============================================================*/
create table s_d_serialno
(
   IncreID              int not null auto_increment,
   primary key (IncreID)
);


drop table if exists s_b_chicken_standar;

/*==============================================================*/
/* Table: s_b_chicken_standar                                   */
/*==============================================================*/
create table s_b_chicken_standar
(
   id                   int not null auto_increment,
   farm_id              int not null,
   feed_type            varchar(40) not null comment 'cock-����
            hen-ĸ��
            mixed-��ĸ����',
   age                  int,
   weight_per           numeric(16,2),
   weight_diff_per      numeric(16,2),
   feed_conversion      numeric(16,2),
   acc_feed             numeric(16,2),
   create_person        int not null,
   create_date          datetime not null,
   create_time          timestamp not null default CURRENT_TIMESTAMP,
   modify_person        int not null,
   modify_date          datetime not null,
   modify_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   primary key (id, farm_id, feed_type)
);



drop table if exists s_d_user_operation;

/*==============================================================*/
/* Table: s_d_user_operation                                    */
/*==============================================================*/
create table s_d_user_operation
(
   id                   int not null auto_increment,
   person_id            int not null,
   menu_code            varchar(10) not null,
   operation_type       varchar(10) not null comment 'select
            update
            add
            delete',
   create_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);



drop table if exists s_l_jpush_log;

/*==============================================================*/
/* Table: s_l_jpush_log                                         */
/*==============================================================*/
create table s_l_jpush_log
(
   id                   int not null auto_increment,
   content              varchar(500) not null,
   tag_name             varchar(100) not null,
   house_id             int not null,
   alarm_id             numeric(16,2) not null,
   push_date            datetime,
   res_flag             varchar(5) comment 'succ-�ɹ�
            fail-ʧ��',
   res_detail           varchar(500),
   make_date            datetime not null,
   modify_date          datetime not null,
   primary key (id)
);
