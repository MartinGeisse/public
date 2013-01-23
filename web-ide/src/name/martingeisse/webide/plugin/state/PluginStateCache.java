/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin.state;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QPluginBundleStates;

import com.mysema.query.FilteredClause;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;



/**
 * {@link PluginBundleStateKey} delegates plugin state handling to this class.
 */
class PluginStateCache {

	/**
	 * Prevent instantiation.
	 */
	private PluginStateCache() {
	}

	/**
	 * 
	 */
	static <T> void save(PluginBundleStateKey key, T data, IPluginBundleStateSerializer<T> serializer) {
		byte[] serializedData = serializer.serialize(data);
	
		// update ...
		SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QPluginBundleStates.pluginBundleStates);
		whereStateKey(update, key);
		update.set(QPluginBundleStates.pluginBundleStates.data, serializedData);
		long updatedRows = update.execute();

		// ... or insert
		if (updatedRows == 0) {
			SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QPluginBundleStates.pluginBundleStates);
			insert.set(QPluginBundleStates.pluginBundleStates.pluginBundleId, key.getAccessToken().getPluginBundleId());
			insert.set(QPluginBundleStates.pluginBundleStates.userId, key.getUserId());
			insert.set(QPluginBundleStates.pluginBundleStates.section, key.getSection());
			insert.set(QPluginBundleStates.pluginBundleStates.data, serializer.serialize(data));
			insert.execute();
		}
		
	}
	
	/**
	 * 
	 */
	static <T> T load(PluginBundleStateKey key, IPluginBundleStateSerializer<T> serializer) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QPluginBundleStates.pluginBundleStates);
		whereStateKey(query, key);
		Object[] row = (Object[])(Object)(query.singleResult(QPluginBundleStates.pluginBundleStates.data));
		if (row == null) {
			return null;
		} else {
			return serializer.deserialize((byte[])row[0]);
		}
	}
	
	/**
	 * 
	 */
	private static void whereStateKey(FilteredClause<?> query, PluginBundleStateKey key) {
		query.where(QPluginBundleStates.pluginBundleStates.pluginBundleId.eq(key.getAccessToken().getPluginBundleId()));
		query.where(QPluginBundleStates.pluginBundleStates.userId.eq(key.getUserId()));
		query.where(QPluginBundleStates.pluginBundleStates.section.eq(key.getSection()));
	}

}
