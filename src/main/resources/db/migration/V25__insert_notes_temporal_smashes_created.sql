CREATE OR REPLACE FUNCTION insert_users_book_notes_temporal() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO users_book_notes_temporal
    (id, username, book_internal_id, notes, ended, created_at, created_by, updated_at,
     updated_by, user_created_at, user_updated_at)
    SELECT new.id,
           new.username,
           b.internal_id,
           new.notes,
           NULL,
           now(),
           new.created_by,
           new.updated_at,
           new.updated_by,
           new.user_created_at,
           new.user_updated_at
    FROM books_temporal b
    WHERE b.id = new.book_id
      AND b.ended IS NULL;
    RETURN NEW;
END;
$$;
