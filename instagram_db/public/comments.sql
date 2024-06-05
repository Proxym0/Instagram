create table comments
(
    id        bigserial,
    body      text      not null,
    user_id   bigint    not null,
    create_at timestamp not null
);

alter table comments
    owner to postgres;