DROP RULE IF EXISTS users_book_reads_update ON users_book_reads;
DROP FUNCTION IF EXISTS update_users_book_reads_temporal CASCADE;
CREATE FUNCTION update_users_book_reads_temporal()
    RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE users_book_reads_temporal SET ended = now() WHERE users_book_reads_temporal.id = old.id;
    INSERT INTO users_book_reads_temporal (username, book_id, started, finished, created_at,
                                           created_by, updated_at, updated_by, audiobook, id, ended)
    VALUES (new.username, new.book_id, new.started, new.finished, new.created_at,
            new.created_by, new.updated_at,
            new.updated_by, new.audiobook, new.id, NULL);
    RETURN new;
END;
$$;

CREATE TRIGGER users_book_reads_update
    INSTEAD OF UPDATE
    ON users_book_reads
    FOR EACH ROW
EXECUTE FUNCTION update_users_book_reads_temporal();

DROP RULE IF EXISTS users_books_notes_view_update ON users_book_notes;
DROP FUNCTION IF EXISTS update_users_book_notes_temporal CASCADE;
CREATE FUNCTION update_users_book_notes_temporal() RETURNS trigger
    LANGUAGE plpgsql AS
$$
BEGIN

    UPDATE users_book_notes_temporal
    SET ended = NOW()
    WHERE id = old.id;
--     new.id = pgcrypto::gen_random_uuid();
    INSERT INTO users_book_notes_temporal
    (id, username, book_id, notes, ended, created_at, created_by, updated_at, updated_by)
    VALUES (new.id, new.username, new.book_id, new.notes, NULL, new.created_at, new.created_by,
            new.updated_at, new.updated_by);
    RETURN new;
END;
$$;

CREATE TRIGGER users_book_notes_update
    INSTEAD OF UPDATE
    ON users_book_notes
    FOR EACH ROW
EXECUTE FUNCTION update_users_book_notes_temporal();

DROP RULE IF EXISTS books_view_update ON books;
DROP FUNCTION IF EXISTS update_books_temporal CASCADE;
CREATE FUNCTION update_books_temporal() RETURNS trigger
    LANGUAGE plpgsql AS
$$
BEGIN
    UPDATE books_temporal
    SET ended = now()
    WHERE id = old.id;
    INSERT INTO books_temporal (author_id, title, published, updated_at, created_by, updated_by,
                                ended)
    VALUES (new.author_id, new.title, new.published, new.updated_at, new.created_by,
            new.updated_by, NULL);
    RETURN new;
END;
$$;

CREATE TRIGGER books_update
    INSTEAD OF UPDATE
    ON books
    FOR EACH ROW
EXECUTE FUNCTION update_books_temporal();

ALTER TABLE books_temporal
    DROP CONSTRAINT books_pkey CASCADE;
ALTER TABLE books_temporal
    ADD CONSTRAINT books_uq UNIQUE (id, ended);
ALTER TABLE books_temporal
    ADD internal_id serial PRIMARY KEY;
ALTER TABLE users_book_reads_temporal
    ADD COLUMN book_internal_id int REFERENCES books_temporal (internal_id);
ALTER TABLE users_book_notes_temporal
    ADD book_internal_id INT REFERENCES books_temporal (internal_id);

CREATE OR REPLACE VIEW users_book_notes
            (id, username, book_id, notes, created_at, created_by, updated_at, updated_by) AS
SELECT users_book_notes_temporal.id,
       users_book_notes_temporal.username,
       bt.id,
       users_book_notes_temporal.notes,
       users_book_notes_temporal.created_at,
       users_book_notes_temporal.created_by,
       users_book_notes_temporal.updated_at,
       users_book_notes_temporal.updated_by
FROM users_book_notes_temporal
         INNER JOIN books_temporal bt ON users_book_notes_temporal.book_internal_id = bt.internal_id
WHERE users_book_notes_temporal.ended IS NULL;

CREATE OR REPLACE VIEW users_book_reads
            (id, username, book_id, started, finished, created_at, created_by, updated_at,
             updated_by, audiobook)
AS
SELECT users_book_reads_temporal.id,
       users_book_reads_temporal.username,
       bt.id,
       users_book_reads_temporal.started,
       users_book_reads_temporal.finished,
       users_book_reads_temporal.created_at,
       users_book_reads_temporal.created_by,
       users_book_reads_temporal.updated_at,
       users_book_reads_temporal.updated_by,
       users_book_reads_temporal.audiobook
FROM users_book_reads_temporal
         INNER JOIN books_temporal bt ON bt.internal_id = users_book_reads_temporal.book_internal_id
WHERE users_book_reads_temporal.ended IS NULL;

ALTER TABLE users_book_reads_temporal
    DROP COLUMN book_id CASCADE ;
ALTER TABLE users_book_notes_temporal
    DROP COLUMN book_id CASCADE ;
