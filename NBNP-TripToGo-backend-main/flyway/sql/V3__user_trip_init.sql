CREATE TABLE IF NOT EXISTS user_trip
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id  INT  NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES app_user (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    trip_id  INT  NOT NULL,
    FOREIGN KEY (trip_id)
        REFERENCES trip (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    is_owner BOOL NOT NULL
);