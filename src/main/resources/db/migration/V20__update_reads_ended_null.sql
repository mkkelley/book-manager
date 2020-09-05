CREATE OR REPLACE FUNCTION update_users_book_reads_temporal() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE users_book_reads_temporal
    SET ended = now()
    WHERE users_book_reads_temporal.id = old.id
      AND users_book_reads_temporal.ended IS NULL;
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
