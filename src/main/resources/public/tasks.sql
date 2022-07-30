create table tasks
(
    id          integer not null
        primary key,
    deadline    date,
    description varchar(255),
    header      varchar(255),
    status      integer,
    user_id     integer
        constraint fk6s1ob9k4ihi75xbxe2w0ylsdh
            references users
);

alter table tasks
    owner to sa;

