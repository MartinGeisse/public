/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.pde;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.entity.QPluginBundles;
import name.martingeisse.webide.entity.QPlugins;
import name.martingeisse.webide.entity.QWorkspaceStagingPlugins;
import name.martingeisse.webide.plugin.InternalPluginUtil;
import name.martingeisse.webide.resources.MarkerMeaning;
import name.martingeisse.webide.resources.MarkerOrigin;
import name.martingeisse.webide.resources.RecursiveResourceOperation;
import name.martingeisse.webide.resources.ResourceHandle;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.build.BuilderResourceDelta;
import name.martingeisse.webide.resources.build.IBuilder;

import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * This façade is used by the builder thread to invoke the plugin builder.
 */
public class PluginBuilder implements IBuilder {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.build.IBuilder#incrementalBuild(long, name.martingeisse.common.javascript.analyze.JsonAnalyzer, java.util.Set)
	 */
	@Override
	public void incrementalBuild(long workspaceId, JsonAnalyzer descriptorAnalyzer, Set<BuilderResourceDelta> deltas) {
		clearStagingPlugins(workspaceId);
		ResourcePath descriptorFilePath = new ResourcePath(descriptorAnalyzer.analyzeMapElement("descriptorFilePath").expectString());
		ResourceHandle descriptorFile = new ResourceHandle(workspaceId, descriptorFilePath);
		ResourcePath binPath = new ResourcePath(descriptorAnalyzer.analyzeMapElement("binPath").expectString());
		ResourceHandle binFolder = new ResourceHandle(workspaceId, binPath);
		ResourcePath bundleFilePath = new ResourcePath(descriptorAnalyzer.analyzeMapElement("bundleFilePath").expectString());
		ResourceHandle bundleFile = new ResourceHandle(workspaceId, bundleFilePath);
		
		boolean mustBuild = false;
		for (BuilderResourceDelta delta : deltas) {
			ResourcePath deltaPath = delta.getPath();
			if (delta.isDeep()) {
				if (deltaPath.isPrefixOf(descriptorFilePath) || deltaPath.isPrefixOf(binPath)) {
					mustBuild = true;
					break;
				}
			}
			if (deltaPath.equals(descriptorFilePath, true)) {
				mustBuild = true;
				break;
			}
			if (binPath.isPrefixOf(deltaPath)) {
				mustBuild = true;
				break;
			}
		}
		if (mustBuild) {
			performBuild(workspaceId, descriptorFile, binFolder, bundleFile);
		}
	}

	/**
	 * 
	 */
	private static void clearStagingPlugins(long workspaceId) {
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QWorkspaceStagingPlugins.workspaceStagingPlugins);
		delete.where(QWorkspaceStagingPlugins.workspaceStagingPlugins.workspaceId.eq(workspaceId));
		delete.execute();
	}

	/**
	 * Builds a single plugin project.
	 */
	private static void performBuild(long workspaceId, final ResourceHandle descriptorFile, final ResourceHandle binFolder, final ResourceHandle bundleFile) {

		// clean previous build
		descriptorFile.deleteMarkers(MarkerOrigin.PDE);
		bundleFile.delete();

		// fetch the plugin descriptor, stop if not found or invalid
		final String pluginBundleDescriptorSourceCode = descriptorFile.readTextFile(false);
		if (pluginBundleDescriptorSourceCode == null) {
			return;
		}
		if (!validateDescriptor(descriptorFile, pluginBundleDescriptorSourceCode)) {
			return;
		}

		// build and install the plugin
		final byte[] jarFile = generateJarFile(binFolder, bundleFile);
		uploadPlugin(pluginBundleDescriptorSourceCode, jarFile, workspaceId);
		
	}

	/**
	 * Validates the specified plugin bundle descriptor.
	 * Returns true on success, false on failure.
	 */
	private static boolean validateDescriptor(final ResourceHandle descriptorFile, final String pluginBundleDescriptorSourceCode) {
		try {
			final JsonAnalyzer analyzer = JsonAnalyzer.parse(pluginBundleDescriptorSourceCode);
			final JsonAnalyzer extensionPoints = analyzer.analyzeMapElement("extension_points");
			if (!extensionPoints.isNull()) {
				extensionPoints.expectList();
			}
			final JsonAnalyzer extensions = analyzer.analyzeMapElement("extensions");
			if (!extensions.isNull()) {
				extensions.expectMap();
			}
			return true;
		} catch (final Exception e) {
			descriptorFile.createMarker(MarkerOrigin.PDE, MarkerMeaning.ERROR, 1L, 1L, e.toString());
			return false;
		}
	}

	/**
	 * Generates a JAR file from the compiled classes and also returns the
	 * contents of the file.
	 */
	private static byte[] generateJarFile(final ResourceHandle binFolder, final ResourceHandle bundleFile) {
		try {

			// start writing a JAR file to a byte array
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			final JarOutputStream jarOutputStream = new JarOutputStream(byteArrayOutputStream);

			// add manifest
			jarOutputStream.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			jarOutputStream.write("Manifest-Version: 1.0\n".getBytes(Charset.forName("utf-8")));

			// add class files
			new RecursiveResourceOperation() {
				@Override
				protected void handleFile(ResourceHandle resourceHandle) {
					try {
						final String zipEntryPath = resourceHandle.getPath().removeFirstSegments(binFolder.getPath().getSegmentCount(), false).toString();
						jarOutputStream.putNextEntry(new ZipEntry(zipEntryPath.toString()));
						resourceHandle.copyFileTo(jarOutputStream, true);
					} catch (final IOException e) {
						throw new RuntimeException(e);
					}
				}
			}.handle(binFolder);

			// finish the JAR file and write it to the workspace
			jarOutputStream.close();
			final byte[] contents = byteArrayOutputStream.toByteArray();
			bundleFile.writeFile(contents, true, true);
			return contents;

		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Uploads the JAR file build by the previous step and the plugin bundle descriptor
	 * as a single-bundle plugin into the plugins and plugin_bundles tables.
	 */
	private static void uploadPlugin(final String descriptor, final byte[] jarFile, final long workspaceId) {

		// plugin
		final SQLInsertClause pluginInsert = EntityConnectionManager.getConnection().createInsert(QPlugins.plugins);
		pluginInsert.set(QPlugins.plugins.isUnpacked, false);
		final long pluginId = pluginInsert.executeWithKey(Long.class);

		// plugin bundle
		final SQLInsertClause bundleInsert = EntityConnectionManager.getConnection().createInsert(QPluginBundles.pluginBundles);
		bundleInsert.set(QPluginBundles.pluginBundles.pluginId, pluginId);
		bundleInsert.set(QPluginBundles.pluginBundles.descriptor, descriptor);
		bundleInsert.set(QPluginBundles.pluginBundles.jarfile, jarFile);
		bundleInsert.execute();

		// extension points / extensions
		InternalPluginUtil.generateDeclaredExtensionPointsAndExtensionsForPlugin(pluginId);

		// (workspace - staging plugin) relation
		final SQLInsertClause workspaceStagingPluginsInsert = EntityConnectionManager.getConnection().createInsert(QWorkspaceStagingPlugins.workspaceStagingPlugins);
		workspaceStagingPluginsInsert.set(QWorkspaceStagingPlugins.workspaceStagingPlugins.workspaceId, workspaceId);
		workspaceStagingPluginsInsert.set(QWorkspaceStagingPlugins.workspaceStagingPlugins.pluginId, pluginId);
		workspaceStagingPluginsInsert.execute();

	}

}
