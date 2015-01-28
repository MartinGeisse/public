/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section.storage;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.network.SectionDataId;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * This storage implementation stores sections in a Cassandra database.
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

	/**
	 * 
	 */
	private ResultSet fetch(final Clause clause) {
		return cassandrasSession.execute(QueryBuilder.select().all().from(tableName).where(clause));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#loadSectionRelatedObject(name.martingeisse.stackd.common.network.SectionDataId)
	 */
	@Override
	public byte[] loadSectionRelatedObject(final SectionDataId id) {
		try {
			for (final Row row : fetch(QueryBuilder.eq("id", id.getIdentifierText()))) {
				final ByteBuffer dataBuffer = row.getBytes("data");
				final byte[] data = new byte[dataBuffer.remaining()];
				dataBuffer.get(data);
				return data;
			}
			return null;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#loadSectionRelatedObjects(java.util.Collection)
	 */
	@Override
	public Map<SectionDataId, byte[]> loadSectionRelatedObjects(final Collection<? extends SectionDataId> ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("loading section-related objects: " + StringUtils.join(ids, ", "));
		}
		try {
			
			// convert the IDs to an array of strings
			Object[] idTexts = new String[ids.size()];
			{
				int i = 0;
				for (SectionDataId id : ids) {
					idTexts[i] = id.getIdentifierText();
					i++;
				}
			}
			
			// fetch the rows
			final Map<SectionDataId, byte[]> result = new HashMap<>();
			for (final Row row : fetch(QueryBuilder.in("id", idTexts))) {
				final String id = row.getString("id");
				final ByteBuffer dataBuffer = row.getBytes("data");
				final byte[] data = new byte[dataBuffer.remaining()];
				dataBuffer.get(data);
				result.put(new SectionDataId(id), data);
			}
			return result;
			
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
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
