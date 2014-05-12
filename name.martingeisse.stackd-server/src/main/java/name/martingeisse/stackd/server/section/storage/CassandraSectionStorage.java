/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section.storage;

import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.network.SectionDataId;
import com.datastax.driver.core.Session;

/**
 * This storage implementation stores sections in a Cassandra database.
 * 
 * TODO not implemented
 */
public final class CassandraSectionStorage extends AbstractSectionStorage {

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
		throw new RuntimeException();
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
	public byte[][] loadSectionRelatedObjects(final SectionDataId[] ids) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#saveSectionRelatedObject(name.martingeisse.stackd.common.network.SectionDataId, byte[])
	 */
	@Override
	public void saveSectionRelatedObject(final SectionDataId id, final byte[] data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#deleteSectionRelatedObject(name.martingeisse.stackd.common.network.SectionDataId)
	 */
	@Override
	public void deleteSectionRelatedObject(final SectionDataId id) {
	}

	/*
	@Override
	public byte[][] loadSectionCubes0(SectionId[] sectionIds) {
		
		// build a map to map the section IDs from the result set back to array indices,
		// also generate text from the IDs
		Map<SectionId, Integer> sectionIndices = new HashMap<SectionId, Integer>();
		String[] sectionIdTexts = new String[sectionIds.length];
		for (int i=0; i<sectionIds.length; i++) {
			sectionIndices.put(sectionIds[i], i);
			sectionIdTexts[i] = sectionIds[i].getIdentifierText();
			System.out.println("loading cubes0 for section " + sectionIds[i]);
		}
		
		// load data from the database
		byte[][] result = new byte[sectionIds.length][];
		try {
			Clause clause = QueryBuilder.in("id", (Object[])sectionIdTexts);
			for (Row row : cassandrasSession.execute(QueryBuilder.select().all().from(tableName).where(clause))) {
				String id = row.getString("id");
				ByteBuffer dataBuffer = row.getBytes("cubes0");
				byte[] data = new byte[dataBuffer.remaining()];
				dataBuffer.get(data);
				result[sectionIndices.get(new SectionId(id))] = data;				
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public void saveSectionCubes0(SectionId sectionId, InputStream in) {
		try {
			final String rowId = sectionId.getIdentifierText();
			final byte[] data = IOUtils.toByteArray(in);
			cassandrasSession.execute(QueryBuilder.insertInto(tableName).value("id", rowId).value("cubes0", ByteBuffer.wrap(data)));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void saveSectionRenderModel0(SectionId sectionId, InputStream in) {
		throw new RuntimeException("not yet implemented");
	}

	@Override
	public void deleteSectionRenderModel0(SectionId sectionId) {
		throw new RuntimeException("not yet implemented");
	}
	*/

}
