-- these indicies all improve performance of the queries on the /api/books call
CREATE INDEX IF NOT EXISTS books_temporal_created_at_ended_null ON books_temporal (created_at) WHERE ended IS NULL;
CREATE INDEX IF NOT EXISTS books_temporal_id_ended_null ON books_temporal (id) WHERE ended IS NULL;
CREATE INDEX IF NOT EXISTS books_temporal_id ON books_temporal (id);

CREATE INDEX IF NOT EXISTS users_book_reads_temporal_book_internal_id_ended_null ON users_book_reads_temporal (book_internal_id) WHERE ended IS NULL;

CREATE INDEX IF NOT EXISTS book_tags_temporal_book_internal_id_ended_null ON books_tags_temporal (book_internal_id) WHERE ended IS NULL;

CREATE INDEX IF NOT EXISTS authors_temporal_id_ended_null ON authors_temporal (id) WHERE ended IS NULL;

-- this lets the query planner ignore the authors table when counting
CREATE OR REPLACE VIEW books AS
SELECT books_temporal.id,
       a.id AS author_id,
       books_temporal.title,
       books_temporal.published,
       books_temporal.created_at,
       books_temporal.updated_at,
       books_temporal.created_by,
       books_temporal.updated_by
FROM books_temporal
         LEFT JOIN authors_temporal a ON a.internal_id = books_temporal.author_internal_id
WHERE books_temporal.ended IS NULL;
