DROP TABLE IF EXISTS tags CASCADE;
CREATE TABLE tags
(
    id  serial NOT NULL PRIMARY KEY,
    tag text   NOT NULL
);

CALL add_audit_columns('tags');

DROP TABLE IF EXISTS books_tags_temporal CASCADE;
CREATE TABLE books_tags_temporal
(
    book_internal_id int NOT NULL REFERENCES books_temporal (internal_id),
    tag_id           int NOT NULL REFERENCES tags (id),
    ended            timestamptz
);

CALL add_audit_columns('books_tags_temporal');

CREATE OR REPLACE VIEW books_tags AS
SELECT bt.id book_id, tag_id, btt.created_at, btt.created_by, btt.updated_at, btt.updated_by
FROM books_tags_temporal btt
         INNER JOIN books_temporal bt ON bt.internal_id = btt.book_internal_id
WHERE btt.ended IS NULL;

CREATE OR REPLACE RULE books_tags_delete AS ON DELETE TO books_tags
    DO INSTEAD (
    INSERT INTO deletable (id, tbl)
    VALUES (concat(old.tag_id::text, ',', old.book_id::text), 'books_tags');
    DELETE
    FROM deletable
    WHERE id = concat(old.tag_id::text, ',', old.book_id::text)
      AND tbl = 'books_tags';
    UPDATE books_tags_temporal
    SET ended = now()
    FROM books_temporal bt
    WHERE books_tags_temporal.ended IS NULL
      AND tag_id = OLD.tag_id
      AND old.book_id = bt.id
      AND books_tags_temporal.book_internal_id = bt.internal_id );

CREATE OR REPLACE FUNCTION insert_books_tags_temporal() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO books_tags_temporal (book_internal_id, tag_id, ended, created_by,
                                     updated_at, updated_by)
    SELECT books_temporal.internal_id,
           new.tag_id,
           NULL,
           new.created_by,
           new.updated_at,
           new.updated_by
    FROM books_temporal
    WHERE books_temporal.id = new.book_id
      AND books_temporal.ended IS NULL;

    UPDATE books_tags_temporal
    SET created_at = CASE
                         WHEN new.created_at IS NULL THEN now()
                         WHEN new.created_at IS NOT NULL THEN new.created_at
        END
    FROM books_temporal
    WHERE new.book_id = books_temporal.id
      AND books_temporal.ended IS NULL
      AND books_temporal.internal_id = books_tags_temporal.book_internal_id
      AND books_tags_temporal.tag_id = new.tag_id
      AND books_tags_temporal.ended IS NULL;
    RETURN new;
END;
$$;

DROP TRIGGER IF EXISTS books_tags_insert ON books_tags;
CREATE TRIGGER books_tags_insert
    INSTEAD OF INSERT
    ON books_tags
    FOR EACH ROW
EXECUTE PROCEDURE insert_books_tags_temporal();

CREATE OR REPLACE FUNCTION update_books_tags_temporal() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE books_tags_temporal btt
    SET ended = now()
    FROM books_temporal
    WHERE btt.ended IS NULL
      AND btt.tag_id = old.tag_id
      AND btt.book_internal_id = books_temporal.internal_id
      AND books_temporal.id = old.book_id;

    INSERT INTO books_tags_temporal (book_internal_id, tag_id, ended, created_by,
                                     updated_at, updated_by)
    SELECT books_temporal.internal_id,
           new.tag_id,
           NULL,
           new.created_by,
           new.updated_at,
           new.updated_by
    FROM books_temporal
    WHERE id = new.book_id
      AND ended IS NULL;

    UPDATE books_tags_temporal
    SET created_at = CASE
                         WHEN new.created_at IS NULL THEN now()
                         WHEN new.created_at IS NOT NULL THEN new.created_at
        END
    FROM books_temporal
    WHERE new.book_id = books_temporal.id
      AND books_temporal.ended IS NULL
      AND books_temporal.internal_id = books_tags_temporal.book_internal_id
      AND books_tags_temporal.tag_id = new.tag_id
      AND books_tags_temporal.ended IS NULL;
    RETURN new;
