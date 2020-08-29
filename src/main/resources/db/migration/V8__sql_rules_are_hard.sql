drop rule if exists users_books_notes_view_insert on users_book_notes;
create rule users_books_notes_view_insert AS ON INSERT TO users_book_notes
    DO instead insert into users_book_notes_temporal
               (id, username, book_id, notes, ended, created_by, updated_at, updated_by)
               VALUES (new.id, new.username, new.book_id, new.notes, null, new.created_by,
                       new.updated_at, new.updated_by);
drop rule if exists users_book_notes_view_update on users_book_notes;
drop rule if exists users_books_notes_view_update on users_book_notes;
create rule users_books_notes_view_update AS ON UPDATE TO users_book_notes
    do instead
    (update users_book_notes_temporal
     set ended = NOW()
     where id = old.id;
    insert into users_book_notes_temporal
    (id, username, book_id, notes, ended, created_by, updated_at, updated_by)
    values (old.id, new.username, new.book_id, new.notes, null, new.created_by,
            new.updated_at, new.updated_by)
    );
