-- 接口信息
create table ny_api_interface
(
    id             bigint auto_increment comment '主键'
        primary key,
    name           varchar(256)                       not null comment '接口名称',
    userId         bigint                             not null comment '创建人id',
    description    varchar(512)                       null comment '描述',
    url            varchar(512)                       not null comment '请求地址',
    method         varchar(256)                       null comment '请求类型',
    requestHeader  text                               null comment '请求头',
    responseHeader text                               null comment '请求体',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDeleted      tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)',
    status         tinyint  default 0                 not null comment '接口状态，0-关闭，1-打开'
)
    comment '接口信息';

-- 用户接口关系表
create table ny_user_api_interface
(
    id             bigint auto_increment comment '主键'  primary key,
    userId         bigint                             not null comment '调用人id',
    interfaceId         bigint                       not null comment '接口id',
    totalNum                int     default 0                         not null comment '总调用次数',
    leftNum                int     default 0                         not null comment '剩余调用次数',
    status         tinyint  default 0                 not null comment '调用状态，0-正常，1-禁用',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDeleted      tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'

)
    comment '用户接口关系表';

-- 用户信息表
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(512)                           not null comment '密码',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    accessKey    varchar(512)                           null comment 'accessKey',
    secretKey    varchar(512)                           null comment 'secretKey',
    constraint uni_userAccount
        unique (userAccount)
)
    comment '用户';
