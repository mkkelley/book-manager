ALTER TABLE users_book_notes_temporal
    ADD COLUMN IF NOT EXISTS user_created_at timestamptz;
UPDATE users_book_notes_temporal
SET user_created_at = created_at;
ALTER TABLE users_book_notes_temporal
    ALTER user_created_at SET NOT NULL;
ALTER TABLE users_book_notes_temporal
    ADD COLUMN IF NOT EXISTS user_updated_at timestamptz;


DROP RULE IF EXISTS users_books_notes_view_delete ON users_book_notes;
DROP TRIGGER IF EXISTS users_book_notes_update ON users_book_notes;
DROP TRIGGER IF EXISTS users_book_notes_temporal_insert ON users_book_notes;
DROP TRIGGER IF EXISTS users_book_notes_insert ON users_book_notes;
CREATE OR REPLACE VIEW users_book_notes
            (id, username, book_id, notes, created_at, created_by, updated_at, updated_by,
             user_created_at, user_updated_at)
AS
SELECT users_book_notes_temporal.id,
       users_book_notes_temporal.username,
       bt.id AS book_id,
       users_book_notes_temporal.notes,
       users_book_notes_temporal.created_at,
       users_book_notes_temporal.created_by,
       users_book_notes_temporal.updated_at,
       users_book_notes_temporal.updated_by,
       users_book_notes_temporal.user_created_at,
       users_book_notes_temporal.user_updated_at
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

CREATE OR REPLACE FUNCTION update_users_book_notes_temporal() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE users_book_notes_temporal
    SET ended = NOW()
    WHERE id = old.id
      AND ended IS NULL;
    INSERT INTO users_book_notes_temporal
    (id, username, book_internal_id, notes, ended, created_at, created_by, updated_at, updated_by,
     user_created_at, user_updated_at)
    SELECT new.id,
           new.username,
           b.internal_id,
           new.notes,
           NULL,
           new.created_at,
           new.created_by,
           new.updated_at,
           new.updated_by,
           new.user_created_at,
           new.user_updated_at
    FROM books_temporal b
    WHERE b.id = new.book_id
      AND b.ended IS NULL;
    RETURN new;
END;
$$;

CREATE OR REPLACE FUNCTION insert_users_book_notes_temporal() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO users_book_notes_temporal
    (id, username, book_internal_id, notes, ended, created_by, updated_at,
     updated_by, user_created_at, user_updated_at)
    SELECT new.id,
           new.username,
           b.internal_id,
           new.notes,
           NULL,
           new.created_by,
           new.updated_at,
           new.updated_by,
           new.user_created_at,
           new.user_updated_at
    FROM books_temporal b
    WHERE b.id = new.book_id
      AND b.ended IS NULL;

    UPDATE users_book_notes_temporal
    SET created_at = CASE
                         WHEN new.created_at IS NULL THEN now()
                         WHEN new.created_at IS NOT NULL THEN new.created_at
        END
    WHERE id = new.id
      AND ended IS NULL;
    RETURN NEW;
END;
$$;

CREATE TRIGGER users_book_notes_update
    INSTEAD OF UPDATE
    ON users_book_notes
    FOR EACH ROW
EXECUTE PROCEDURE update_users_book_notes_temporal();

CREATE TRIGGER users_book_notes_insert
    INSTEAD OF INSERT
    ON users_book_notes
    FOR EACH ROW
EXECUTE PROCEDURE insert_users_book_notes_temporal();

