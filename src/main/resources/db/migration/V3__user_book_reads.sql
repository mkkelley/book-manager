create table users_book_reads
(
    username   text      not null references users (username),
    book_id    int       not null references books (id),
    started    timestamp,
    finished   timestamp,
    created_at timestamp not null default now(),
    created_by text      not null references users (username),
    updated_at timestamp,
    updated_by text references users (username)
);

alter table authors
    add created_by text not null references users (username) default 'michael';
alter table authors
    alter column created_by drop default;
alter table authors
    add updated_by text references users (username) default 'michael';
alter table authors
    alter column updated_by drop default;

alter table books
    add created_by text not null references users (username) default 'michael';
alter table books
    alter column created_by drop default;
alter table books
    add updated_by text references users (username) default 'michael';
alter table books
    alter column updated_by drop default;
