/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.pde;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.Files;
import name.martingeisse.webide.entity.QFiles;
import name.martingeisse.webide.entity.QPluginBundles;
import name.martingeisse.webide.entity.QPlugins;
import name.martingeisse.webide.entity.QUserPlugins;
import name.martingeisse.webide.plugin.InternalPluginUtil;
import name.martingeisse.webide.resources.WorkspaceUtil;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * This façade is used by the builder thread to invoke the plugin builder.
 */
public class PluginBuilderFacade {

	/**
	 * This method is invoked by the builder thread to perform a plugin build.
	 */
	public static void performBuild() {
		WorkspaceUtil.delete("plugin.jar");
		byte[] pluginBundleDescriptorFileContents = WorkspaceUtil.getContents("plugin.json");
		if (pluginBundleDescriptorFileContents == null) {
			return;
		}
		String pluginBundleDescriptorSourceCode = new String(pluginBundleDescriptorFileContents, Charset.forName("utf-8"));
		if (!validateDescriptor(pluginBundleDescriptorSourceCode)) {
			return;
		}
		byte[] jarFile = generateJarFile();
		long pluginId = uploadPlugin(pluginBundleDescriptorSourceCode, jarFile);
		updateUsersPlugins(pluginId);
	}
	
	/**
	 * Validates the specified plugin bundle descriptor.
	 * Returns true on success, false on failure.
	 */
	private static boolean validateDescriptor(String pluginBundleDescriptorSourceCode) {
		// TODO
		return true;
	}

	/**
	 * Generates a JAR file from the compiled classes and also returns the
	 * contents of the file.
	 */
	private static byte[] generateJarFile() {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			JarOutputStream jarOutputStream = new JarOutputStream(byteArrayOutputStream);
			final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
			query.from(QFiles.files).where(QFiles.files.name.endsWith(".jar"));
			CloseableIterator<Files> iterator = query.iterate(QFiles.files);
			try {
				while (iterator.hasNext()) {
					Files file = iterator.next();
					jarOutputStream.putNextEntry(new ZipEntry(file.getName()));
					jarOutputStream.write(file.getContents());
				}
			} finally {
				iterator.close();
			}
			jarOutputStream.close();
			byte[] contents = byteArrayOutputStream.toByteArray();
			WorkspaceUtil.createFile("plugin.jar", contents);
			return contents;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Uploads the JAR file build by the previous step and the plugin bundle descriptor
	 * as a single-bundle plugin into the plugins and plugin_bundles tables.
	 * Returns the plugin id.
	 */
	private static long uploadPlugin(String descriptor, byte[] jarFile) {
		
		SQLInsertClause pluginInsert = EntityConnectionManager.getConnection().createInsert(QPlugins.plugins);
		pluginInsert.set(QPlugins.plugins.isUnpacked, false);
		long pluginId = pluginInsert.executeWithKey(Long.class);
		
		SQLInsertClause bundleInsert = EntityConnectionManager.getConnection().createInsert(QPluginBundles.pluginBundles);
		bundleInsert.set(QPluginBundles.pluginBundles.pluginId, pluginId);
		bundleInsert.set(QPluginBundles.pluginBundles.descriptor, descriptor);
		bundleInsert.set(QPluginBundles.pluginBundles.jarfile, jarFile);
		bundleInsert.execute();
		
		InternalPluginUtil.generateDeclaredExtensionPointsAndExtensionsForPlugin(pluginId);
		return pluginId;
	}
	
	/**
	 * Updates the user's plugins, currently to include only the specified plugin.
	 */
	private static void updateUsersPlugins(long pluginId) {
		long userId = 1;
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QUserPlugins.userPlugins);
		delete.where(QUserPlugins.userPlugins.userId.eq(userId)).execute();
		SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QUserPlugins.userPlugins);
		insert.set(QUserPlugins.userPlugins.userId, userId);
		insert.set(QUserPlugins.userPlugins.pluginId, pluginId);
		insert.execute();
		InternalPluginUtil.updateExtensionBindingsForUser(userId);
	}
	
}
