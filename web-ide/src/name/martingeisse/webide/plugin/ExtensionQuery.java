/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QDeclaredExtensions;
import name.martingeisse.webide.entity.QExtensionBindings;

import org.json.simple.JSONValue;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.sql.SQLQuery;

/**
 * Callers may use this class to ask for extensions bound
 * to specific extension points. The search for extensions
 * is done within a workspace and optionally for a specific
 * user. If a user is specified, then the search typically
 * finds more results (from plugins installed by the user),
 * otherwise it finds only those installed in the workspace.
 */
public final class ExtensionQuery {

	/**
	 * the workspaceId
	 */
	private final long workspaceId;

	/**
	 * the userId
	 */
	private final Long userId;

	/**
	 * the extensionPointName
	 */
	private final String extensionPointName;

	/**
	 * Constructor.
	 * @param workspaceId the ID of the workspace
	 * @param userId the ID of the user, or null for a workspace search
	 * @param extensionPointName the name of the extension point
	 */
	public ExtensionQuery(final long workspaceId, final Long userId, final String extensionPointName) {
		this.workspaceId = workspaceId;
		this.userId = userId;
		this.extensionPointName = extensionPointName;
	}

	/**
	 * Getter method for the workspaceId.
	 * @return the workspaceId
	 */
	public long getWorkspaceId() {
		return workspaceId;
	}
	
	/**
	 * Getter method for the userId.
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * Getter method for the extensionPointName.
	 * @return the extensionPointName
	 */
	public String getExtensionPointName() {
		return extensionPointName;
	}

	/**
	 * Fetches the extensions bound to the extension point for the workspace
	 * resource, and returns the descriptors for them.
	 * 
	 * @return the extension descriptors (parsed JSON)
	 */
	public List<Result> fetch() {
		long networkId = InternalPluginUtil.getExtensionNetworkId(workspaceId, userId);
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QExtensionBindings.extensionBindings, QDeclaredExtensions.declaredExtensions);
		query.where(QExtensionBindings.extensionBindings.declaredExtensionId.eq(QDeclaredExtensions.declaredExtensions.id));
		query.where(QExtensionBindings.extensionBindings.extensionNetworkId.eq(networkId));
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
	 * Fetches the extensions bound to the specified extension point
	 * and returns the descriptors for them.
	 * 
	 * @param workspaceId the ID of the workspace
	 * @param userId the ID of the user, or null for a workspace search
	 * @param extensionPointName the name of the extension point
	 * @return the extensions
	 */
	public static List<Result> fetch(final long workspaceId, final Long userId, final String extensionPointName) {
		return new ExtensionQuery(workspaceId, userId, extensionPointName).fetch();
	}
	
	/**
	 * Represents a single extension that was found by an extension query.
	 */
	public static final class Result {

		/**
		 * the descriptor
		 */
		private final Object descriptor;

		/**
		 * the pluginBundleId
		 */
		private final long pluginBundleId;

		/**
		 * Constructor.
		 */
		Result(final Object descriptor, final long pluginBundleId) {
			this.descriptor = descriptor;
			this.pluginBundleId = pluginBundleId;
		}
		
		/**
		 * Getter method for the descriptor.
		 * @return the descriptor
		 */
		public Object getDescriptor() {
			return descriptor;
		}

		/**
		 * Getter method for the pluginBundleId.
		 * @return the pluginBundleId
		 */
		public long getPluginBundleId() {
			return pluginBundleId;
		}

	}
	
}
