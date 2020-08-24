create table authors (
    id serial primary key,
    name text,
    created_at timestamp default now() not null,
    updated_at timestamp
);

create table books (
    id serial primary key,
    author_id int references authors(id),
    title text,
    published date,
    created_at timestamp default now() not null,
    updated_at timestamp
);