DROP TABLE IF EXISTS secure_data;
DROP TABLE IF EXISTS type;

CREATE TABLE type
(
  id   uuid
    CONSTRAINT unique_type_id PRIMARY KEY,
  name TEXT
    CONSTRAINT unique_name UNIQUE NOT NULL
);

CREATE TABLE secure_data
(
  id      uuid
    CONSTRAINT unique_secure_data_id PRIMARY KEY,
  type_id uuid REFERENCES type (id) NOT NULL,
  key     TEXT                      NOT NULL,
  value   TEXT,
  CONSTRAINT unique_type_key UNIQUE (type_id, key)
);

INSERT into type(id, name)
values ('804a0bd2-00da-11e9-8eb2-f2801f1b9fd1', 'Rakuten Bank');
INSERT into type(id, name)
values ('1f582452-00db-11e9-8eb2-f2801f1b9fd1', 'Post Bank');
INSERT into type(id, name)
values ('5af906f2-00db-11e9-8eb2-f2801f1b9fd1', 'Commercial Bank');

INSERT into secure_data(id, type_id, key, value)
values ('f3cd774c-00da-11e9-8eb2-f2801f1b9fd1', '804a0bd2-00da-11e9-8eb2-f2801f1b9fd1', 'Username', 'Piyal');

INSERT into secure_data (id, type_id, key, value)
values ('f3cd7bc0-00da-11e9-8eb2-f2801f1b9fd1', '804a0bd2-00da-11e9-8eb2-f2801f1b9fd1', 'Password', 'skehwkd343dxuwww');

INSERT into secure_data (id, type_id, key, value)
values ('f3cd7d1e-00da-11e9-8eb2-f2801f1b9fd1', '804a0bd2-00da-11e9-8eb2-f2801f1b9fd1', 'Account Number', '34345334');

INSERT into secure_data(id, type_id, key, value)
values ('f3cd7e54-00da-11e9-8eb2-f2801f1b9fd1', '1f582452-00db-11e9-8eb2-f2801f1b9fd1', 'Username', 'Saman');

INSERT into secure_data (id, type_id, key, value)
values ('f3cd8340-00da-11e9-8eb2-f2801f1b9fd1', '1f582452-00db-11e9-8eb2-f2801f1b9fd1', 'Password', 'wefd45h343433f');

INSERT into secure_data (id, type_id, key, value)
values ('f3cd8340-00da-11e9-8eb2-f2801f1b9fd2', '1f582452-00db-11e9-8eb2-f2801f1b9fd1', 'Account Number', '234282828');

INSERT into secure_data(id, type_id, key, value)
values ('7a108010-00db-11e9-8eb2-f2801f1b9fd1', '5af906f2-00db-11e9-8eb2-f2801f1b9fd1', 'Username', 'Kamal');

INSERT into secure_data (id, type_id, key, value)
values ('7a10851a-00db-11e9-8eb2-f2801f1b9fd1', '5af906f2-00db-11e9-8eb2-f2801f1b9fd1', 'Password', '4ssw3334f343');

INSERT into secure_data (id, type_id, key, value)
values ('7a108682-00db-11e9-8eb2-f2801f1b9fd1', '5af906f2-00db-11e9-8eb2-f2801f1b9fd1', 'Account Number', '754332222');