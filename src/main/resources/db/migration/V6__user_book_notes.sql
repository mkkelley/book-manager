create table users_book_notes_temporal
(
    id       uuid not null,
    username text not null references users (username),
    book_id  int  not null references books (id),
    notes    text not null,
    ended    timestamp,
    primary key (id, ended)
);

create procedure add_audit_columns(tbl text)
    language plpgsql
as
$$
begin
    execute (
        concat('alter table ', tbl,
               ' add column IF NOT EXISTS created_at timestamp not null default now();')
        );
    execute (concat('alter table ', tbl,
                    ' add column if not exists created_by text not null references users (username);')
        );
    execute (concat('alter table ', tbl,
                    ' add column if not exists updated_at timestamp;')
        );
    execute (concat('alter table ', tbl,
                    ' add column if not exists updated_by text references users (username);')
        );
end;
$$;

call add_audit_columns('users_book_notes_temporal');

create view users_book_notes as
select id,
       username,
       book_id,
       notes,
       created_at,
       created_by,
       updated_at,
       updated_by
from users_book_notes_temporal
where ended is null;

create rule users_books_notes_view_insert AS ON INSERT TO users_book_notes
    DO instead insert into users_book_notes_temporal
               (id, username, book_id, notes, ended, created_at, created_by, updated_at, updated_by)
               VALUES (new.id, new.username, new.book_id, new.notes, null, new.created_at,
                       new.created_by, new.updated_at, new.updated_by);
create rule users_books_notes_view_delete AS ON DELETE TO users_book_notes
    DO instead
    update users_book_notes_temporal
    set ended = NOW()
    where id = old.id;
create rule users_book_notes_view_update AS ON UPDATE TO users_book_notes
    do instead
    (update users_book_notes_temporal
     set ended = NOW()
     where id = old.id;
    insert into users_book_notes_temporal
    (id, username, book_id, notes, ended, created_at, created_by, updated_at, updated_by)
    values (old.id, new.username, new.book_id, new.notes, null, new.created_at, new.created_by,
            new.updated_at, new.updated_by)
    );
