CREATE TABLE meetup (
                        id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        image VARCHAR(255),
                        address VARCHAR(255),
                        description TEXT
);
