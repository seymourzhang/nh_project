drop table if exists s_b_meat_std;

/*==============================================================*/
/* Table: s_b_meat_std                                          */
/*==============================================================*/
create table s_b_meat_std
(
   id                   int not null auto_increment comment '���',
   variety_id           int comment 'Ʒ��id',
   variety              varchar(50) comment 'Ʒ��',
   grow_age             int comment '��������',
   female_avg_weed_out  numeric(20,6) comment 'ĸ��ƽ��������',
   female_total_weed_out numeric(20,6) comment 'ĸ���ۼ�������',
   male_avg_weed_out    numeric(20,6) comment '����ƽ��������',
   male_total_weed_out  numeric(20,6) comment '�����ۼ�������',
   male_weight          numeric(20,6) comment '���ľ���',
   female_weight        numeric(20,6) comment 'ĸ�ľ���',
   male_feed_daliy      numeric(20,6) comment '��ֻ�ղ�ʳ��',
   female_feed_daliy    numeric(20,6) comment 'ĸֻ�ղ�ʳ��',
   female_total_feed    numeric(20,6) comment 'ĸ�ۼ����Ϻ���',
   male_total_feed      numeric(20,6) comment '���ۼ����Ϻ���',
   create_person        int comment '������',
   create_date          datetime comment '��������',
   create_time          timestamp comment '����ʱ��',
   modify_person        int,
   modify_date          datetime comment '��������',
   modify_time          datetime,
   primary key (id)
);
