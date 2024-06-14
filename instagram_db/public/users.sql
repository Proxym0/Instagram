create table users
(
    id        bigserial PRIMARY KEY,
    username  varchar   not null,
    password  varchar,
    email     varchar   not null,
    fullname  varchar   not null,
    create_at timestamp not null,
    update_at timestamp
);

alter table users
    owner to postgres;