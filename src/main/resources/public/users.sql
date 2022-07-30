create table users
(
    id   integer not null
        primary key,
    name varchar(255)
);

alter table users
    owner to sa;

