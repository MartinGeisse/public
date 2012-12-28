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
	public ExtensionQuery(long userId, String extensionPointName) {
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
	public void setUserId(long userId) {
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
	public void setExtensionPointName(String extensionPointName) {
		this.extensionPointName = extensionPointName;
	}

	/**
	 * Fetches the extensions bound to the extension point for the user, and
	 * returns the descriptors for them.
	 * 
	 * @return the extension descriptors (parsed JSON)
	 */
	public List<Object> fetch() {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QExtensionBindings.extensionBindings, QDeclaredExtensions.declaredExtensions);
		query.where(QExtensionBindings.extensionBindings.declaredExtensionId.eq(QDeclaredExtensions.declaredExtensions.id));
		query.where(QExtensionBindings.extensionBindings.userId.eq(userId));
		query.where(QDeclaredExtensions.declaredExtensions.extensionPointName.eq(extensionPointName));
		CloseableIterator<String> iterator = query.iterate(QDeclaredExtensions.declaredExtensions.descriptor);
		try {
			List<Object> result = new ArrayList<Object>();
			while (iterator.hasNext()) {
				result.add(JSONValue.parse(iterator.next()));
			}
			return result;
		} finally {
			iterator.close();
		}
	}
	
}
