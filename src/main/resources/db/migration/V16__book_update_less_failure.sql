CREATE OR REPLACE RULE books_view_delete AS
    ON DELETE TO books DO INSTEAD (
    INSERT INTO deletable (id, tbl)
    VALUES (old.id::text, 'books'::text)
    ;

    DELETE
    FROM deletable
    WHERE deletable.id = old.id::text
      AND deletable.tbl = 'books'::text
    ;

    DELETE
    FROM users_book_reads
    WHERE book_id = old.id
    ;

    DELETE
    FROM users_book_notes
    WHERE book_id = old.id;

    UPDATE books_temporal
    SET ended = now()
    WHERE books_temporal.id = old.id
      AND books_temporal.ended IS NULL
    RETURNING books_temporal.id, books_temporal.author_id, books_temporal.title, books_temporal.published, books_temporal.created_at, books_temporal.updated_at, books_temporal.created_by, books_temporal.updated_by;
    );

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

    CREATE TEMPORARY TABLE temp_users_book_reads ON COMMIT DROP AS
    SELECT * FROM users_book_reads WHERE book_id = old.id;
    CREATE TEMPORARY TABLE temp_users_book_notes ON COMMIT DROP AS
    SELECT * FROM users_book_notes WHERE book_id = old.id;

    DELETE FROM users_book_reads WHERE book_id = old.id;
    DELETE FROM users_book_notes WHERE book_id = old.id;

    INSERT INTO users_book_reads SELECT * FROM temp_users_book_reads;
    INSERT INTO users_book_notes SELECT * FROM temp_users_book_notes;

    RETURN new;
END;
$$;

CREATE OR REPLACE RULE users_books_notes_view_delete AS
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
