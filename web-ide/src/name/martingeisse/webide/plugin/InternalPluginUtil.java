/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import java.util.ArrayList;
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
import name.martingeisse.webide.entity.PluginVersions;
import name.martingeisse.webide.entity.QBuiltinPlugins;
import name.martingeisse.webide.entity.QDeclaredExtensionPoints;
import name.martingeisse.webide.entity.QDeclaredExtensions;
import name.martingeisse.webide.entity.QExtensionBindings;
import name.martingeisse.webide.entity.QExtensionNetworks;
import name.martingeisse.webide.entity.QPluginBundles;
import name.martingeisse.webide.entity.QPluginVersions;
import name.martingeisse.webide.entity.QUserInstalledPlugins;
import name.martingeisse.webide.entity.QWorkspaceInstalledPlugins;

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
	 * Like generateDeclaredExtensionPointsAndExtensionsForPluginVersion(pluginVersionId) for
	 * all the specified IDs.
	 * 
	 * @param pluginVersionIds the IDs of the plugin versions
	 */
	public static void generateDeclaredExtensionPointsAndExtensionsForPluginVersions(final Iterable<Long> pluginVersionIds) {
		for (final long pluginVersionId : pluginVersionIds) {
			generateDeclaredExtensionPointsAndExtensionsForPluginVersion(pluginVersionId);
		}
	}

	/**
	 * Reads the descriptor of all bundles of the specified plugin version, then inserts
	 * declared extension points and extensions. This should be called only once
	 * for each plugin version -- previously generated extension points and extensions
	 * might be referred to already, so they cannot be safely removed.
	 * 
	 * @param pluginVersionId the ID of the plugin version
	 */
	public static void generateDeclaredExtensionPointsAndExtensionsForPluginVersion(final long pluginVersionId) {
		if (fetchPluginVersionById(pluginVersionId).getIsUnpacked()) {
			return;
		}
		final List<JsonAnalyzer> emptyList = new ArrayList<JsonAnalyzer>();
		final Map<String, JsonAnalyzer> emptyMap = new HashMap<String, JsonAnalyzer>();
		final List<PluginBundles> bundles = fetchPluginBundlesByPluginVersionId(pluginVersionId);
		for (final PluginBundles bundle : bundles) {
			final String descriptor = bundle.getDescriptor();
			final JsonAnalyzer analyzer = JsonAnalyzer.parse(descriptor);
			
			// syntax check
			if (analyzer.isNull()) {
				throw new RuntimeException("invalid bundle descriptor syntax for plugin bundle id " + bundle.getId());
			}
			
			// extension points
			for (JsonAnalyzer entry : analyzer.analyzeMapElement("extension_points").fallback(emptyList).analyzeList()) {
				String name = entry.analyzeMapElement("name").expectString();
				SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QDeclaredExtensionPoints.declaredExtensionPoints);
				insert.set(QDeclaredExtensionPoints.declaredExtensionPoints.pluginBundleId, bundle.getId());
				insert.set(QDeclaredExtensionPoints.declaredExtensionPoints.name, name);
				insert.execute();
			}
			
			// extensions
			for (Map.Entry<String, JsonAnalyzer> entry : analyzer.analyzeMapElement("extensions").fallback(emptyMap).analyzeMap().entrySet()) {
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
		setPluginVersionUnpacked(pluginVersionId);
	}

	/**
	 * Clears all plugin bindings for the specified workspace, for all users
	 * and for the workspace itself.
	 * 
	 * This method is used when a plugin has been installed or removed for the workspace.
	 * 
	 * @param workspaceId the workspace id
	 */
	public static void clearWorkspacePluginBindings(long workspaceId) {
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QExtensionNetworks.extensionNetworks);
		delete.where(QExtensionNetworks.extensionNetworks.workspaceId.eq(workspaceId));
		delete.execute();
	}
	
	/**
	 * Clears plugin bindings for the specified user, for all workspaces.
	 * 
	 * This method is used when a plugin has been installed or removed for the user.
	 * 
	 * @param userId the user id
	 */
	public static void clearUserPluginBindings(long userId) {
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QExtensionNetworks.extensionNetworks);
		delete.where(QExtensionNetworks.extensionNetworks.userId.eq(userId));
		delete.execute();
	}
	
	/**
	 * Clears a single workspace/user extension network.
	 * 
	 * This method is used when a user refreshes the staging plugins in a workspace.
	 * 
	 * @param workspaceId the workspace id
	 * @param userId the user id, or null to affect all users
	 */
	public static void clearWorkspaceUserPluginBindings(long workspaceId, long userId) {
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QExtensionNetworks.extensionNetworks);
		delete.where(QExtensionNetworks.extensionNetworks.workspaceId.eq(workspaceId));
		delete.where(QExtensionNetworks.extensionNetworks.userId.eq(userId));
		delete.execute();
	}
	
	/**
	 * Fetches the ID of the plugin network for the specified workspace (and optionally user)
	 * from the database. If no user ID is specified then the network for the workspace itself
	 * is fetched.
	 * 
	 * The network gets built by this method if it does not exist yet, for example if previously
	 * cleared by {@link #clearWorkspacePluginBindings(long)} or {@link #clearUserPluginBindings(long)}.
	 * 
	 * @param workspaceId the ID of the workspace
	 * @param userId the ID of the user, or null to fetch the network for the workspace
	 * @return the ID of the extension network
	 */
	public static long getExtensionNetworkId(long workspaceId, Long userId) {
		
		// try an existing network
		{
			final QExtensionNetworks qnet = QExtensionNetworks.extensionNetworks;
			final BooleanExpression workspaceIdMatch = qnet.workspaceId.eq(workspaceId);
			final BooleanExpression userIdMatch = (userId == null ? qnet.userId.isNull() : qnet.userId.eq(userId));
			final Long existingNetworkId = QueryUtil.fetchSingle(qnet, qnet.id, workspaceIdMatch, userIdMatch);
			if (existingNetworkId != null) {
				return existingNetworkId;
			}
			
		}
		
		// determine the plugin versions that contribute to the network
		Set<Long> pluginVersionIds = new HashSet<Long>();
		pluginVersionIds.addAll(fetchBuiltinPluginVersionIds());
		pluginVersionIds.addAll(fetchWorkspaceInstalledPluginVersionIds(workspaceId));
		pluginVersionIds.addAll(fetchWorkspaceStagingPluginVersionIds(workspaceId));
		if (userId != null) {
			pluginVersionIds.addAll(fetchUserInstalledPluginVersionIds(userId));
		}
		
		// fetch plug-in data
		final List<Long> pluginBundleIds = fetchPluginBundleIds(pluginVersionIds);
		final List<DeclaredExtensionPoints> declaredExtensionPoints = fetchDeclaredExtensionPoints(pluginBundleIds);
		final List<DeclaredExtensions> declaredExtensions = fetchDeclaredExtensions(pluginBundleIds);

		// map extension points by name
		final Map<String, DeclaredExtensionPoints> declaredExtensionPointsByName = new HashMap<String, DeclaredExtensionPoints>();
		for (final DeclaredExtensionPoints extensionPoint : declaredExtensionPoints) {
			if (declaredExtensionPointsByName.put(extensionPoint.getName(), extensionPoint) != null) {
				throw new RuntimeException("duplicate extension point: " + extensionPoint.getName());
			}
		}

		// create an empty network
		final long newNetworkId;
		{
			final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QExtensionNetworks.extensionNetworks);
			insert.set(QExtensionNetworks.extensionNetworks.workspaceId, workspaceId);
			insert.set(QExtensionNetworks.extensionNetworks.userId, userId);
			newNetworkId = insert.execute();
		}
		
		// insert the new bindings
		for (final DeclaredExtensions extension : declaredExtensions) {
			final String extensionPointName = extension.getExtensionPointName();
			final DeclaredExtensionPoints extensionPoint = declaredExtensionPointsByName.get(extensionPointName);
			if (extensionPoint == null) {
				throw new RuntimeException("extension for unknown extension point: " + extensionPointName);
			}
			final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QExtensionBindings.extensionBindings);
			insert.set(QExtensionBindings.extensionBindings.extensionNetworkId, newNetworkId);
			insert.set(QExtensionBindings.extensionBindings.declaredExtensionPointId, extensionPoint.getId());
			insert.set(QExtensionBindings.extensionBindings.declaredExtensionId, extension.getId());
			insert.execute();
		}

		return newNetworkId;
	}
	
	// ------------------------------------------------------------------------------------------------
	// utility methods
	// ------------------------------------------------------------------------------------------------

	/**
	 * Returns the plugin version with the specified ID.
	 */
	private static PluginVersions fetchPluginVersionById(long pluginVersionId) {
		return QueryUtil.fetchSingle(QPluginVersions.pluginVersions, QPluginVersions.pluginVersions.id.eq(pluginVersionId));
	}
	
	/**
	 * Sets the is_unpacked flag of the specified plugin version to true.
	 */
	private static void setPluginVersionUnpacked(long pluginVersionId) {
		SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QPluginVersions.pluginVersions);
		update.where(QPluginVersions.pluginVersions.id.eq(pluginVersionId));
		update.set(QPluginVersions.pluginVersions.isUnpacked, true);
		update.execute();
	}
	
	/**
	 * Returns the plugin bundles that belong to the specified plugin version.
	 */
	private static List<PluginBundles> fetchPluginBundlesByPluginVersionId(long pluginVersionId) {
		return QueryUtil.fetchMultiple(QPluginBundles.pluginBundles, QPluginBundles.pluginBundles.pluginVersionId.eq(pluginVersionId));
	}

	/**
	 * Returns the pluginVersionIds of all built-in plugins.
	 */
	private static List<Long> fetchBuiltinPluginVersionIds() {
		return QueryUtil.fetchAll(QBuiltinPlugins.builtinPlugins, QBuiltinPlugins.builtinPlugins.pluginVersionId);
	}

	/**
	 * Returns the pluginIds of all workspace-installed plugins.
	 */
	private static List<Long> fetchWorkspaceInstalledPluginVersionIds(long workspaceId) {
		final QWorkspaceInstalledPlugins qwip = QWorkspaceInstalledPlugins.workspaceInstalledPlugins;
		final QPluginVersions qpv = QPluginVersions.pluginVersions;
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query = query.from(qwip, qpv);
		query = query.where(qwip.pluginPublicId.eq(qpv.pluginPublicId));
		query = query.where(qwip.workspaceId.eq(workspaceId));
		query = query.where(qpv.stagingWorkspaceId.isNull());
		query = query.where(qpv.isActive.isTrue());
		return query.list(qpv.id);
	}
	
	/**
	 * Returns the pluginIds of all user-installed plugins.
	 */
	private static List<Long> fetchUserInstalledPluginVersionIds(long userId) {
		final QUserInstalledPlugins quip = QUserInstalledPlugins.userInstalledPlugins;
		final QPluginVersions qpv = QPluginVersions.pluginVersions;
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query = query.from(quip, qpv);
		query = query.where(quip.pluginPublicId.eq(qpv.pluginPublicId));
		query = query.where(quip.userId.eq(userId));
		query = query.where(qpv.stagingWorkspaceId.isNull());
		query = query.where(qpv.isActive.isTrue());
		return query.list(qpv.id);
	}

	/**
	 * Returns the pluginIds of all workspace staging plugins.
	 */
	private static List<Long> fetchWorkspaceStagingPluginVersionIds(long workspaceId) {
		QPluginVersions q = QPluginVersions.pluginVersions;
		return QueryUtil.fetchMultiple(q, q.id, q.stagingWorkspaceId.eq(workspaceId), q.isActive.isTrue());
	}

	/**
	 * Returns the plugin bundle IDs for all bundles that belong to any of
	 * the specified plugin versions.
	 */
	private static List<Long> fetchPluginBundleIds(final Collection<Long> pluginVersionIds) {
		return QueryUtil.fetchMultiple(QPluginBundles.pluginBundles, QPluginBundles.pluginBundles.id, QPluginBundles.pluginBundles.pluginVersionId.in(pluginVersionIds));
	}

	/**
	 * Returns the declared extension points that belong to any of the specified plugin bundles
	 * or are provided by the system.
	 */
	private static List<DeclaredExtensionPoints> fetchDeclaredExtensionPoints(final List<Long> pluginBundleIds) {
		return QueryUtil.fetchMultiple(QDeclaredExtensionPoints.declaredExtensionPoints, QDeclaredExtensionPoints.declaredExtensionPoints.pluginBundleId.in(pluginBundleIds));
	}

	/**
	 * Returns the declared extensions that belong to any of the specified plugin bundles.
	 */
	private static List<DeclaredExtensions> fetchDeclaredExtensions(final List<Long> pluginBundleIds) {
		return QueryUtil.fetchMultiple(QDeclaredExtensions.declaredExtensions, QDeclaredExtensions.declaredExtensions.pluginBundleId.in(pluginBundleIds));
	}

}
