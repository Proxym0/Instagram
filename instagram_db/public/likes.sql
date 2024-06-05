create table likes
(
    id        bigserial,
    user_id   bigint    not null,
    post_id   bigint    not null,
    create_at timestamp not null
);

alter table likes
    owner to postgres;