-- 接口信息
create table if not exists ny_api.`ny_api_interface`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '接口名称',
    `create_user_id` bigint not null comment '创建人id',
    `description` varchar(512) null comment '描述',
    `request_url` varchar(512) not null comment '请求地址',
    `massage` varchar(256) null comment '请求类型',
    `request_header` text null comment '请求头',
    `response_header` text null comment '请求体',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)',
    `status` tinyint default 0 not null comment '接口状态，0-关闭，1-打开'
    ) comment '接口信息';

insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('武黎昕', 9067, 'tr6Cq', 'www.creola-bauch.org', 'PR0d', 'QN', 'GT7c9', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('任伟宸', 15228539, 'Lb2', 'www.piedad-schmeler.io', 'IshYY', '9t', 'MxD1', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('徐浩然', 4857208864, 'TfF', 'www.brenton-davis.name', 'yf', '2S', 'Yj', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('林文轩', 18258, 'Hg', 'www.archie-leffler.org', 'PDi4', 'ld', 'h3FK6', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('蒋潇然', 81548, 'h04Y', 'www.dante-koepp.com', 'M1sou', 'xJ8Km', 'dW', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('尹晓啸', 9, '0ln1E', 'www.eduardo-schowalter.io', 'ce', 'oOR7', 'HIDD', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('李远航', 9575035, 'LHQWn', 'www.fidel-paucek.org', '9bEFE', 'r1Zdp', 'gMKEd', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('韩弘文', 959419327, 'bT', 'www.bari-cruickshank.biz', 'jD', '2CPYB', 'tfm6x', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('杜鸿煊', 1354521218, 'XZ55', 'www.linnea-kerluke.com', 'Nx', '8xLg', 'iH', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('郑立轩', 6421747, 'rbV', 'www.yolando-wisozk.net', 'TsSev', 'li3Rn', 'F5Vgy', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('孔鹏涛', 819, 'SFc', 'www.miquel-friesen.biz', 'fDXP', 'gR', '6Y6U', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('顾志泽', 30987, 'Ux38', 'www.ervin-romaguera.io', 'Rjxkh', 'h6Fv', 'F2', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('阎智辉', 21437212, 'Fv5T', 'www.alex-beier.co', 'Xu', 'b8Es', 'BHbWD', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('丁浩轩', 137847156, 'aJJ', 'www.alva-mcdermott.net', 'VCnvm', 'jkR2', 'U1ZX', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('雷烨霖', 4516259, 'jf9F9', 'www.rosina-reynolds.com', 'cDl', 'rqq', 'GQ', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('刘正豪', 715631, 'Tr1', 'www.julius-durgan.com', 'uv475', '36', 'nm', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('武明轩', 1167, 'DbI0', 'www.edison-kihn.co', 'XAjHY', 'pe6', 'xl', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('夏雨泽', 770588, 'ZoM', 'www.tennie-wisoky.info', 'Dq', 'YiT', '12K', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('顾博文', 8559116957, 'zV', 'www.elyse-mosciski.biz', 'vn8Uw', 'fT7yP', 'U2mE', 0);
insert into ny_api.`ny_api_interface` (`name`, `create_user_id`, `description`, `request_url`, `massage`, `request_header`, `response_header`, `status`) values ('孙志强', 3, 'Yl', 'www.paris-borer.org', '9x', 'Pqs58', 'eFDf', 0);