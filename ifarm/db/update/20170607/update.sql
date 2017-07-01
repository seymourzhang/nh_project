alter table s_b_alarm_inco add COLUMN is_notice int default 1 comment '1.通知 0.不通知';
alter table s_d_stock_change_remind add COLUMN is_notice int default 1 comment '1.通知 0.不通知';