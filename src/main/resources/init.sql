create schema ding_talk collate utf8_general_ci;

create table conversation
(
    id                   bigint not null
        primary key,
    open_conversation_id varchar(255) null,
    chatid               varchar(255) null,
    name                 varchar(255) null,
    constraint name_index
        unique (name)
);

create table if not exists dept
(
    dept_id
    bigint
    not
    null
    primary
    key,
    name
    varchar
(
    255
) null,
    parent_id bigint null,
    creator varchar
(
    255
) null,
    create_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    editor varchar
(
    255
) null,
    edit_time datetime default CURRENT_TIMESTAMP null
                                                        on update CURRENT_TIMESTAMP
    );

create table if not exists user
(
    userid varchar
(
    255
) not null
    primary key,
    job_number varchar
(
    255
) null,
    unionid varchar
(
    255
) null,
    manager_userid varchar
(
    255
) null,
    mobile varchar
(
    255
) null,
    name varchar
(
    255
) null,
    title varchar
(
    255
) null,
    hired_date datetime null,
    workPlace varchar
(
    255
) null,
    org_email varchar
(
    255
) null,
    creator varchar
(
    255
) null,
    create_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    editor varchar
(
    255
) null,
    edit_time datetime default CURRENT_TIMESTAMP null
                                                        on update CURRENT_TIMESTAMP,
    email varchar
(
    255
) null,
    constraint email_index
    unique
(
    email
)
    );

