DROP RULE IF EXISTS books_view_delete ON books;
DROP RULE IF EXISTS books_view_insert ON books;
DROP VIEW IF EXISTS users_book_notes;
DROP VIEW IF EXISTS users_book_reads;
DROP VIEW IF EXISTS books;

ALTER TABLE books_temporal
    ALTER created_at TYPE timestamptz;
ALTER TABLE books_temporal
    ALTER updated_at TYPE timestamptz;
ALTER TABLE books_temporal
    ALTER ended TYPE timestamptz;
ALTER TABLE users_book_notes_temporal
    ALTER created_at TYPE timestamptz;
ALTER TABLE users_book_notes_temporal
    ALTER updated_at TYPE timestamptz;
ALTER TABLE users_book_notes_temporal
    ALTER ended TYPE timestamptz;
ALTER TABLE authors
    ALTER created_at TYPE timestamptz;
ALTER TABLE authors
    ALTER updated_at TYPE timestamptz;
ALTER TABLE users_book_reads_temporal
    ALTER started TYPE timestamptz;
ALTER TABLE users_book_reads_temporal
    ALTER finished TYPE timestamptz;
ALTER TABLE users_book_reads_temporal
    ALTER ended TYPE timestamptz;
ALTER TABLE users_book_reads_temporal
    ALTER created_at TYPE timestamptz;
ALTER TABLE users_book_reads_temporal
    ALTER updated_at TYPE timestamptz;

-- region recreate_users_book_notes
CREATE OR REPLACE VIEW users_book_notes
            (id, username, book_id, notes, created_at, created_by, updated_at, updated_by) AS
SELECT users_book_notes_temporal.id,
       users_book_notes_temporal.username,
       bt.id AS book_id,
       users_book_notes_temporal.notes,
       users_book_notes_temporal.created_at,
       users_book_notes_temporal.created_by,
       users_book_notes_temporal.updated_at,
       users_book_notes_temporal.updated_by
FROM users_book_notes_temporal
         JOIN books_temporal bt ON users_book_notes_temporal.book_internal_id = bt.internal_id
WHERE users_book_notes_temporal.ended IS NULL;

CREATE RULE users_books_notes_view_delete AS
    ON DELETE TO users_book_notes DO INSTEAD ( INSERT INTO deletable (id, tbl)
                                               VALUES (old.id::text, 'users_book_notes'::text);
    DELETE
    FROM deletable
    WHERE deletable.id = old.id::text
      AND deletable.tbl = 'users_book_notes'::text;
    UPDATE users_book_notes_temporal
    SET ended = now()
    WHERE users_book_notes_temporal.id = old.id
      AND users_book_notes_temporal.ended IS NULL;
    );

CREATE TRIGGER users_book_notes_update
    INSTEAD OF UPDATE
    ON users_book_notes
    FOR EACH ROW
EXECUTE PROCEDURE update_users_book_notes_temporal();

CREATE TRIGGER users_book_notes_temporal_insert
    INSTEAD OF INSERT
    ON users_book_notes
    FOR EACH ROW
EXECUTE PROCEDURE insert_users_book_notes_temporal();
-- endregion

-- region recreate_users_book_reads
CREATE OR REPLACE VIEW users_book_reads
            (id, username, book_id, started, finished, created_at, created_by, updated_at,
             updated_by, audiobook)
AS
SELECT users_book_reads_temporal.id,
       users_book_reads_temporal.username,
       bt.id AS book_id,
       users_book_reads_temporal.started,
       users_book_reads_temporal.finished,
       users_book_reads_temporal.created_at,
       users_book_reads_temporal.created_by,
       users_book_reads_temporal.updated_at,
       users_book_reads_temporal.updated_by,
       users_book_reads_temporal.audiobook
FROM users_book_reads_temporal
         JOIN books_temporal bt ON bt.internal_id = users_book_reads_temporal.book_internal_id
WHERE users_book_reads_temporal.ended IS NULL;

CREATE RULE users_book_reads_delete AS
    ON DELETE TO users_book_reads DO INSTEAD ( INSERT INTO deletable (id, tbl)
                                               VALUES (old.id::text, 'users_book_reads'::text);
    DELETE
    FROM deletable
    WHERE deletable.id = old.id::text
      AND deletable.tbl = 'users_book_reads'::text;
    UPDATE users_book_reads_temporal
    SET ended = now()
    WHERE users_book_reads_temporal.id = old.id
      AND users_book_reads_temporal.ended IS NULL;
    );

CREATE TRIGGER users_book_reads_update
    INSTEAD OF UPDATE
    ON users_book_reads
    FOR EACH ROW
EXECUTE PROCEDURE update_users_book_reads_temporal();

CREATE TRIGGER users_book_reads_temporal_insert
    INSTEAD OF INSERT
    ON users_book_reads
    FOR EACH ROW
EXECUTE PROCEDURE insert_users_book_reads_temporal();
-- endregion

-- region recreate_books
CREATE OR REPLACE VIEW books
            (id, author_id, title, published, created_at, updated_at, created_by, updated_by) AS
SELECT books_temporal.id,
       books_temporal.author_id,
       books_temporal.title,
       books_temporal.published,
       books_temporal.created_at,
       books_temporal.updated_at,
       books_temporal.created_by,
       books_temporal.updated_by
FROM books_temporal
WHERE books_temporal.ended IS NULL;

CREATE RULE books_view_insert AS
    ON INSERT TO books DO INSTEAD INSERT INTO books_temporal (author_id, title, published,
                                                              updated_at, created_by, updated_by,
                                                              ended)
                                  VALUES (new.author_id, new.title, new.published, new.updated_at,
                                          new.created_by, new.updated_by,
                                          NULL::timestamp WITHOUT TIME ZONE)
                                  RETURNING books_temporal.id, books_temporal.author_id, books_temporal.title, books_temporal.published, books_temporal.created_at, books_temporal.updated_at, books_temporal.created_by, books_temporal.updated_by;

CREATE RULE books_view_delete AS
    ON DELETE TO books DO INSTEAD ( INSERT INTO deletable (id, tbl)
                                    VALUES (old.id::text, 'books'::text);
    DELETE
    FROM deletable
    WHERE deletable.id = old.id::text
      AND deletable.tbl = 'books'::text;
    DELETE
    FROM users_book_reads
    WHERE users_book_reads.book_id = old.id;
    UPDATE books_temporal
    SET ended = now()
    WHERE books_temporal.id = old.id
      AND books_temporal.ended IS NULL
    RETURNING books_temporal.id, books_temporal.author_id, books_temporal.title, books_temporal.published, books_temporal.created_at, books_temporal.updated_at, books_temporal.created_by, books_temporal.updated_by;
    );

CREATE TRIGGER books_update
    INSTEAD OF UPDATE
    ON books
    FOR EACH ROW
EXECUTE PROCEDURE update_books_temporal();
-- endregion

