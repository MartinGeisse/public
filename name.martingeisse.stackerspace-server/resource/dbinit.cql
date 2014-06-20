
DROP KEYSPACE stackerspace;
CREATE KEYSPACE stackerspace WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
USE stackerspace;

CREATE TABLE section_data (
	id text PRIMARY KEY,
	data blob
);
