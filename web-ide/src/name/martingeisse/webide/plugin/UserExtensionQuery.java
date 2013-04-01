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
import name.martingeisse.webide.entity.QUserExtensionBindings;

import org.json.simple.JSONValue;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.sql.SQLQuery;

/**
 * Plugins may use this class to ask for extensions bound
 * to specific extension points for a user.
 */
public final class UserExtensionQuery extends AbstractExtensionQuery {

	/**
	 * the userId
	 */
	private final long userId;

	/**
	 * Constructor.
	 * @param userId the ID of the user
	 * @param extensionPointName the name of the extension point
	 */
	public UserExtensionQuery(final long userId, final String extensionPointName) {
		super(extensionPointName);
		this.userId = userId;
	}

	/**
	 * Getter method for the userId.
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Fetches the extensions bound to the extension point for the user, and
	 * returns the descriptors for them.
	 * 
	 * @return the extension descriptors (parsed JSON)
	 */
	public List<Result> fetch() {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QUserExtensionBindings.userExtensionBindings, QDeclaredExtensions.declaredExtensions);
		query.where(QUserExtensionBindings.userExtensionBindings.declaredExtensionId.eq(QDeclaredExtensions.declaredExtensions.id));
		query.where(QUserExtensionBindings.userExtensionBindings.userId.eq(userId));
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
	 * Fetches the extensions bound to the extension point for the user, and
	 * returns the descriptors for them.
	 * 
	 * @param userId the user ID
	 * @param extensionPointName the name of the extension point
	 * @return the extension descriptors (parsed JSON)
	 */
	public static List<Result> fetch(final long userId, final String extensionPointName) {
		return new UserExtensionQuery(userId, extensionPointName).fetch();
	}

}
