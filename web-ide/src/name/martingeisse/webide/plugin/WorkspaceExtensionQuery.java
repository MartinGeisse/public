/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.QueryUtil;
import name.martingeisse.webide.entity.QDeclaredExtensions;
import name.martingeisse.webide.entity.QUserExtensionBindings;
import name.martingeisse.webide.entity.QWorkspaceExtensionBindings;
import name.martingeisse.webide.entity.QWorkspaceExtensionNetworks;
import name.martingeisse.webide.entity.WorkspaceExtensionNetworks;
import name.martingeisse.webide.plugin.InternalPluginUtil.ExtensionNetworkBuilder;
import name.martingeisse.webide.resources.ResourceHandle;
import name.martingeisse.webide.resources.ResourcePath;

import org.json.simple.JSONValue;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.Pair;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * Plugins may use this class to ask for extensions bound
 * to specific extension points for a workspace resource
 * that binds plugins.
 */
public final class WorkspaceExtensionQuery extends AbstractExtensionQuery {

	/**
	 * the workspaceId
	 */
	private final long workspaceId;

	/**
	 * the anchorPath
	 */
	private final String anchorPath;

	/**
	 * Constructor.
	 * @param resourceHandle the handle of the resource that binds the plugins
	 * @param extensionPointName the name of the extension point
	 */
	public WorkspaceExtensionQuery(final ResourceHandle resourceHandle, final String extensionPointName) {
		this(resourceHandle.getWorkspaceId(), resourceHandle.getPath(), extensionPointName);
	}
	
	/**
	 * Constructor.
	 * @param workspaceId the ID of the workspace
	 * @param anchorPath the path to the anchor file that binds the plugins
	 * @param extensionPointName the name of the extension point
	 */
	public WorkspaceExtensionQuery(final long workspaceId, final ResourcePath anchorPath, final String extensionPointName) {
		this(workspaceId, anchorPath.toString(), extensionPointName);
		
	}
	
	/**
	 * Constructor.
	 * @param workspaceId the ID of the workspace
	 * @param anchorPath the path to the anchor file that binds the plugins
	 * @param extensionPointName the name of the extension point
	 */
	public WorkspaceExtensionQuery(final long workspaceId, final String anchorPath, final String extensionPointName) {
		super(extensionPointName);
		this.workspaceId = workspaceId;
		this.anchorPath = anchorPath;
	}

	/**
	 * Getter method for the workspaceId.
	 * @return the workspaceId
	 */
	public long getWorkspaceId() {
		return workspaceId;
	}
	
	/**
	 * Getter method for the anchorPath.
	 * @return the anchorPath
	 */
	public String getAnchorPath() {
		return anchorPath;
	}

	/**
	 * Fetches the extensions bound to the extension point for the workspace
	 * resource, and returns the descriptors for them.
	 * 
	 * @return the extension descriptors (parsed JSON)
	 */
	public List<Result> fetch() {
		long networkId = fetchExtensionNetworkId();
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QWorkspaceExtensionBindings.workspaceExtensionBindings, QDeclaredExtensions.declaredExtensions);
		query.where(QWorkspaceExtensionBindings.workspaceExtensionBindings.declaredExtensionId.eq(QDeclaredExtensions.declaredExtensions.id));
		query.where(QWorkspaceExtensionBindings.workspaceExtensionBindings.workspaceExtensionNetworkId.eq(networkId));
		query.where(QDeclaredExtensions.declaredExtensions.extensionPointName.eq(getExtensionPointName()));
		final CloseableIterator<Object[]> iterator = query.iterate(QDeclaredExtensions.declaredExtensions.descriptor, QDeclaredExtensions.declaredExtensions.pluginBundleId);
		try {
			final List<Result> results = new ArrayList<Result>();
			while (iterator.hasNext()) {
				Object[] row = iterator.next();
				String rawDescriptor = (String)row[0];
				long pluginBundleId = (Long)row[1];
				results.add(new Result(JSONValue.parse(rawDescriptor), pluginBundleId));
			}
			return results;
		} finally {
			iterator.close();
		}
	}
	
	/**
	 * Fetches the ID of the extension network, building it if not yet present.
	 */
	private long fetchExtensionNetworkId() {
		
		// try an existing network
		final QWorkspaceExtensionNetworks qnet = QWorkspaceExtensionNetworks.workspaceExtensionNetworks;
		final Long existingNetworkId = QueryUtil.fetchSingle(qnet, qnet.id, qnet.workspaceId.eq(workspaceId), qnet.anchorPath.eq(anchorPath));
		if (existingNetworkId != null) {
			return existingNetworkId;
		}
		
		// create an empty network
		final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QWorkspaceExtensionNetworks.workspaceExtensionNetworks);
		insert.set(QWorkspaceExtensionNetworks.workspaceExtensionNetworks.workspaceId, workspaceId);
		insert.set(QWorkspaceExtensionNetworks.workspaceExtensionNetworks.anchorPath, anchorPath);
		final long newNetworkId = insert.execute();
		
		// build the network
		new ExtensionNetworkBuilder() {
			@Override
			protected void insertBinding(long extensionPointId, long extensionId) {
				final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QWorkspaceExtensionBindings.workspaceExtensionBindings);
				insert.set(QWorkspaceExtensionBindings.workspaceExtensionBindings.workspaceExtensionNetworkId, newNetworkId);
				insert.set(QWorkspaceExtensionBindings.workspaceExtensionBindings.declaredExtensionPointId, extensionPointId);
				insert.set(QWorkspaceExtensionBindings.workspaceExtensionBindings.declaredExtensionId, extensionId);
				insert.execute();
			}
		}.build(pluginVersionIds);
		
		return newNetworkId;
	}

	/**
	 * Fetches the extensions bound to the extension point for the resource, and
	 * returns the descriptors for them.
	 * 
	 * @param resourceHandle the handle of the resource that binds the plugins
	 * @param extensionPointName the name of the extension point
	 * @return the extension descriptors (parsed JSON)
	 */
	public static List<Result> fetch(final ResourceHandle resourceHandle, final String extensionPointName) {
		return new WorkspaceExtensionQuery(resourceHandle, extensionPointName).fetch();
	}
	
	/**
	 * Fetches the extensions bound to the extension point for the resource, and
	 * returns the descriptors for them.
	 * 
	 * @param workspaceId the ID of the workspace
	 * @param anchorPath the path to the anchor file that binds the plugins
	 * @param extensionPointName the name of the extension point
	 * @return the extension descriptors (parsed JSON)
	 */
	public static List<Result> fetch(final long workspaceId, final ResourcePath anchorPath, final String extensionPointName) {
		return new WorkspaceExtensionQuery(workspaceId, anchorPath, extensionPointName).fetch();
	}
	
	/**
	 * Fetches the extensions bound to the extension point for the resource, and
	 * returns the descriptors for them.
	 * 
	 * @param workspaceId the ID of the workspace
	 * @param anchorPath the path to the anchor file that binds the plugins
	 * @param extensionPointName the name of the extension point
	 * @return the extension descriptors (parsed JSON)
	 */
	public static List<Result> fetch(final long workspaceId, final String anchorPath, final String extensionPointName) {
		return new WorkspaceExtensionQuery(workspaceId, anchorPath, extensionPointName).fetch();
	}

}
