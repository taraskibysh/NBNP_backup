CREATE TABLE IF NOT EXISTS app_user (
id INT PRIMARY KEY AUTO_INCREMENT,
email VARCHAR(100) UNIQUE NOT NULL,
full_name VARCHAR(150) NOT NULL,
avatar_image VARCHAR(100),
sex ENUM('male', 'female'),
date_of_birth DATE NOT NULL,
user_password VARCHAR(72),
messenger_link VARCHAR(250),
phone_number VARCHAR(20)
);
