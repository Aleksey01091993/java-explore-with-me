drop table if exists compilation cascade;
drop table if exists requests cascade;

create table if not exists stats
(
    id        bigserial not null,
    app       varchar(255),
    uri       varchar(255),
    ip        varchar(255),
    time_tamp timestamp,
    primary key (id)
);


create table if not exists stats_unique
(
    id        bigserial not null,
    app       varchar(255),
    uri       varchar(255),
    uri_ip    varchar(255) unique,
    time_tamp timestamp,
    primary key (id)
);
