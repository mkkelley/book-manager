CREATE TABLE book_storage
(
    id          serial PRIMARY KEY,
    book_id     int  NOT NULL,
    bucket      text NOT NULL,
    storage_key text NOT NULL
);

CALL add_audit_columns('book_storage');