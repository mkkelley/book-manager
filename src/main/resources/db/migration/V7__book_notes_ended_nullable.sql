alter table users_book_notes_temporal
    drop constraint users_book_notes_temporal_pkey;
alter table users_book_notes_temporal
    add constraint users_book_notes_temporal_uq unique (id, ended);
alter table users_book_notes_temporal
    alter column ended drop NOT NULL;