CREATE OR REPLACE PROCEDURE add_audit_columns(tbl text)
    LANGUAGE plpgsql
AS
$$
BEGIN
    EXECUTE (
        concat('alter table ', tbl,
               ' add column IF NOT EXISTS created_at timestamptz not null default now();')
        );
    EXECUTE (concat('alter table ', tbl,
                    ' add column if not exists created_by text not null references users (username);')
        );
    EXECUTE (concat('alter table ', tbl,
                    ' add column if not exists updated_at timestamptz;')
        );
    EXECUTE (concat('alter table ', tbl,
                    ' add column if not exists updated_by text references users (username);')
        );
END;
$$;
