/**
 * Copyright (c) 2012 Shopgate GmbH
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
 * Plugins may use this class to ask for extensions bound
 * to specific extension points for a user.
 */
public final class ExtensionQuery {

	/**
	 * the userId
	 */
	private long userId;

	/**
	 * the extensionPointName
	 */
	private String extensionPointName;

	/**
	 * Constructor.
	 */
	public ExtensionQuery() {
	}

	/**
	 * Constructor.
	 * @param userId the ID of the user
	 * @param extensionPointName the name of the extension point
	 */
	public ExtensionQuery(final long userId, final String extensionPointName) {
		this.userId = userId;
		this.extensionPointName = extensionPointName;
	}

	/**
	 * Getter method for the userId.
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Setter method for the userId.
	 * @param userId the userId to set
	 */
	public void setUserId(final long userId) {
		this.userId = userId;
	}

	/**
	 * Getter method for the extensionPointName.
	 * @return the extensionPointName
	 */
	public String getExtensionPointName() {
		return extensionPointName;
	}

	/**
	 * Setter method for the extensionPointName.
	 * @param extensionPointName the extensionPointName to set
	 */
	public void setExtensionPointName(final String extensionPointName) {
		this.extensionPointName = extensionPointName;
	}

	/**
	 * Fetches the extensions bound to the extension point for the user, and
	 * returns the descriptors for them.
	 * 
	 * @return the extension descriptors (parsed JSON)
	 */
	public List<Result> fetch() {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QExtensionBindings.extensionBindings, QDeclaredExtensions.declaredExtensions);
		query.where(QExtensionBindings.extensionBindings.declaredExtensionId.eq(QDeclaredExtensions.declaredExtensions.id));
		query.where(QExtensionBindings.extensionBindings.userId.eq(userId));
		query.where(QDeclaredExtensions.declaredExtensions.extensionPointName.eq(extensionPointName));
		final CloseableIterator<Object[]> iterator = query.iterate(QDeclaredExtensions.declaredExtensions.descriptor, QDeclaredExtensions.declaredExtensions.pluginBundleId);
		try {
			final List<Result> results = new ArrayList<Result>();
			while (iterator.hasNext()) {
				Object[] row = iterator.next();
				String rawDescriptor = (String)row[0];
				long pluginBundleId = (Long)row[1];
				results.add(new Result(JSONValue.parse(rawDescriptor), new PluginBundleHandle(pluginBundleId)));
			}
			return results;
		} finally {
			iterator.close();
		}
	}

	/**
	 * Fetches the extensions bound to the extension point for the user, and
	 * returns the descriptors for them.
	 * 
	 * @param userId the user ID
	 * @param extensionPointName the name of the extension point
	 * @return the extension descriptors (parsed JSON)
	 */
	public static List<Result> fetch(final long userId, final String extensionPointName) {
		return new ExtensionQuery(userId, extensionPointName).fetch();
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
		 * the bundleHandle
		 */
		private final PluginBundleHandle bundleHandle;

		/**
		 * Constructor.
		 */
		Result(final Object descriptor, final PluginBundleHandle bundleHandle) {
			this.descriptor = descriptor;
			this.bundleHandle = bundleHandle;
		}
		
		/**
		 * Getter method for the descriptor.
		 * @return the descriptor
		 */
		public Object getDescriptor() {
			return descriptor;
		}
		
		/**
		 * Getter method for the bundleHandle.
		 * @return the bundleHandle
		 */
		public PluginBundleHandle getBundleHandle() {
			return bundleHandle;
		}

	}

}
