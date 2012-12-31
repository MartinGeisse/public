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

import name.martingeisse.webide.resources.WorkspaceUtil;

/**
 * This façade is used by the builder thread to invoke the plugin builder.
 */
public class PluginBuilderFacade {

	/**
	 * This method is invoked by the builder thread to perform a plugin build.
	 */
	public static void performBuild() {
		WorkspaceUtil.delete("plugin.jar");
		byte[] pluginBundleDescriptorSourceCode = WorkspaceUtil.getContents("plugin.json");
		if (pluginBundleDescriptorSourceCode == null) {
			return;
		}
		if (!validateDescriptor(pluginBundleDescriptorSourceCode)) {
			return;
		}
		generateJarFile();
		long pluginId = uploadPlugin();
		updateUsersPlugins(pluginId);
	}
	
	/**
	 * Validates the specified plugin bundle descriptor.
	 * Returns true on success, false on failure.
	 */
	private static boolean validateDescriptor(byte[] pluginBundleDescriptorSourceCode) {
		// TODO
		return true;
	}

	/**
	 * Generates a JAR file from the compiled classes.
	 */
	private static void generateJarFile() {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			JarOutputStream jarOutputStream = new JarOutputStream(byteArrayOutputStream);
			jarOutputStream.putNextEntry(new ZipEntry("test.txt"));
			jarOutputStream.write("test foo bar".getBytes(Charset.forName("utf-8")));
			jarOutputStream.close();
			WorkspaceUtil.createFile("plugin.jar", byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Uploads the JAR file build by the previous step and the plugin bundle descriptor
	 * as a single-bundle plugin into the plugins and plugin_bundles tables.
	 * Returns the plugin id.
	 */
	private static long uploadPlugin() {
		// TODO
		return 0;
	}
	
	/**
	 * Updates the user's plugins, currently to include only the specified plugin.
	 */
	private static void updateUsersPlugins(long pluginId) {
		// TODO
	}
	
}
