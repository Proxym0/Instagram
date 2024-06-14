create table comments
(
    id        bigserial PRIMARY KEY,
    body      text      not null,
    user_id   bigint    not null,
    create_at timestamp not null,
    constraint user_id foreign key references user (id),
    constraint post_id foreign key references post (id)
);

alter table comments
    owner to postgres;