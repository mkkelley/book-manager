drop rule if exists books_view_insert ON books;
create rule books_view_insert as on insert to books
    do instead insert into books_temporal (author_id, title, published, updated_at, created_by,
                                           updated_by, ended)
               values (new.author_id, new.title, new.published, new.updated_at,
                       new.created_by, new.updated_by, null)
               returning id, author_id, title, published, created_at, updated_at, created_by, updated_by;

create table if not exists deletable
(
    id  text,
    tbl text,
    primary key (id, tbl)
);

drop rule if exists books_view_delete on books;
create rule books_view_delete as on delete TO books
    do instead (
    insert into deletable (id, tbl)
    values (cast(old.id as text), 'books');

    delete
    from deletable
    where id = cast(old.id as text)
      and tbl = 'books';

    update books_temporal
    set ended = now()
    where id = old.id
    returning id, author_id, title, published, created_at, updated_at, created_by, updated_by);

drop rule if exists users_books_notes_view_delete on users_book_notes;
create rule users_books_notes_view_delete AS ON DELETE TO users_book_notes
    DO instead (
    insert into deletable (id, tbl)
    values (cast(old.id as text), 'users_book_notes');

    delete
    from deletable
    where id = cast(old.id as text)
      and tbl = 'books';

    update users_book_notes_temporal
    set ended = NOW()
    where id = old.id);
