-- Table: roles
CREATE TABLE roles (
                       id          integer         NOT NULL PRIMARY KEY,
                       name        varchar(100)    NOT NULL
);


-- Table: users
CREATE TABLE users (
                    id          integer         NOT NULL PRIMARY KEY,
                    username    varchar(255)    NOT NULL,
                    password    varchar(255)    NOT NULL,
                    role_id     integer         NOT NULL,

                    FOREIGN KEY (role_id) REFERENCES roles (id),

                    UNIQUE (username)
);


-- Insert data
INSERT INTO roles VALUES (1, 'USER');
INSERT INTO roles VALUES (2, 'ADMIN');
INSERT INTO roles VALUES (3, 'GUEST');


INSERT INTO users VALUES (1, 'admin', '$2a$12$G9Tyz/oGx3ptSi6ZxZZrtuCZDj6wwTj5VYKfuYoHxec8MOqrQm.XS', 2);
INSERT INTO users VALUES (2, 'user',  '$2a$12$X.mZ.klfcVCM66Ge2fe.qeWj0wg6OF4s9r/o.yF7dRsBEJ/OhfgKm', 1);
INSERT INTO users VALUES (3, 'guest', '$2a$12$NDKyA3CTuW0YRvMfS00FReTAivzRw8efKjzUL59jb.SmotP.XsY.S', 3);
