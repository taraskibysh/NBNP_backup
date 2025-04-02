CREATE TABLE IF NOT EXISTS expense
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    trip_id      INT,
    description  VARCHAR(255)   NOT NULL,
    owner_id     INT            NOT NULL,
    amount       DECIMAL(10, 2) NOT NULL,
    payment_date DATE,
    FOREIGN KEY (trip_id)
        REFERENCES trip (id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (owner_id)
        REFERENCES app_user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE
);