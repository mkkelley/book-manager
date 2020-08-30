alter table books
    add ended timestamp;

alter table books
    rename to books_temporal;

create view books as
select id,
       author_id,
       title,
       published,
       created_at,
       updated_at,
       created_by,
       updated_by
from books_temporal
where ended is null;

create rule books_view_insert as on insert to books
    do instead insert into books_temporal (author_id, title, published, updated_at, created_by,
                                           updated_by, ended)
               values (new.author_id, new.title, new.published, new.updated_at,
                       new.created_by, new.updated_by, null);
create rule books_view_delete as on delete to books
    do instead update books_temporal
               set ended = now()
               where id = old.id;

create rule books_view_update as on update to books
    do instead (
    update books_temporal
    set ended = now()
    where id = old.id;
    insert into books_temporal (id, author_id, title, published, updated_at, created_by, updated_by,
                                ended)
    values (old.id, new.author_id, new.title, new.published, new.updated_at, new.created_by,
            new.updated_by, null)
    );