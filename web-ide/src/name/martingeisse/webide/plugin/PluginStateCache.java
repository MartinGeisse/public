/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QPluginBundleStates;
import name.martingeisse.webide.plugin.serializer.IPluginBundleStateSerializer;

import org.apache.log4j.Logger;

import com.mysema.commons.lang.Pair;
import com.mysema.query.FilteredClause;
import com.mysema.query.group.QPair;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Ops;
import com.mysema.query.types.PredicateOperation;



/**
 * {@link PluginBundleStateKey} delegates plugin state handling to this class.
 */
class PluginStateCache {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(PluginStateCache.class);
	
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
	 * This method is called when the set of activated plugins changes for a user,
	 * and after the user's extension bindings have been updated. It deletes state
	 * for the specified user/bundle/section triples, which in turn are the sections
	 * to be cleared upon activation changes as specified in the extension points.
	 */
	static void onActivationChange(long userId, List<Pair<Long, Integer>> bundleSections) {
		if (!bundleSections.isEmpty()) {
			SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QPluginBundleStates.pluginBundleStates);
			delete.where(QPluginBundleStates.pluginBundleStates.userId.eq(userId));
			QPair<Long, Integer> qpair = QPair.create(QPluginBundleStates.pluginBundleStates.pluginBundleId, QPluginBundleStates.pluginBundleStates.section);
			delete.where(new PredicateOperation(Ops.IN, qpair, Expressions.constant(bundleSections)));
			long affected = delete.execute();
			logger.trace("state rows deleted: " + affected);
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
