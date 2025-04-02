CREATE TABLE IF NOT EXISTS trip_event
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    title             VARCHAR(100) NOT NULL,
    event_description TEXT,
    event_date        DATETIME     NOT NULL,
    trip_id           INT          NOT NULL,
    FOREIGN KEY (trip_id)
        REFERENCES trip (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);