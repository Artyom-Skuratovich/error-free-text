CREATE TABLE IF NOT EXISTS correction_tasks (
    id UUID PRIMARY KEY,
    text TEXT NOT NULL,
    error_message VARCHAR(255),
    language VARCHAR(2) NOT NULL,
    status VARCHAR(10) NOT NULL
);