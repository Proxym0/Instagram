create table posts
(
    id          bigserial, PRIMARY KEY,
    description text,
    url         varchar   not null,
    user_id     bigint    not null,
    create_at   timestamp not null,
    constraint user_id  foreign key references user(id)
);

alter table posts
    owner to postgres;