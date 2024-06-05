create table posts
(
    id          bigserial,
    description text,
    url         varchar   not null,
    user_id     bigint    not null,
    create_at   timestamp not null
);

alter table posts
    owner to postgres;