CREATE TABLE IF NOT EXISTS trip
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    title            VARCHAR(250) NOT NULL,
    trip_description TEXT,
    group_link       VARCHAR(250),
    finance_link     VARCHAR(250),
    start_date       DATETIME     NOT NULL,
    end_date         DATETIME     NOT NULL,
    is_active        BOOL
);