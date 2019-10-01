ALTER TABLE lists
  ADD COLUMN creation_date TIMESTAMP NOT NULL DEFAULT NOW(),
  ADD COLUMN updated_date TIMESTAMP,
  ALTER COLUMN description DROP NOT NULL;

ALTER TABLE items
  ALTER COLUMN description DROP NOT NULL;