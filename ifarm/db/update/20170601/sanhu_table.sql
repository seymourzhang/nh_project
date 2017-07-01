drop table if exists s_d_authority_sh;

/*==============================================================*/
/* Table: s_d_authority_sh                                      */
/*==============================================================*/
create table s_d_authority_sh
(
   ID                   int not null auto_increment comment 'ID',
   authority_name       varchar(50) not null comment '��������',
   authority_desc       varchar(100) not null comment '��������',
   create_person        int not null comment '������',
   create_date          datetime not null comment '��������',
   create_time          timestamp not null comment '����ʱ��',
   modify_person        int not null comment '�޸���',
   modify_date          datetime not null comment '�޸�����',
   modify_time          timestamp not null comment '�޸�ʱ��',
   primary key (ID)
);


drop table if exists s_d_user_role_sh;

/*==============================================================*/
/* Table: s_d_user_role_sh                                      */
/*==============================================================*/
create table s_d_user_role_sh
(
   id                   int not null auto_increment comment '���к�',
   user_id              int not null comment '�û�id',
   user_code            varchar(50),
   role_id              int not null comment '��ɫid',
   author_id            int,
   bak                  varchar(100),
   create_person        int not null comment '������',
   create_date          datetime not null comment '��������',
   create_time          timestamp not null comment '����ʱ��',
   modify_person        int not null comment '�޸���',
   modify_date          datetime not null comment '�޸�����',
   modify_time          timestamp not null comment '�޸�ʱ��',
   primary key (id)
);


drop table if exists s_d_roles_sh;

/*==============================================================*/
/* Table: s_d_roles_sh                                          */
/*==============================================================*/
create table s_d_roles_sh
(
   role_id              int not null auto_increment comment '��ɫid',
   role_name            varchar(50) not null comment '��ɫ����',
   create_person        int not null comment '������',
   create_date          datetime not null comment '��������',
   create_time          timestamp not null comment '����ʱ��',
   modify_person        int not null comment '�޸���',
   modify_date          datetime not null comment '�޸�����',
   modify_time          timestamp not null comment '�޸�ʱ��',
   primary key (role_id)
);


drop table if exists s_b_user_house_sh;

/*==============================================================*/
/* Table: s_b_user_house_sh                                     */
/*==============================================================*/
create table s_b_user_house_sh
(
   id                   int not null auto_increment comment '���к�',
   user_id              int not null comment '�û�id',
   user_code            varchar(50) comment '�û�����',
   farm_id              int not null comment 'ũ��id',
   house_id             int not null comment '����id',
   create_person        int not null comment '������',
   create_date          datetime not null comment '��������',
   create_time          timestamp not null comment '����ʱ��',
   modify_person        int not null comment '�޸���',
   modify_date          datetime not null comment '�޸�����',
   modify_time          timestamp not null comment '�޸�ʱ��',
   primary key (id)
);


drop table if exists s_d_user_sh;

/*==============================================================*/
/* Table: s_d_user_sh                                           */
/*==============================================================*/
create table s_d_user_sh
(
   id                   int not null comment '�û�id',
   user_code            varchar(50) not null comment '�û���',
   user_real_name       varchar(500) comment '������',
   user_real_name_en    varchar(500) comment 'Ӣ����',
   user_password        varchar(50) not null comment '����',
   user_mobile_1        varchar(50) comment '�ֻ�1',
   user_mobile_2        varchar(50) comment '�ֻ�2',
   user_tel_1           varchar(50) comment '�̻�1',
   user_tel_2           varchar(50) comment '�̻�2',
   user_email           varchar(500) comment '����',
   user_status          varchar(50) not null comment '�û�״̬
            1-����',
   freeze_status        varchar(50) not null comment '����״̬
            1-��ɾ��
            0-δɾ��',
   memo_1               varchar(500) comment '�����ֶ�1',
   memo_2               varchar(500) comment '�����ֶ�2',
   memo_3               varchar(500) comment '�����ֶ�3',
   memo_4               varchar(500) comment '�����ֶ�4',
   create_person        int not null comment '������',
   create_date          datetime not null comment '��������',
   create_time          timestamp not null comment '����ʱ��',
   modify_person        int not null comment '�޸���',
   modify_date          datetime not null comment '�޸�����',
   modify_time          timestamp not null comment '�޸�ʱ��',
   primary key (id)
);


drop table if exists s_b_user_farm_sh;

/*==============================================================*/
/* Table: s_b_user_farm_sh                                      */
/*==============================================================*/
create table s_b_user_farm_sh
(
   id                   int not null auto_increment comment '���к�',
   user_id              int not null comment '�û�id',
   user_code            varchar(50) comment '�û�����',
   farm_id              int not null comment 'ũ��id',
   create_person        int not null comment '������',
   create_date          datetime not null comment '��������',
   create_time          timestamp not null comment '����ʱ��',
   modify_person        int not null comment '�޸���',
   modify_date          datetime not null comment '�޸�����',
   modify_time          timestamp not null comment '�޸�ʱ��',
   primary key (id)
);
