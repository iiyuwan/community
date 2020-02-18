create table user
(
    id           int auto_increment
        primary key,
    account_id   varchar(100) null,
    name         varchar(30)  null,
    token        char(50)     null,
    gmt_create   bigint       null,
    gmt_modified bigint       null,
    avatar_url   varchar(100) null
)
    charset = utf8;