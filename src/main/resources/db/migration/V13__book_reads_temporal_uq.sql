ALTER TABLE users_book_reads_temporal
    DROP CONSTRAINT users_book_reads_pkey;
ALTER TABLE users_book_reads_temporal
    ADD CONSTRAINT users_books_reads_temporal_uq UNIQUE (id, ended);