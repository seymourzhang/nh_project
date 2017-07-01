drop table if exists s_b_meat_std;

/*==============================================================*/
/* Table: s_b_meat_std                                          */
/*==============================================================*/
create table s_b_meat_std
(
   id                   int not null auto_increment comment '序号',
   variety_id           int comment '品种id',
   variety              varchar(50) comment '品种',
   grow_age             int comment '生长日龄',
   female_avg_weed_out  numeric(20,6) comment '母鸡平均死淘率',
   female_total_weed_out numeric(20,6) comment '母鸡累计死淘率',
   male_avg_weed_out    numeric(20,6) comment '公鸡平均死淘率',
   male_total_weed_out  numeric(20,6) comment '公鸡累计死淘率',
   male_weight          numeric(20,6) comment '公的均重',
   female_weight        numeric(20,6) comment '母的均重',
   male_feed_daliy      numeric(20,6) comment '公只日采食量',
   female_feed_daliy    numeric(20,6) comment '母只日采食量',
   female_total_feed    numeric(20,6) comment '母累计饲料耗量',
   male_total_feed      numeric(20,6) comment '公累计饲料耗量',
   create_person        int comment '创建人',
   create_date          datetime comment '创建日期',
   create_time          timestamp comment '创建时间',
   modify_person        int,
   modify_date          datetime comment '创建日期',
   modify_time          datetime,
   primary key (id)
);
