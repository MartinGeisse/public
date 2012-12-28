/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.DeclaredExtensionPoints;
import name.martingeisse.webide.entity.DeclaredExtensions;
import name.martingeisse.webide.entity.PluginBundles;
import name.martingeisse.webide.entity.QDeclaredExtensionPoints;
import name.martingeisse.webide.entity.QDeclaredExtensions;
import name.martingeisse.webide.entity.QExtensionBindings;
import name.martingeisse.webide.entity.QPluginBundles;
import name.martingeisse.webide.entity.QUserPlugins;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * This class contains internal plugin handling utility methods.
 */
public class InternalPluginUtil {

	/**
	 * Prevent instantiation.
	 */
	private InternalPluginUtil() {
	}
	
	/**
	 * Like generateDeclaredExtensionPointsAndExtensionsForPlugin(pluginId) for
	 * all the specified IDs.
	 * 
	 * @param pluginIds the IDs of the plugins
	 */
	public static void generateDeclaredExtensionPointsAndExtensionsForPlugins(Iterable<Long> pluginIds) {
		for (long pluginId : pluginIds) {
			generateDeclaredExtensionPointsAndExtensionsForPlugin(pluginId);
		}
	}
	
	/**
	 * Reads the descriptor of all bundles of the specified plugin, then inserts
	 * declared extension points and extensions. This should be called only once
	 * for each plugin -- previously generated extension points and extensions
	 * might be referred to already, so they cannot be safely removed.
	 * 
	 * @param pluginId the ID of the plugin
	 */
	public static void generateDeclaredExtensionPointsAndExtensionsForPlugin(long pluginId) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		List<PluginBundles> bundles = query.from(QPluginBundles.pluginBundles).where(QPluginBundles.pluginBundles.pluginId.eq(pluginId)).list(QPluginBundles.pluginBundles);
		for (PluginBundles bundle : bundles) {
			String descriptor = bundle.getDescriptor();
			// TODO: parse descriptor, insert Ext.s / EPs
		}
	}

	/**
	 * Like updateExtensionBindingsForUser(userId) for all the specified IDs.
	 * 
	 * @param userIds the IDs of the users
	 */
	public static void updateExtensionBindingsForUsers(Iterable<Long> userIds) {
		for (long userId : userIds) {
			updateExtensionBindingsForUser(userId);
		}
	}
	
	/**
	 * Clears, then regenerates the extension bindings for the specified user.
	 * This method assumes that the declared extension points and extensions
	 * are available for all plugins the user has added to his/her user account.
	 * 
	 * Generated bindings will have newly allocated database IDs, even if they
	 * correspond to previously existing bindings. Binding IDs should not be
	 * linked to for this reason.
	 * 
	 * @param userId the ID of the user
	 */
	public static void updateExtensionBindingsForUser(long userId) {
		
		// fetch data
		List<Long> pluginIds = fetchInstalledPluginIdsForUser(userId);
		List<Long> pluginBundleIds = fetchPluginBundleIds(pluginIds);
		List<DeclaredExtensionPoints> declaredExtensionPoints = fetchDeclaredExtensionPoints(pluginBundleIds);
		List<DeclaredExtensions> declaredExtensions = fetchDeclaredExtensions(pluginBundleIds);
		
		// map extension points by name
		Map<String, DeclaredExtensionPoints> declaredExtensionPointsByName = new HashMap<String, DeclaredExtensionPoints>();
		for (DeclaredExtensionPoints extensionPoint : declaredExtensionPoints) {
			// TODO: detect duplicate EP names!
			declaredExtensionPointsByName.put(extensionPoint.getName(), extensionPoint);
		}
		
		// delete any previously existing bindings
		deleteExtensionBindingsForUser(userId);
		
		// insert the new bindings
		for (DeclaredExtensions extension : declaredExtensions) {
			SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QExtensionBindings.extensionBindings);
			insert.set(QExtensionBindings.extensionBindings.userId, userId);
			insert.set(QExtensionBindings.extensionBindings.declaredExtensionPointId, declaredExtensionPointsByName.get(extension.getExtensionPointName()).getId());
			insert.set(QExtensionBindings.extensionBindings.declaredExtensionId, extension.getId());
			insert.execute();
		}
		
	}
	
	/**
	 * Deletes any existing extension bindings for the specified user.
	 */
	private static void deleteExtensionBindingsForUser(long userId) {
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QExtensionBindings.extensionBindings);
		delete.where(QExtensionBindings.extensionBindings.userId.eq(userId)).execute();
	}
	
	/**
	 * Returns the pluginIds of the plugins the specified user has added.
	 * 
	 * TODO: the word "installed" could mean "added to the plugins table" or
	 * "linked with a user account" -> use different terms!
	 */
	private static List<Long> fetchInstalledPluginIdsForUser(long userId) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QUserPlugins.userPlugins);
		query.where(QUserPlugins.userPlugins.userId.eq(userId));
		return query.list(QUserPlugins.userPlugins.pluginId);
	}
	
	/**
	 * Returns the plugin bundle IDs for all bundles that belong to any of
	 * the specified plugins.
	 */
	private static List<Long> fetchPluginBundleIds(List<Long> pluginIds) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QPluginBundles.pluginBundles);
		query.where(QPluginBundles.pluginBundles.pluginId.in(pluginIds));
		return query.list(QPluginBundles.pluginBundles.id);
	}
	
	/**
	 * Returns the declared extension points that belong to any of the specified plugin bundles.
	 */
	private static List<DeclaredExtensionPoints> fetchDeclaredExtensionPoints(List<Long> pluginBundleIds) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QDeclaredExtensionPoints.declaredExtensionPoints);
		query.where(QDeclaredExtensionPoints.declaredExtensionPoints.pluginBundleId.in(pluginBundleIds));
		return query.list(QDeclaredExtensionPoints.declaredExtensionPoints);
	}
	
	/**
	 * Returns the declared extensions that belong to any of the specified plugin bundles.
	 */
	private static List<DeclaredExtensions> fetchDeclaredExtensions(List<Long> pluginBundleIds) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QDeclaredExtensions.declaredExtensions);
		query.where(QDeclaredExtensions.declaredExtensions.pluginBundleId.in(pluginBundleIds));
		return query.list(QDeclaredExtensions.declaredExtensions);
	}
	 
}
