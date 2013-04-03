/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.tools;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.application.IdeLauncher;
import name.martingeisse.webide.entity.QPluginVersions;
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
		IdeLauncher.initialize(false);
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		InternalPluginUtil.generateDeclaredExtensionPointsAndExtensionsForPluginVersions(query.from(QPluginVersions.pluginVersions).list(QPluginVersions.pluginVersions.id));
	}
	
}
