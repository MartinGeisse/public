
DROP KEYSPACE miner;
CREATE KEYSPACE miner WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
USE miner;

CREATE TABLE section_data (
	id text PRIMARY KEY,
	data blob
);
