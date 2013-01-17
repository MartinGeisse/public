/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.entity.DeclaredExtensionPoints;
import name.martingeisse.webide.entity.DeclaredExtensions;
import name.martingeisse.webide.entity.PluginBundles;
import name.martingeisse.webide.entity.Plugins;
import name.martingeisse.webide.entity.QDeclaredExtensionPoints;
import name.martingeisse.webide.entity.QDeclaredExtensions;
import name.martingeisse.webide.entity.QExtensionBindings;
import name.martingeisse.webide.entity.QPluginBundles;
import name.martingeisse.webide.entity.QPlugins;
import name.martingeisse.webide.entity.QUserPlugins;

import org.json.simple.JSONValue;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.expr.BooleanExpression;

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
	public static void generateDeclaredExtensionPointsAndExtensionsForPlugins(final Iterable<Long> pluginIds) {
		for (final long pluginId : pluginIds) {
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
	public static void generateDeclaredExtensionPointsAndExtensionsForPlugin(final long pluginId) {
		if (fetchPluginById(pluginId).getIsUnpacked()) {
			return;
		}
		final List<PluginBundles> bundles = fetchPluginBundlesByPluginId(pluginId);
		for (final PluginBundles bundle : bundles) {
			final String descriptor = bundle.getDescriptor();
			final JsonAnalyzer analyzer = JsonAnalyzer.parse(descriptor);
			
			// extension points
			// TODO
			// final JsonAnalyzer extensionPointsAnalyzer = analyzer.analyzeMapElement("extension_points");
			
			// extensions
			for (Map.Entry<String, JsonAnalyzer> entry : analyzer.analyzeMapElement("extensions").analyzeMap().entrySet()) {
				String extensionPointName = entry.getKey();
				for (JsonAnalyzer extensionAnalyzer : entry.getValue().analyzeListOrSingle()) {
					SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QDeclaredExtensions.declaredExtensions);
					insert.set(QDeclaredExtensions.declaredExtensions.pluginBundleId, bundle.getId());
					insert.set(QDeclaredExtensions.declaredExtensions.extensionPointName, extensionPointName);
					insert.set(QDeclaredExtensions.declaredExtensions.descriptor, JSONValue.toJSONString(extensionAnalyzer.getValue()));
					insert.execute();
				}
			}
			
		}
		setPluginUnpacked(pluginId);
	}

	/**
	 * Like updateExtensionBindingsForUser(userId) for all the specified IDs.
	 * 
	 * @param userIds the IDs of the users
	 */
	public static void updateExtensionBindingsForUsers(final Iterable<Long> userIds) {
		for (final long userId : userIds) {
			updateExtensionBindingsForUser(userId);
		}
	}

	/**
	 * Rebuilds the set of installed staging plugins for the specified user
	 * by fetching the set of staging plugins for the current workspace.
	 * Non-staging plugins are not affected.
	 * 
	 * @param userId the user ID
	 */
	public static void updateStagingPluginsForUser(final long userId) {
		
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
	public static void updateExtensionBindingsForUser(final long userId) {

		// fetch data
		final List<Long> pluginIds = fetchInstalledPluginIdsForUser(userId);
		final List<Long> pluginBundleIds = fetchPluginBundleIds(pluginIds);
		final List<DeclaredExtensionPoints> declaredExtensionPoints = fetchDeclaredExtensionPoints(pluginBundleIds);
		final List<DeclaredExtensions> declaredExtensions = fetchDeclaredExtensions(pluginBundleIds);

		// map extension points by name
		final Map<String, DeclaredExtensionPoints> declaredExtensionPointsByName = new HashMap<String, DeclaredExtensionPoints>();
		for (final DeclaredExtensionPoints extensionPoint : declaredExtensionPoints) {
			if (declaredExtensionPointsByName.put(extensionPoint.getName(), extensionPoint) != null) {
				throw new RuntimeException("duplicate extension point: " + extensionPoint.getName());
			}
		}

		// delete any previously existing bindings
		deleteExtensionBindingsForUser(userId);

		// insert the new bindings
		for (final DeclaredExtensions extension : declaredExtensions) {
			final String extensionPointName = extension.getExtensionPointName();
			final DeclaredExtensionPoints extensionPoint = declaredExtensionPointsByName.get(extensionPointName);
			if (extensionPoint == null) {
				throw new RuntimeException("extension for unknown extension point: " + extensionPointName);
			}
			final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QExtensionBindings.extensionBindings);
			insert.set(QExtensionBindings.extensionBindings.userId, userId);
			insert.set(QExtensionBindings.extensionBindings.declaredExtensionPointId, extensionPoint.getId());
			insert.set(QExtensionBindings.extensionBindings.declaredExtensionId, extension.getId());
			insert.execute();
		}

	}
	
	/**
	 * Returns a class loader for the JAR file of the specified plugin bundle.
	 * 
	 * @param pluginBundleId the ID of the plugin bundle
	 * @return the class loader
	 */
	public static ClassLoader getPluginBundleClassLoader(final long pluginBundleId) {
		try {
			final URL url = new URL("bundle", null, Long.toString(pluginBundleId));
			return new URLClassLoader(new URL[] {url});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// ------------------------------------------------------------------------------------------------
	// utility methods
	// ------------------------------------------------------------------------------------------------

	/**
	 * Returns the plugin with the specified ID.
	 */
	private static Plugins fetchPluginById(long pluginId) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QPlugins.plugins);
		query.where(QPlugins.plugins.id.eq(pluginId));
		return query.singleResult(QPlugins.plugins);
	}
	
	/**
	 * Sets the is_unpacked flag of the specified plugin to true.
	 */
	private static void setPluginUnpacked(long pluginId) {
		SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QPlugins.plugins);
		update.where(QPlugins.plugins.id.eq(pluginId));
		update.set(QPlugins.plugins.isUnpacked, true);
		update.execute();
	}
	
	/**
	 * Returns the plugin bundles that belong to the specified plugin.
	 */
	private static List<PluginBundles> fetchPluginBundlesByPluginId(long pluginId) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QPluginBundles.pluginBundles);
		query.where(QPluginBundles.pluginBundles.pluginId.eq(pluginId));
		return query.list(QPluginBundles.pluginBundles);
	}

	/**
	 * Deletes any existing extension bindings for the specified user.
	 */
	private static void deleteExtensionBindingsForUser(final long userId) {
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QExtensionBindings.extensionBindings);
		delete.where(QExtensionBindings.extensionBindings.userId.eq(userId)).execute();
	}

	/**
	 * Returns the pluginIds of the plugins the specified user has added.
	 * 
	 * TODO: the word "installed" could mean "added to the plugins table" or
	 * "linked with a user account" -> use different terms!
	 */
	private static List<Long> fetchInstalledPluginIdsForUser(final long userId) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QUserPlugins.userPlugins);
		query.where(QUserPlugins.userPlugins.userId.eq(userId));
		return query.list(QUserPlugins.userPlugins.pluginId);
	}

	/**
	 * Returns the plugin bundle IDs for all bundles that belong to any of
	 * the specified plugins.
	 */
	private static List<Long> fetchPluginBundleIds(final List<Long> pluginIds) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QPluginBundles.pluginBundles);
		query.where(QPluginBundles.pluginBundles.pluginId.in(pluginIds));
		return query.list(QPluginBundles.pluginBundles.id);
	}

	/**
	 * Returns the declared extension points that belong to any of the specified plugin bundles.
	 */
	private static List<DeclaredExtensionPoints> fetchDeclaredExtensionPoints(final List<Long> pluginBundleIds) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QDeclaredExtensionPoints.declaredExtensionPoints);
		BooleanExpression inSpecifiedBundles = QDeclaredExtensionPoints.declaredExtensionPoints.pluginBundleId.in(pluginBundleIds);
		BooleanExpression isSystemExtensionPoint = QDeclaredExtensionPoints.declaredExtensionPoints.pluginBundleId.isNull();
		query.where(inSpecifiedBundles.or(isSystemExtensionPoint));
		return query.list(QDeclaredExtensionPoints.declaredExtensionPoints);
	}

	/**
	 * Returns the declared extensions that belong to any of the specified plugin bundles.
	 */
	private static List<DeclaredExtensions> fetchDeclaredExtensions(final List<Long> pluginBundleIds) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QDeclaredExtensions.declaredExtensions);
		query.where(QDeclaredExtensions.declaredExtensions.pluginBundleId.in(pluginBundleIds));
		return query.list(QDeclaredExtensions.declaredExtensions);
	}

}
