/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.pde;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.Workspace;
import name.martingeisse.webide.resources.build.BuilderResourceDelta;
import name.martingeisse.webide.resources.build.IBuilder;

import org.apache.commons.io.FileUtils;

import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * This façade is used by the builder thread to invoke the plugin builder.
 */
public class PluginBuilder implements IBuilder {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.build.IBuilder#incrementalBuild(name.martingeisse.common.javascript.analyze.JsonAnalyzer, java.util.Set)
	 */
	@Override
	public void incrementalBuild(final JsonAnalyzer descriptorAnalyzer, final Set<BuilderResourceDelta> deltas) {
		clearStagingPlugins();
		ResourcePath descriptorFilePath = new ResourcePath(descriptorAnalyzer.analyzeMapElement("descriptorFilePath").expectString()); 
		ResourcePath binPath = new ResourcePath(descriptorAnalyzer.analyzeMapElement("binPath").expectString()); 
		ResourcePath bundleFilePath = new ResourcePath(descriptorAnalyzer.analyzeMapElement("bundleFilePath").expectString());
		
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
			performBuild(descriptorFilePath, binPath, bundleFilePath);
		}
	}

	/**
	 * 
	 */
	private static void clearStagingPlugins() {
		final long workspaceId = 1; // TODO
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QWorkspaceStagingPlugins.workspaceStagingPlugins);
		delete.where(QWorkspaceStagingPlugins.workspaceStagingPlugins.workspaceId.eq(workspaceId));
		delete.execute();
	}

	/**
	 * Builds a single plugin project.
	 */
	private static void performBuild(final ResourcePath descriptorFilePath, final ResourcePath binPath, final ResourcePath bundleFilePath) {

		// TODO
		final long workspaceId = 1;

		// clean previous build
		Workspace.deleteMarkers(descriptorFilePath, MarkerOrigin.PDE);
		Workspace.delete(bundleFilePath);

		// fetch the plugin descriptor, stop if not found or invalid
		final String pluginBundleDescriptorSourceCode = Workspace.readTextFile(descriptorFilePath, false);
		if (pluginBundleDescriptorSourceCode == null) {
			return;
		}
		if (!validateDescriptor(descriptorFilePath, pluginBundleDescriptorSourceCode)) {
			return;
		}

		// build and install the plugin
		final byte[] jarFile = generateJarFile(binPath, bundleFilePath);
		uploadPlugin(pluginBundleDescriptorSourceCode, jarFile, workspaceId);
		
	}

	/**
	 * Validates the specified plugin bundle descriptor.
	 * Returns true on success, false on failure.
	 */
	private static boolean validateDescriptor(final ResourcePath descriptorFilePath, final String pluginBundleDescriptorSourceCode) {
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
			Workspace.createMarker(descriptorFilePath, MarkerOrigin.PDE, MarkerMeaning.ERROR, 1L, 1L, e.toString());
			return false;
		}
	}

	/**
	 * Generates a JAR file from the compiled classes and also returns the
	 * contents of the file.
	 */
	private static byte[] generateJarFile(final ResourcePath binPath, final ResourcePath jarPath) {
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
				protected void handleFile(ResourcePath path, File file) {
					try {
						final String zipEntryPath = path.removeFirstSegments(binPath.getSegmentCount(), false).toString();
						jarOutputStream.putNextEntry(new ZipEntry(zipEntryPath.toString()));
						FileUtils.copyFile(file, jarOutputStream);
					} catch (final IOException e) {
						throw new RuntimeException(e);
					}
				}
			}.handle(binPath);

			// finish the JAR file and write it to the workspace
			jarOutputStream.close();
			final byte[] contents = byteArrayOutputStream.toByteArray();
			Workspace.createFile(jarPath, contents, true);
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
