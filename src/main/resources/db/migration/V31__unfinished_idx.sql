CREATE INDEX users_book_reads_temporal_username_ended_null_finished_null
    ON users_book_reads_temporal (username) WHERE ended IS NULL AND finished IS NULL;
