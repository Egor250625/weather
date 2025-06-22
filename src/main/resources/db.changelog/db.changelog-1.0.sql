--changeset egorivanov:1

CREATE TABLE IF NOT EXISTS Users
(
    ID       SERIAL PRIMARY KEY,
    Username VARCHAR(128) UNIQUE NOT NULL,
    Password VARCHAR(512)        NOT NULL
);

--changeset egorivanov:2
CREATE TABLE IF NOT EXISTS Locations
(
    ID        SERIAL PRIMARY KEY,
    Name      VARCHAR(128)              NOT NULL,
    User_id   INT REFERENCES Users (ID) NOT NULL,
    Latitude  DOUBLE PRECISION          NOT NULL,
    Longitude DOUBLE PRECISION          NOT NULL
);

--changeset egorivanov:3
CREATE TABLE IF NOT EXISTS Sessions
(
    ID         UUID PRIMARY KEY,
    User_id    INT REFERENCES Users (ID) NOT NULL,
    Expires_at TIMESTAMP                 NOT NULL
);