END;
$$;

DROP TRIGGER IF EXISTS books_tags_update ON books_tags;
CREATE TRIGGER books_tags_update
    INSTEAD OF UPDATE
    ON books_tags
    FOR EACH ROW
EXECUTE PROCEDURE update_books_tags_temporal();

CREATE OR REPLACE FUNCTION update_tags_on_books_temporal_insert() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    RAISE NOTICE 'new: %', new;
    CREATE TEMPORARY TABLE utobti_books_tags AS
    SELECT * FROM books_tags WHERE book_id = new.id;
    DELETE FROM books_tags WHERE book_id = new.id;
    INSERT INTO books_tags SELECT * FROM utobti_books_tags;
--     DROP TABLE utobti_books_tags;
    RETURN new;
END;
$$;

DROP TRIGGER IF EXISTS books_internal_insert_updates_tags ON books_temporal;
CREATE TRIGGER books_internal_insert_updates_tags
    AFTER INSERT
    ON books_temporal
    FOR EACH ROW
EXECUTE PROCEDURE update_tags_on_books_temporal_insert();

CREATE OR REPLACE FUNCTION update_notes_on_books_temporal_insert() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    CREATE TEMPORARY TABLE unobti_book_notes AS
    SELECT * FROM users_book_notes WHERE book_id = new.id;

    DELETE FROM users_book_notes WHERE book_id = new.id;

    INSERT INTO users_book_notes SELECT * FROM unobti_book_notes;
    DROP TABLE unobti_book_notes;
    RETURN new;
END;

$$;

DROP TRIGGER IF EXISTS books_internal_insert_updates_notes ON books_temporal;
CREATE TRIGGER books_internal_insert_updates_notes
    AFTER INSERT
    ON books_temporal
    FOR EACH ROW
EXECUTE PROCEDURE update_notes_on_books_temporal_insert();

CREATE OR REPLACE FUNCTION update_reads_on_books_temporal_insert() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    CREATE TEMPORARY TABLE urobti_book_reads AS
    SELECT * FROM users_book_reads WHERE book_id = new.id;

    DELETE FROM users_book_reads WHERE book_id = new.id;

    INSERT INTO users_book_reads SELECT * FROM urobti_book_reads;
    DROP TABLE urobti_book_reads;
    RETURN new;
END;
$$;

DROP TRIGGER IF EXISTS books_internal_insert_updates_reads ON books_temporal;
CREATE TRIGGER books_internal_insert_updates_reads
    AFTER INSERT
    ON books_temporal
    FOR EACH ROW
EXECUTE PROCEDURE update_reads_on_books_temporal_insert();

CREATE OR REPLACE FUNCTION update_books_temporal() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE books_temporal
    SET ended = now()
    WHERE id = old.id
      AND ended IS NULL;

    INSERT INTO books_temporal (id, author_id, title, published, updated_at, created_by, updated_by,
                                ended)
    VALUES (new.id, new.author_id, new.title, new.published, new.updated_at, new.created_by,
            new.updated_by, NULL);

    RETURN new;
END;
$$;

ALTER TABLE books_temporal
    DROP CONSTRAINT IF EXISTS books_uq;
ALTER TABLE users_book_reads_temporal
    DROP CONSTRAINT IF EXISTS users_books_reads_temporal_uq;
ALTER TABLE users_book_notes_temporal
    DROP CONSTRAINT IF EXISTS users_book_notes_temporal_uq;

DROP RULE IF EXISTS books_view_delete ON books;
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
    DELETE
    FROM users_book_notes
    WHERE users_book_notes.book_id = old.id;
    DELETE
    FROM books_tags
    WHERE books_tags.book_id = old.id;
    UPDATE books_temporal
    SET ended = now()
    WHERE books_temporal.id = old.id
      AND books_temporal.ended IS NULL
    RETURNING books_temporal.id, books_temporal.author_id, books_temporal.title, books_temporal.published, books_temporal.created_at, books_temporal.updated_at, books_temporal.created_by, books_temporal.updated_by;
    );
