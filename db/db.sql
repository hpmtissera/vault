DROP TABLE IF EXISTS secure_data;

CREATE TABLE secure_data (
  id uuid CONSTRAINT unique_id UNIQUE NOT NULL,
  type TEXT NOT NULL,
  key TEXT NOT NULL,
  value TEXT,
  CONSTRAINT unique_type_key UNIQUE (type, key)
);