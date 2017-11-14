# --- !Ups

create table amazon (
  id                            bigint auto_increment not null,
  title_id                      integer not null,
  last_seen                     datetime(6),
  constraint pk_amazon primary key (id)
);

create table redbox (
  id                            bigint auto_increment not null,
  title_id                      bigint,
  last_seen                     datetime(6),
  soon                          tinyint(1) default 0,
  constraint pk_redbox primary key (id)
);

create table request (
  id                            bigint auto_increment not null,
  title_id                      bigint not null,
  user_id                       bigint not null,
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
  overview                      TEXT,
  poster_path                   varchar(255),
  release_date                  datetime(6),
  release_year                  integer not null,
  status                        varchar(255),
  tagline                       varchar(255),
  original_title                varchar(255),
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
  service_id                    bigint not null,
  user_id                       bigint not null,
  constraint pk_user_service primary key (id)
);

alter table request add constraint fk_request_title_id foreign key (title_id) references title (id) on delete restrict on update restrict;
create index ix_request_title_id on request (title_id);

alter table request add constraint fk_request_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_request_user_id on request (user_id);

alter table user_service add constraint fk_user_service_service_id foreign key (service_id) references service (id) on delete restrict on update restrict;
create index ix_user_service_service_id on user_service (service_id);

alter table user_service add constraint fk_user_service_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_service_user_id on user_service (user_id);


# --- !Downs

alter table request drop foreign key fk_request_title_id;
drop index ix_request_title_id on request;

alter table request drop foreign key fk_request_user_id;
drop index ix_request_user_id on request;

alter table user_service drop foreign key fk_user_service_service_id;
drop index ix_user_service_service_id on user_service;

alter table user_service drop foreign key fk_user_service_user_id;
drop index ix_user_service_user_id on user_service;

drop table if exists amazon;

drop table if exists redbox;

drop table if exists request;

drop table if exists service;

drop table if exists title;

drop table if exists user;

drop table if exists user_service;

