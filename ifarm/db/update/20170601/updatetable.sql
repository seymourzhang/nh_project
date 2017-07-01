alter table s_b_batch_change modify COLUMN weed_out_male_total_count int;
alter table s_b_farm_settle add COLUMN gross_male_chicken_number int comment '公毛鸡数量';
alter table s_b_farm_settle add COLUMN gross_male_chicken_weight numeric(20,6) comment '公毛鸡总重量';