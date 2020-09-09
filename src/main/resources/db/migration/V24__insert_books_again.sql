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
                                           INNER JOIN books_temporal bt
                                                      ON authors_temporal.internal_id =
                                                         bt.author_internal_id
                                  WHERE bt.internal_id = books_temporal.internal_id), books_temporal.title, books_temporal.published, books_temporal.created_at, books_temporal.updated_at, books_temporal.created_by, books_temporal.updated_by;
