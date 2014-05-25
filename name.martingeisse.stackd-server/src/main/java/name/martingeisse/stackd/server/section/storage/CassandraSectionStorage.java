/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section.storage;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.network.SectionDataId;
import org.apache.log4j.Logger;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * This storage implementation stores sections in a Cassandra database.
 * 
 * TODO not implemented
 */
public final class CassandraSectionStorage extends AbstractSectionStorage {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(CassandraSectionStorage.class);
	
	/**
	 * the cassandrasSession
	 */
	private Session cassandrasSession;

	/**
	 * the tableName
	 */
	private String tableName;

	/**
	 * Constructor.
	 * @param clusterSize the cluster-size of sections
	 * @param cassandrasSession the cassandra {@link Session} object used to access the database
	 * @param tableName the name of the section table
	 */
	public CassandraSectionStorage(final ClusterSize clusterSize, final Session cassandrasSession, final String tableName) {
		super(clusterSize);
		this.cassandrasSession = cassandrasSession;
		this.tableName = tableName;
	}

	/**
	 * Getter method for the cassandrasSession.
	 * @return the cassandrasSession
	 */
	public Session getCassandrasSession() {
		return cassandrasSession;
	}

	/**
	 * Getter method for the tableName.
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#loadSectionRelatedObjects(name.martingeisse.stackd.common.network.SectionDataId[])
	 */
	@Override
	public byte[][] loadSectionRelatedObjects(final SectionDataId[] SectionDataIds) {
		logger.debug("loading section-related objects...");
		
		// build a map to map the section IDs from the result set back to array indices,
		// also generate text from the IDs
		Map<SectionDataId, Integer> sectionIndices = new HashMap<SectionDataId, Integer>();
		String[] sectionDataIdTexts = new String[SectionDataIds.length];
		for (int i=0; i<SectionDataIds.length; i++) {
			sectionIndices.put(SectionDataIds[i], i);
			sectionDataIdTexts[i] = SectionDataIds[i].getIdentifierText();
			logger.trace("including: " + SectionDataIds[i]);
		}
		
		// load data from the database
		byte[][] result = new byte[SectionDataIds.length][];
		try {
			Clause clause = QueryBuilder.in("id", (Object[])sectionDataIdTexts);
			for (Row row : cassandrasSession.execute(QueryBuilder.select().all().from(tableName).where(clause))) {
				String id = row.getString("id");
				ByteBuffer dataBuffer = row.getBytes("data");
				byte[] data = new byte[dataBuffer.remaining()];
				dataBuffer.get(data);
				result[sectionIndices.get(new SectionDataId(id))] = data;				
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		
		return result;
	
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#saveSectionRelatedObject(name.martingeisse.stackd.common.network.SectionDataId, byte[])
	 */
	@Override
	public void saveSectionRelatedObject(final SectionDataId sectionDataId, final byte[] data) {
		try {
			final String rowId = sectionDataId.getIdentifierText();
			cassandrasSession.execute(QueryBuilder.insertInto(tableName).value("id", rowId).value("data", ByteBuffer.wrap(data)));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#deleteSectionRelatedObject(name.martingeisse.stackd.common.network.SectionDataId)
	 */
	@Override
	public void deleteSectionRelatedObject(final SectionDataId sectionDataId) {
		try {
			final Clause clause = QueryBuilder.eq("id", sectionDataId.getIdentifierText());
			cassandrasSession.execute(QueryBuilder.delete().from(tableName).where(clause));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
