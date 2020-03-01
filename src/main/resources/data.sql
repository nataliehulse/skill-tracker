INSERT INTO PEOPLE (first_name, last_name, email_address) VALUES
  ('Joey', 'Tribbiani', 'joey.tribbiani@email.com'),
  ('Ross', 'Geller', 'ross.geller@email.com'),
  ('Chandler', 'Bing', 'chandler.bing@email.com'),
  ('Rachel', 'Green', 'rachel.green@email.com'),
  ('Monica', 'Geller', 'monica.geller@email.com'),
  ('Phoebe', 'Buffet', 'phoebe.buffet@email.com');

INSERT INTO SKILLS (description) VALUES
  ('Java'),
  ('Spring'),
  ('Postgres'),
  ('SQL'),
  ('HTML'),
  ('Javascript');

INSERT INTO PEOPLE_SKILLS (people_id, skill_id, level) VALUES
  (1,1,'Expert'),
  (1,2,'Expert'),
  (1,3,'Practitioner'),
  (1,4,'Awareness'),
  (2,1,'Working'),
  (2,2,'Practitioner'),
  (2,4,'Awareness'),
  (2,6,'Expert'),
  (3,3,'Working'),
  (4,4,'Awareness'),
  (4,5,'Awareness'),
  (4,6,'Awareness'),
  (5,1,'Practitioner'),
  (5,2,'Practitioner'),
  (5,3,'Practitioner'),
  (6,2,'Expert'),
  (6,6,'Expert');