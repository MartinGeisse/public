
DROP KEYSPACE miner;
CREATE KEYSPACE miner WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
USE miner;

CREATE TABLE sections (
	id text PRIMARY KEY,
	cubes0 blob
);

INSERT INTO sections (id, cubes0) VALUES (
	'foo', textAsBlob('blarp')
);
