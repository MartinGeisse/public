/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.tools;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.IdeLauncher;
import name.martingeisse.webide.entity.QPlugins;
import name.martingeisse.webide.plugin.InternalPluginUtil;

import com.mysema.query.sql.SQLQuery;

/**
 * The main class.
 */
public class UnpackPluginsMain {

	/**
	 * The main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {
		IdeLauncher.initialize();
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		InternalPluginUtil.generateDeclaredExtensionPointsAndExtensionsForPlugins(query.from(QPlugins.plugins).list(QPlugins.plugins.id));
		InternalPluginUtil.updateExtensionBindingsForUser(1);
	}
	
}
