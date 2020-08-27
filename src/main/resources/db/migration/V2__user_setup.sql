create table users
(
    username text    not null primary key,
    password text    not null,
    enabled  boolean not null
);

create table authorities
(
    username  text not null references users (username),
    authority text not null,
    unique (username, authority)
);