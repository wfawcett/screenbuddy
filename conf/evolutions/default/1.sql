# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table amazon (
  id                            bigint auto_increment not null,
  title_id                      integer not null,
  last_seen                     datetime(6),
  constraint pk_amazon primary key (id)
);

create table redbox (
  id                            bigint auto_increment not null,
  title_id                      integer not null,
  last_seen                     datetime(6),
  constraint pk_redbox primary key (id)
);

create table request (
  id                            bigint auto_increment not null,
  title_id                      integer not null,
  user_id                       integer not null,
  constraint pk_request primary key (id)
);

create table service (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_service primary key (id)
);

create table title (
  id                            bigint auto_increment not null,
  tmdb_id                       integer not null,
  backdrop_path                 varchar(255),
  original_language             varchar(255),
  overview                      varchar(255),
  poster_path                   varchar(255),
  release_date                  datetime(6),
  status                        varchar(255),
  tagline                       varchar(255),
  title                         varchar(255),
  vote_average                  double,
  vote_count                    integer not null,
  popularity                    double,
  constraint pk_title primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  hash_pass                     varchar(255),
  name                          varchar(255),
  constraint pk_user primary key (id)
);

create table user_service (
  id                            bigint auto_increment not null,
  user_id                       integer not null,
  service_id                    integer not null,
  constraint pk_user_service primary key (id)
);


# --- !Downs

drop table if exists amazon;

drop table if exists redbox;

drop table if exists request;

drop table if exists service;

drop table if exists title;

drop table if exists user;

drop table if exists user_service;

