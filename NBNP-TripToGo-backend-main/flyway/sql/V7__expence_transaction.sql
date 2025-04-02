CREATE TABLE IF NOT EXISTS expense_transaction
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT            NOT NULL,
    expense_id  INT            NOT NULL,
    user_amount DECIMAL(10, 2) NOT NULL,
    done        BOOL           NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES app_user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (expense_id)
        REFERENCES expense (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);