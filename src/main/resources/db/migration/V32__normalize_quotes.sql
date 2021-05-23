UPDATE books
SET title = REPLACE(title, '''', '’')
WHERE title LIKE '%''%'
  AND id < (SELECT PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY id) FROM books);

UPDATE books
SET title = REPLACE(title, '''', '’')
WHERE title LIKE '%''%'
  AND id < (SELECT PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY id) FROM books);

UPDATE books
SET title = REPLACE(title, '''', '’')
WHERE title LIKE '%''%'
  AND id < (SELECT PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY id) FROM books);

UPDATE books
SET title = REPLACE(title, '''', '’')
WHERE title LIKE '%''%';
