DROP TABLE IF EXISTS "user";

CREATE TABLE "user" (
  id uuid CONSTRAINT unique_user_id PRIMARY KEY,
  name text NOT NULL,
  username text CONSTRAINT "unique_username" UNIQUE NOT NULL,
  password text NOT NULL
);

INSERT INTO "user" (id, name, username, password)
    VALUES ('4310686d-bbb9-46a7-aa95-53ea2ec92618', 'Administrator', 'admin', E'$2a$10$xMtxPuXs9CXtb4mnA2SG0\.91D0Oc4a2hgFW9wLitoBYQD\.u\.uaDUq');