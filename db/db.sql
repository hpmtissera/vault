DROP TABLE IF EXISTS secure_data;

CREATE TABLE secure_data (
  id uuid CONSTRAINT unique_id UNIQUE NOT NULL,
  type TEXT NOT NULL,
  key TEXT NOT NULL,
  value TEXT,
  CONSTRAINT unique_type_key UNIQUE (type, key)
);

INSERT into secure_data(id, type, key, value) values ('3a348cea-0061-11e9-8eb2-f2801f1b9fd2', 'RakutenBank', 'Username', 'Piyal');
INSERT into secure_data (id, type, key, value) values ('3a348cea-0061-11e9-8eb2-f2801f1b9fd2', 'RakutenBan', 'Password', 'Puwak');