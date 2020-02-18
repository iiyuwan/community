create table comment
(
	id bigint auto_increment,
	parent_id bigint not null,
	type int null comment '父类id',
	commentator int not null comment '评论人',
	gmt_create bigint not null comment '创建时间',
	gmt_modified bigint not null comment '更新时间',
	like_count bigint default 0 null comment '点赞数',
	constraint comment_pk
		primary key (id)
);

/* mvn flyway:migrate */