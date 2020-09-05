ALTER TABLE public.users_book_reads
    RENAME TO users_book_reads_temporal;

ALTER TABLE public.users_book_reads_temporal
    ADD ended timestamp;

CREATE VIEW public.users_book_reads
AS
(
SELECT id,
       username,
       book_id,
       started,
       finished,
       created_at,
       created_by,
       updated_at,
       updated_by,
       audiobook
FROM public.users_book_reads_temporal
WHERE ended IS NULL);

CREATE RULE users_book_reads_insert AS ON
    INSERT TO public.users_book_reads DO INSTEAD
    INSERT INTO public.users_book_reads_temporal
    (id, username, book_id, started, finished, created_by, updated_at, updated_by, audiobook, ended)
    VALUES (new.id, new.username, new.book_id, new.started, new.finished, new.created_by,
            new.updated_at, new.updated_by, new.audiobook, NULL)
;

CREATE RULE users_book_reads_delete AS ON DELETE TO public.users_book_reads DO INSTEAD (
    INSERT INTO public.deletable (id, tbl)
    VALUES (cast(old.id AS text), 'users_book_reads');
    DELETE
    FROM public.deletable
    WHERE id = cast(old.id AS text)
      AND tbl = 'users_book_reads';
    UPDATE public.users_book_reads_temporal
    SET ended = now()
    WHERE id = old.id
    );

CREATE RULE users_book_reads_update AS ON UPDATE TO public.users_book_reads DO INSTEAD (
    UPDATE public.users_book_reads_temporal
    SET ended = now()
    WHERE id = old.id;
    INSERT INTO public.users_book_reads_temporal
    (id, username, book_id, started, finished, created_by, updated_at, updated_by, audiobook, ended)
    VALUES (old.id, new.username, new.book_id, new.started, new.finished, new.created_by,
            new.updated_at, new.updated_by, new.audiobook, NULL)
    );
