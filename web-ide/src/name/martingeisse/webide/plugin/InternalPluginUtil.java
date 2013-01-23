/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.QueryUtil;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.entity.DeclaredExtensionPoints;
import name.martingeisse.webide.entity.DeclaredExtensions;
import name.martingeisse.webide.entity.PluginBundles;
import name.martingeisse.webide.entity.Plugins;
import name.martingeisse.webide.entity.QBuiltinPlugins;
import name.martingeisse.webide.entity.QDeclaredExtensionPoints;
import name.martingeisse.webide.entity.QDeclaredExtensions;
import name.martingeisse.webide.entity.QExtensionBindings;
import name.martingeisse.webide.entity.QPluginBundles;
import name.martingeisse.webide.entity.QPlugins;
import name.martingeisse.webide.entity.QUserInstalledPlugins;
import name.martingeisse.webide.entity.QWorkspaceStagingPlugins;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.operation.FetchSingleResourceOperation;

import org.json.simple.JSONValue;

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
	 * Updates the user's plugin run-time data from the sets of built-in plugins, installed plugins for that
	 * user, and currently available workspace staging plugins for the current workspace.
	 */
	public static void updateUsersPlugins() {
		
		// TODO: support multiple users, multiple workspaces
		final long userId = 1;
		final long workspaceRootId = findWorkspaceRoot();

		// determine the set of active plugins
		Set<Long> pluginIds = new HashSet<Long>();
		pluginIds.addAll(fetchBuiltinPluginIds());
		pluginIds.addAll(fetchUserInstalledPluginIds(userId));
		pluginIds.addAll(fetchWorkspaceStagingPluginIds(workspaceRootId));
		
		// fetch plug-in data
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
	
	

	// ------------------------------------------------------------------------------------------------
	// utility methods
	// ------------------------------------------------------------------------------------------------

	/**
	 * Returns the plugin with the specified ID.
	 */
	private static Plugins fetchPluginById(long pluginId) {
		return QueryUtil.fetchSingle(QPlugins.plugins, QPlugins.plugins.id.eq(pluginId));
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
		return QueryUtil.fetchMultiple(QPluginBundles.pluginBundles, QPluginBundles.pluginBundles.pluginId.eq(pluginId));
	}

	/**
	 * Deletes any existing extension bindings for the specified user.
	 */
	private static void deleteExtensionBindingsForUser(final long userId) {
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QExtensionBindings.extensionBindings);
		delete.where(QExtensionBindings.extensionBindings.userId.eq(userId)).execute();
	}

	/**
	 * Returns the pluginIds of all built-in plugins.
	 */
	private static List<Long> fetchBuiltinPluginIds() {
		return QueryUtil.fetchAll(QBuiltinPlugins.builtinPlugins, QBuiltinPlugins.builtinPlugins.pluginId);
	}

	/**
	 * Returns the pluginIds of all user-installed plugins.
	 */
	private static List<Long> fetchUserInstalledPluginIds(long userId) {
		QUserInstalledPlugins qpath = QUserInstalledPlugins.userInstalledPlugins;
		return QueryUtil.fetchMultiple(qpath, qpath.pluginId, qpath.userId.eq(userId));
	}

	/**
	 * Returns the pluginIds of all workspace staging plugins.
	 */
	private static List<Long> fetchWorkspaceStagingPluginIds(long workspaceRootId) {
		QWorkspaceStagingPlugins qpath = QWorkspaceStagingPlugins.workspaceStagingPlugins;
		return QueryUtil.fetchMultiple(qpath, qpath.pluginId, qpath.workspaceResourceId.eq(workspaceRootId));
	}

	/**
	 * Returns the plugin bundle IDs for all bundles that belong to any of
	 * the specified plugins.
	 */
	private static List<Long> fetchPluginBundleIds(final Collection<Long> pluginIds) {
		return QueryUtil.fetchMultiple(QPluginBundles.pluginBundles, QPluginBundles.pluginBundles.id, QPluginBundles.pluginBundles.pluginId.in(pluginIds));
	}

	/**
	 * Returns the declared extension points that belong to any of the specified plugin bundles
	 * or are provided by the system.
	 */
	private static List<DeclaredExtensionPoints> fetchDeclaredExtensionPoints(final List<Long> pluginBundleIds) {
		BooleanExpression inSpecifiedBundles = QDeclaredExtensionPoints.declaredExtensionPoints.pluginBundleId.in(pluginBundleIds);
		BooleanExpression isSystemExtensionPoint = QDeclaredExtensionPoints.declaredExtensionPoints.pluginBundleId.isNull();
		return QueryUtil.fetchMultiple(QDeclaredExtensionPoints.declaredExtensionPoints, inSpecifiedBundles.or(isSystemExtensionPoint));
	}

	/**
	 * Returns the declared extensions that belong to any of the specified plugin bundles.
	 */
	private static List<DeclaredExtensions> fetchDeclaredExtensions(final List<Long> pluginBundleIds) {
		return QueryUtil.fetchMultiple(QDeclaredExtensions.declaredExtensions, QDeclaredExtensions.declaredExtensions.pluginBundleId.in(pluginBundleIds));
	}

	/**
	 * Finds the workspace resource id of the workspace root.
	 */
	private static long findWorkspaceRoot() {
		FetchSingleResourceOperation operation = new FetchSingleResourceOperation(ResourcePath.ROOT);
		operation.run();
		return operation.getResult().getId();
	}
	
}
