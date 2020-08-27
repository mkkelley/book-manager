alter table users_book_reads
    add audiobook boolean not null default false;
alter table users_book_reads
    alter column audiobook drop default;