delete database summercamp if exists;
create database summercamp;
use summercamp;
drop table comment if exists;
create table comment
(
  id          int auto_increment
    primary key,
  entity_type int          not null,
  entity_id   int          not null,
  user_id     int          not null,
  content     varchar(256) not null,
  pic_url     varchar(256) null,
  status      int          not null,
  create_date datetime     not null,
  like_count  int          not null,
  reply_count int          not null
);

drop table message if exists;
create table message
(
  id              int auto_increment
    primary key,
  content         text        not null,
  from_id         int         not null,
  to_id           int         not null,
  conversation_id varchar(64) not null,
  has_read        int         not null,
  from_delete     int         not null,
  to_delete       int         not null,
  created_date    datetime    not null
);

drop table ticket if exists;
create table ticket
(
  id      int auto_increment
    primary key,
  user_id int          not null,
  ticket  varchar(255) not null,
  status  int          not null,
  expired datetime     not null
);

drop table user if exists;
create table user
(
  id       int auto_increment
    primary key,
  name     varchar(255) not null,
  password varchar(255) not null,
  salt     varchar(255) not null,
  head_url varchar(128) not null,
  email    varchar(128) not null
);

drop table weibo if exists;
create table weibo
(
  id            int auto_increment
    primary key,
  user_id       int          not null,
  comment_count int          not null,
  status        int          not null,
  pic_url       varchar(255) not null,
  created_date  datetime     not null,
  like_count    int          not null,
  content       text         not null
);


