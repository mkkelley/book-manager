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
    WHERE books_temporal.id = old.id
      AND books_temporal.ended IS NULL
    RETURNING books_temporal.id, books_temporal.author_id, books_temporal.title, books_temporal.published, books_temporal.created_at, books_temporal.updated_at, books_temporal.created_by, books_temporal.updated_by;
    );
