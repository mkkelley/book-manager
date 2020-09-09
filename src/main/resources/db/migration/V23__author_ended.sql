ALTER TABLE books_temporal
    DROP CONSTRAINT books_author_id_fkey;
ALTER TABLE authors
    DROP CONSTRAINT authors_pkey;
ALTER TABLE authors
    ADD internal_id serial PRIMARY KEY;
ALTER TABLE authors
    ADD ended timestamptz NULL;
ALTER TABLE books_temporal
    ADD COLUMN author_internal_id int REFERENCES authors (internal_id);

UPDATE books_temporal
SET author_internal_id = a.internal_id
FROM authors a
WHERE a.id = books_temporal.author_id;

ALTER TABLE books_temporal
    ALTER COLUMN author_internal_id SET NOT NULL;


CREATE OR REPLACE VIEW books
            (id, author_id, title, published, created_at, updated_at, created_by, updated_by) AS
SELECT books_temporal.id,
       a.id,
       books_temporal.title,
       books_temporal.published,
       books_temporal.created_at,
       books_temporal.updated_at,
       books_temporal.created_by,
       books_temporal.updated_by
FROM books_temporal
         INNER JOIN authors a ON a.internal_id = books_temporal.author_internal_id
WHERE books_temporal.ended IS NULL;

ALTER TABLE authors
    RENAME TO authors_temporal;

CREATE VIEW authors AS
SELECT id, name, created_at, updated_at, created_by, updated_by
FROM authors_temporal
WHERE ended IS NULL;

CREATE OR REPLACE RULE books_view_insert AS
    ON INSERT TO books DO INSTEAD
    INSERT INTO books_temporal (author_internal_id, title, published,
                                updated_at, created_by, updated_by,
                                ended)

    SELECT authors_temporal.internal_id,
           new.title,
           new.published,
           new.updated_at,
           new.created_by,
           new.updated_by,
           NULL::timestamp WITHOUT TIME ZONE
    FROM authors_temporal
    WHERE authors_temporal.id = new.author_id
      AND authors_temporal.ended IS NULL

    RETURNING books_temporal.id, (SELECT authors_temporal.id
                                  FROM authors_temporal
                                           INNER JOIN books_temporal
                                                      ON authors_temporal.internal_id =
                                                         books_temporal.author_internal_id), books_temporal.title, books_temporal.published, books_temporal.created_at, books_temporal.updated_at, books_temporal.created_by, books_temporal.updated_by;

CREATE OR REPLACE RULE books_view_delete AS
    ON DELETE TO books DO INSTEAD ( INSERT INTO deletable (id, tbl)
                                    VALUES (old.id::text, 'books'::text);
    DELETE
    FROM users_book_reads
    WHERE users_book_reads.book_id = old.id;
    DELETE
    FROM users_book_notes
    WHERE users_book_notes.book_id = old.id;
    DELETE
    FROM books_tags
    WHERE books_tags.book_id = old.id;
    DELETE
    FROM deletable
    WHERE deletable.id = old.id::text
      AND deletable.tbl = 'books'::text;
    UPDATE books_temporal
    SET ended = now()
    FROM authors_temporal a
    WHERE a.internal_id = books_temporal.author_internal_id
      AND books_temporal.id = OLD.id
      AND books_temporal.ended IS NULL
    RETURNING books_temporal.id , a.id , books_temporal.title , books_temporal.published , books_temporal.created_at , books_temporal.updated_at , books_temporal.created_by , books_temporal.updated_by;
    );

ALTER TABLE books_temporal
    DROP COLUMN author_id;

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
    DROP TABLE utobti_books_tags;
    RETURN new;
END;
$$;

CREATE OR REPLACE FUNCTION update_books_temporal() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE books_temporal
    SET ended = now()
    WHERE id = old.id
      AND ended IS NULL;

    INSERT INTO books_temporal (id, author_internal_id, title, published, updated_at, created_by,
                                updated_by,
                                ended)
    SELECT new.id,
           a.internal_id,
           new.title,
           new.published,
           new.updated_at,
           new.created_by,
           new.updated_by,
           NULL
    FROM authors_temporal a
    WHERE a.id = new.author_id
      AND a.ended IS NULL;

    RETURN new;
END;
$$;

CREATE OR REPLACE RULE authors_view_delete AS ON DELETE TO authors DO INSTEAD
    (INSERT INTO deletable(id, tbl)
     VALUES (old.id::text, 'authors')
    ;
    DELETE
    FROM deletable
    WHERE id = old.id::text
      AND tbl = 'authors';
    UPDATE authors_temporal
    SET ended = now()
    WHERE ended IS NULL
      AND id = old.id;
    );

CREATE OR REPLACE FUNCTION update_authors_temporal() RETURNS trigger
    LANGUAGE plpgsql AS
$$
BEGIN
    RAISE EXCEPTION 'cannot update authors';
END;
$$;

CREATE TRIGGER authors_update
    INSTEAD OF UPDATE
    ON authors
    FOR EACH ROW
EXECUTE PROCEDURE update_authors_temporal();
