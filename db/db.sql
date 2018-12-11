DROP TABLE IF EXISTS secure_data;

CREATE TABLE secure_data (
  id INTEGER CONSTRAINT unique_id UNIQUE NOT NULL,
  type VARCHAR CONSTRAINT unique_type UNIQUE NOT NULL,
  key VARCHAR NOT NULL,
  value VARCHAR,
  CONSTRAINT unique_type_key UNIQUE (type, key)
);