drop table if exists users cascade;
drop table if exists categories cascade;
drop table if exists comments cascade;
drop table if exists events cascade;
drop table if exists compilation_events cascade;
drop table if exists compilation cascade;
drop table if exists requests cascade;

create table if not exists users
(
    id    bigserial not null,
    email varchar(255) unique,
    name  varchar(250),
    primary key (id)
);

create table if not exists categories
(
    id   bigserial not null,
    name varchar(255) unique,
    primary key (id)
);

create table if not exists events
(
    id                 bigserial not null,
    annotation         varchar(2000),
    confirmed_request  int4,
    created_on         timestamp,
    description        varchar(7000),
    event_date         timestamp,
    lat                float4,
    lon                float4,
    paid               boolean,
    participant_limit  int4,
    published_on       timestamp,
    request_moderation boolean,
    state              varchar(255),
    title              varchar(120),
    views              int4,
    category_id        int8,
    initiator_id       int8,
    primary key (id),
    foreign key (category_id)
        references categories,
    foreign key (initiator_id)
        references users
);

create table if not exists comments
(
    id           bigserial not null,
    created_time timestamp,
    text         varchar(7000),
    author_id    int8,
    event_id     int8      not null,
    primary key (id),
    foreign key (author_id)
        references users,
    foreign key (event_id)
        references events
);

create table if not exists compilation
(
    id     bigserial not null,
    pinned boolean,
    title  varchar(50),
    primary key (id)
);

create table if not exists compilation_events
(
    compilation_id int8 not null,
    events_id      int8,
    foreign key (events_id)
        references events,
    foreign key (compilation_id)
        references compilation
);

create table if not exists requests
(
    id           bigserial not null,
    created      timestamp,
    status       varchar(255),
    event_id     int8,
    requester_id int8,
    primary key (id),
    foreign key (event_id)
        references events,
    foreign key (requester_id)
        references users
);