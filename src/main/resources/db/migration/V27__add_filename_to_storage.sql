ALTER TABLE book_storage
    ADD COLUMN filename text NOT NULL DEFAULT '';
ALTER TABLE book_storage
    ALTER COLUMN filename DROP DEFAULT;