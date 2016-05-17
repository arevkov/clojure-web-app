create database clojure_web_app;

\connect clojure_web_app;

create table users
(
    id bigserial,
    first_name text not null,
    last_name text not null,
    email text not null,
    phone text,
    create_date timestamp(0) not null,
    modify_date timestamp(0) not null,
    gender char(1),

    constraint pk_user primary key( id )
);