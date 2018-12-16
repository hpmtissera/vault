DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_roles;

CREATE  TABLE users (
  username text NOT NULL,
  password text NOT NULL,
  enabled boolean NOT NULL DEFAULT 1 ,
  PRIMARY KEY (username));

CREATE TABLE user_roles (
    user_role_id int(11) NOT NULL AUTO_INCREMENT,
    username varchar(45) NOT NULL,
    role varchar(45) NOT NULL,
    PRIMARY KEY (user_role_id),
    UNIQUE KEY uni_username_role (role,username),
    KEY fk_username_idx (username),
    CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));

INSERT INTO users(username,password,enabled)
    VALUES ('admin','admin', true);
    INSERT INTO users(username,password,enabled)
    VALUES ('user','password1', true);

INSERT INTO user_roles (username, role)
VALUES ('user', 'ROLE_USER');
INSERT INTO user_roles (username, role)
VALUES ('admin', 'ROLE_ADMIN');