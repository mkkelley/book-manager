CREATE MATERIALIZED VIEW IF NOT EXISTS
    books_tags_counts AS
SELECT bt.tag_id, COUNT(bt.tag_id) c
FROM books_tags bt
GROUP BY bt.tag_id
UNION
SELECT id tag_id, 0
FROM tags t
WHERE NOT EXISTS(SELECT id FROM books_tags bt1 WHERE bt1.tag_id = t.id)
ORDER BY c DESC;

CREATE INDEX books_tags_counts_tag_id ON books_tags_counts (tag_id) INCLUDE (c);

CREATE OR REPLACE VIEW tags_ordered AS
SELECT t.id, t.tag, t.created_at, t.created_by, t.updated_at, t.updated_by
FROM tags t
         LEFT OUTER JOIN books_tags_counts btc ON t.id = btc.tag_id
ORDER BY btc.c DESC NULLS FIRST;

CREATE OR REPLACE FUNCTION refresh_books_tags_counts()
    RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
BEGIN
    REFRESH MATERIALIZED VIEW books_tags_counts;
    RETURN NULL;
END;
$$;

CREATE TRIGGER refresh_tag_order_on_tag_insert
    AFTER INSERT
    ON tags
    FOR EACH STATEMENT
EXECUTE PROCEDURE refresh_books_tags_counts();