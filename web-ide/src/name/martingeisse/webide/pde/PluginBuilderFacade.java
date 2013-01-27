/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.pde;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
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
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;
import name.martingeisse.webide.resources.operation.CreateFileOperation;
import name.martingeisse.webide.resources.operation.CreateResourceMarkerOperation;
import name.martingeisse.webide.resources.operation.DeleteResourceOperation;
import name.martingeisse.webide.resources.operation.DeleteSingleResourceMarkersOperation;
import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.FetchSingleResourceOperation;
import name.martingeisse.webide.resources.operation.ListResourcesOperation;
import name.martingeisse.webide.resources.operation.RecursiveResourceOperation;

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
		clearStagingPlugins();
		final ListResourcesOperation list = new ListResourcesOperation(new ResourcePath("/"));
		list.run();
		for (final FetchResourceResult fetchResult : list.getChildren()) {
			performBuild(fetchResult.getPath());
		}
	}

	/**
	 * 
	 */
	private static void clearStagingPlugins() {
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QWorkspaceStagingPlugins.workspaceStagingPlugins);
		delete.where(QWorkspaceStagingPlugins.workspaceStagingPlugins.workspaceResourceId.eq(findWorkspaceRoot()));
		delete.execute();
	}
	
	/**
	 * Builds a single plugin project.
	 */
	private static void performBuild(final ResourcePath basePath) {

		// prepare
		final ResourcePath descriptorFilePath = basePath.appendSegment("plugin.json", false);
		final ResourcePath bundleFilePath = basePath.appendSegment("plugin.jar", false);
		final ResourcePath binPath = basePath.appendSegment("bin", false);

		// clean previous build
		new DeleteSingleResourceMarkersOperation(descriptorFilePath, MarkerOrigin.PDE).run();
		new DeleteResourceOperation(bundleFilePath).run();

		// fetch the plugin descriptor, stop if not found
		final FetchSingleResourceOperation fetchDescriptorFileOperation = new FetchSingleResourceOperation(descriptorFilePath);
		fetchDescriptorFileOperation.run();
		final FetchResourceResult fetchDescriptorResult = fetchDescriptorFileOperation.getResult();
		if (fetchDescriptorResult == null || fetchDescriptorResult.getType() != ResourceType.FILE) {
			return;
		}

		// validate the descriptor
		final String pluginBundleDescriptorSourceCode = new String(fetchDescriptorResult.getContents(), Charset.forName("utf-8"));
		if (!validateDescriptor(descriptorFilePath, pluginBundleDescriptorSourceCode)) {
			return;
		}

		// build and install the plugin
		final byte[] jarFile = generateJarFile(binPath, bundleFilePath);
		uploadPlugin(pluginBundleDescriptorSourceCode, jarFile, findWorkspaceRoot());

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
			final CreateResourceMarkerOperation operation = new CreateResourceMarkerOperation(descriptorFilePath, MarkerOrigin.PDE, MarkerMeaning.ERROR, 1L, 1L, e.toString());
			operation.run();
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
			new RecursiveResourceOperation(binPath) {
				@Override
				protected void onLevelFetched(final List<FetchResourceResult> fetchResults) {
					try {
						for (final FetchResourceResult fetchResult : fetchResults) {
							String zipEntryPath = fetchResult.getPath().removeFirstSegments(binPath.getSegmentCount(), false).toString();
							if (fetchResult.getType() == ResourceType.FILE) {
								jarOutputStream.putNextEntry(new ZipEntry(zipEntryPath.toString()));
								jarOutputStream.write(fetchResult.getContents());
							}
						}
					} catch (final IOException e) {
						throw new RuntimeException(e);
					}
				}
			}.run();
			
			// finish the JAR file and write it to the workspace
			jarOutputStream.close();
			final byte[] contents = byteArrayOutputStream.toByteArray();
			new CreateFileOperation(jarPath, contents, true).run();
			return contents;
			
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Finds the workspace resource id of the workspace root.
	 */
	private static long findWorkspaceRoot() {
		FetchSingleResourceOperation operation = new FetchSingleResourceOperation(ResourcePath.ROOT);
		operation.run();
		return operation.getResult().getId();
	}
	
	/**
	 * Uploads the JAR file build by the previous step and the plugin bundle descriptor
	 * as a single-bundle plugin into the plugins and plugin_bundles tables.
	 */
	private static void uploadPlugin(final String descriptor, final byte[] jarFile, long workspaceRootId) {

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
		workspaceStagingPluginsInsert.set(QWorkspaceStagingPlugins.workspaceStagingPlugins.workspaceResourceId, workspaceRootId);
		workspaceStagingPluginsInsert.set(QWorkspaceStagingPlugins.workspaceStagingPlugins.pluginId, pluginId);
		workspaceStagingPluginsInsert.execute();
		
	}

}
