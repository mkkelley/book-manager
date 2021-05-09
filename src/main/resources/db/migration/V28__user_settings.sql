CREATE TABLE user_settings
(
    username TEXT NOT NULL PRIMARY KEY REFERENCES users (username),
    settings jsonb
);
