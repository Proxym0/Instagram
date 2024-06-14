create table likes
(
    id        bigserial PRIMARY KEY,
    user_id   bigint    not null,
    post_id   bigint    not null,
    create_at timestamp not null,
    constraint user_id  foreign key references user(id),
    constraint post_id   foreign key references post(id)
);

alter table likes
    owner to postgres;