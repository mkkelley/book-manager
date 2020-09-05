DROP RULE IF EXISTS users_books_notes_view_insert ON users_book_notes;
DROP FUNCTION IF EXISTS insert_users_book_notes_temporal CASCADE;
CREATE FUNCTION insert_users_book_notes_temporal() RETURNS trigger
    LANGUAGE plpgsql AS
$$
BEGIN
    INSERT INTO users_book_notes_temporal
    (id, username, book_internal_id, notes, ended, created_by, updated_at,
     updated_by)
    SELECT new.id,
           new.username,
           b.internal_id,
           new.notes,
           NULL,
           new.created_by,
           new.updated_at,
           new.updated_by
    FROM books_temporal b
    WHERE b.id = new.book_id
      AND b.ended IS NULL;
    RETURN new;
END;
$$;

CREATE TRIGGER users_book_notes_temporal_insert
    INSTEAD OF INSERT
    ON users_book_notes
    FOR EACH ROW
EXECUTE FUNCTION insert_users_book_notes_temporal();

DROP RULE IF EXISTS users_book_reads_view_insert ON users_book_reads;
DROP FUNCTION IF EXISTS insert_users_book_reads_temporal CASCADE;
CREATE FUNCTION insert_users_book_reads_temporal() RETURNS trigger
    LANGUAGE plpgsql AS
$$
BEGIN
    INSERT INTO public.users_book_reads_temporal
    (id, username, book_internal_id, started, finished, created_by, updated_at, updated_by,
     audiobook, ended)
    SELECT new.id,
           new.username,
           b.internal_id,
           new.started,
           new.finished,
           new.created_by,
           new.updated_at,
           new.updated_by,
           new.audiobook,
           NULL
    FROM books_temporal b
    WHERE b.id = new.book_id
      AND b.ended IS NULL;
    RETURN new;
END;
$$;

CREATE TRIGGER users_book_reads_temporal_insert
    INSTEAD OF INSERT
    ON users_book_reads
    FOR EACH ROW
EXECUTE FUNCTION insert_users_book_reads_temporal();

CREATE OR REPLACE FUNCTION update_users_book_reads_temporal() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE users_book_reads_temporal SET ended = now() WHERE users_book_reads_temporal.id = old.id;
    INSERT INTO users_book_reads_temporal (username, book_internal_id, started, finished,
                                           created_at,
                                           created_by, updated_at, updated_by, audiobook, id, ended)
    SELECT new.username,
           b.internal_id,
           new.started,
           new.finished,
           new.created_at,
           new.created_by,
           new.updated_at,
           new.updated_by,
           new.audiobook,
           new.id,
           NULL
    FROM books_temporal b
    WHERE b.id = new.book_id
      AND b.ended IS NULL;
    RETURN new;
END;
$$;

CREATE OR REPLACE FUNCTION update_users_book_notes_temporal() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE users_book_notes_temporal
    SET ended = NOW()
    WHERE id = old.id;
    INSERT INTO users_book_notes_temporal
    (id, username, book_internal_id, notes, ended, created_at, created_by, updated_at, updated_by)
    SELECT new.id,
           new.username,
           b.internal_id,
           new.notes,
           NULL,
           new.created_at,
           new.created_by,
           new.updated_at,
           new.updated_by
    FROM books_temporal b
    WHERE b.id = new.book_id
      AND b.ended IS NULL;
    RETURN new;
END;
$$;

DROP RULE IF EXISTS users_book_reads_delete ON users_book_reads;
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
      AND ended IS NULL;
    );

DROP RULE IF EXISTS users_books_notes_view_delete ON users_book_notes;
CREATE RULE users_books_notes_view_delete AS
    ON DELETE TO users_book_notes DO INSTEAD ( INSERT INTO deletable (id, tbl)
                                               VALUES (old.id::text, 'users_book_notes'::text);
    DELETE
    FROM deletable
    WHERE deletable.id = old.id::text
      AND deletable.tbl = 'books'::text;
    UPDATE users_book_notes_temporal
    SET ended = now()
    WHERE users_book_notes_temporal.id = old.id
      AND ended IS NULL;
    );

DROP RULE IF EXISTS books_view_delete ON books;
CREATE RULE books_view_delete AS
    ON DELETE TO books DO INSTEAD ( INSERT INTO deletable (id, tbl)
                                    VALUES (old.id::text, 'books'::text);
    DELETE
    FROM deletable
    WHERE deletable.id = old.id::text
      AND deletable.tbl = 'books'::text;
    UPDATE books_temporal
    SET ended = now()
    WHERE books_temporal.id = old.id
      AND books_temporal.ended IS NULL
    RETURNING books_temporal.id, books_temporal.author_id, books_temporal.title, books_temporal.published, books_temporal.created_at, books_temporal.updated_at, books_temporal.created_by, books_temporal.updated_by;
    );
