/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.util.ArrayUtil;
import name.martingeisse.webide.entity.Markers;
import name.martingeisse.webide.entity.QMarkers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * Static access to workspace resources.
 */
public class Workspace {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(Workspace.class);

	/**
	 * the ROOT
	 */
	private static File ROOT = new File("/var/projects/ide/workspace");

	/**
	 * Prevent instantiation.
	 */
	private Workspace() {
	}

	/**
	 * Logs the specified path at DEBUG level (optimized).
	 * @param prefix the prefix to log
	 * @param path the path to log
	 */
	static final void debug(final String prefix, final ResourcePath path) {
		if (logger.isDebugEnabled()) {
			logger.debug(prefix + ": " + path);
		}
	}

	/**
	 * Logs the specified path at DEBUG level (optimized).
	 * @param prefix the prefix to log
	 * @param paths the paths to log
	 */
	static final void debug(final String prefix, final ResourcePath[] paths) {
		if (logger.isDebugEnabled()) {
			logger.debug(prefix + ": " + StringUtils.join(paths, ", "));
		}
	}

	/**
	 * Logs the specified path at TRACE level (optimized).
	 * @param prefix the prefix to log
	 * @param path the path to log
	 */
	static final void trace(final String prefix, final ResourcePath path) {
		if (logger.isTraceEnabled()) {
			logger.trace(prefix + ": " + path);
		}
	}

	/**
	 * Logs the specified path at TRACE level (optimized).
	 * @param prefix the prefix to log
	 * @param paths the paths to log
	 */
	static final void trace(final String prefix, final ResourcePath[] paths) {
		if (logger.isTraceEnabled()) {
			logger.trace(prefix + ": " + StringUtils.join(paths, ", "));
		}
	}

	/**
	 * Maps the specified resource path to a {@link File} object without
	 * performing any action on the workspace, and without checking whether
	 * the file or any of its parent folders actually exist.
	 * 
	 * @param path the path to map
	 * @return the file object
	 */
	public static File map(final ResourcePath path) {
		if (!path.isLeadingSeparator()) {
			throw new IllegalArgumentException("cannot map a relative path");
		}
		return new File(ROOT, path.withLeadingSeparator(false).toString());
	}

	/**
	 * Maps the specified resource to a {@link ResourcePath} without
	 * performing any action on the workspace, and without checking whether
	 * the file or any of its parent folders actually exist.
	 * 
	 * This method will throw an {@link IllegalArgumentException} if the
	 * specified resource is not part of the workspace.
	 * 
	 * @param resource the resource to map
	 * @return the resource path
	 */
	public static ResourcePath map(final File resource) {
		if (!resource.isAbsolute()) {
			throw new IllegalArgumentException("cannot map a relative resource");
		}
		if (resource.equals(ROOT)) {
			return ResourcePath.ROOT;
		}
		if (resource.getParent() == null) {
			throw new IllegalArgumentException("resource is not part of the workspace: " + resource);
		}
		final ResourcePath parentPath = map(resource.getParentFile());
		return parentPath.appendSegment(resource.getName(), false);
	}

	/**
	 * Creates the specified folder and all its enclosing folders, returning
	 * without error if the folder already exists.
	 * @param enclosingPath the path for the folder to create
	 * @return the {@link File} object for the folder
	 */
	public static File createEnclosingFolders(final ResourcePath enclosingPath) {
		if (!enclosingPath.isLeadingSeparator()) {
			throw new IllegalArgumentException("cannot create a folder using a relative path");
		}
		if (enclosingPath.getSegmentCount() == 0) {
			return ROOT;
		}
		final ResourcePath parentPath = enclosingPath.removeLastSegment(false);
		final File parent = createEnclosingFolders(parentPath);
		if (!parent.isDirectory()) {
			throw new WorkspaceOperationException("enclosing resource is not a folder: " + parentPath);
		}
		final File folder = new File(parent, enclosingPath.getLastSegment());
		if (!folder.mkdir()) {
			throw new WorkspaceOperationException("could not create folder: " + enclosingPath);
		}
		WorkspaceResourceDeltaUtil.generateDeltas("auto-create enclosing folders", false, enclosingPath);
		return folder;
	}

	/**
	 * Helper method that creates enclosing folders only if requested so by the caller.
	 * Otherwise it checks that the parent folder exists and is indeed a folder.
	 * Then, this method ensures that the specified resource does not yet exist.
	 * Returns the {@link File} object for the resource to create.
	 */
	private static File prepareCreateResource(final ResourcePath path, final boolean createEnclosingFolders) {

		// checks
		debug("prepareCreateResource (autocreate = " + createEnclosingFolders + ")", path);
		if (path.getSegmentCount() == 0) {
			throw new IllegalArgumentException("cannot create a resource at the workspace root");
		}
		if (!path.isLeadingSeparator()) {
			throw new IllegalArgumentException("cannot create/check enclosing folders using a relative path");
		}
		final ResourcePath parentPath = path.removeLastSegment(false);

		// create / check parent
		final File parent;
		if (createEnclosingFolders) {
			trace("will create enclosing folders if needed", parentPath);
			parent = createEnclosingFolders(parentPath);
		} else if (parentPath.getSegmentCount() == 0) {
			trace("enclosing folder is the workspace root", parentPath);
			parent = ROOT;
		} else {
			parent = map(parentPath);
			if (!parent.exists()) {
				throw new WorkspaceOperationException("parent resource does not exist: " + parentPath);
			}
			if (!parent.isDirectory()) {
				throw new WorkspaceOperationException("parent resource is not a folder: " + parentPath);
			}
			trace("enclosing folder exists", parentPath);
		}

		// check for collision
		final File resource = new File(parent, path.getLastSegment());
		if (resource.exists()) {
			throw new WorkspaceOperationException("a workspace resource already exists at this path: " + path);
		}

		trace("prepareCreateResource finished", path);
		return resource;
	}

	/**
	 * Creates a folder in the workspace. This method will fail if a file or folder with
	 * the same name already exists. See {@link #createEnclosingFolders(ResourcePath)}
	 * for a way to create folders in a way that succeeds if the folder already exists.
	 * 
	 * @param path the path of the folder
	 * @param createEnclosingFolders whether enclosing folders shall be created as required
	 */
	public static void createNewFolder(final ResourcePath path, final boolean createEnclosingFolders) {
		final File folder = prepareCreateResource(path, createEnclosingFolders);
		if (!folder.mkdir()) {
			throw new WorkspaceOperationException("could not create folder: " + path);
		}
		trace("file created", path);
		WorkspaceResourceDeltaUtil.generateDeltas("create file", false, path);
		trace("delta created", path);
	}

	/**
	 * Creates a file in the workspace and fills it with UTF-8 encoded text.
	 * 
	 * This method tries to overwrite any existing file but will fail if the file
	 * cannot be created or overwritten, e.g. if a folder with that name exists.
	 * 
	 * @param path the path of the file
	 * @param contents the text contents (assuming UTF-8 encoding)
	 * @param createEnclosingFolders whether enclosing folders shall be created as required
	 */
	public static void createFile(final ResourcePath path, final String contents, final boolean createEnclosingFolders) {
		createFile(path, contents == null ? null : contents.getBytes(Charset.forName("utf-8")), createEnclosingFolders);
	}

	/**
	 * Creates a file in the workspace and fills it with binary data.
	 * 
	 * This method tries to overwrite any existing file but will fail if the file
	 * cannot be created or overwritten, e.g. if a folder with that name exists.
	 * 
	 * @param path the path of the file
	 * @param contents the binary contents
	 * @param createEnclosingFolders whether enclosing folders shall be created as required
	 */
	public static void createFile(final ResourcePath path, final byte[] contents, final boolean createEnclosingFolders) {
		final File file = prepareCreateResource(path, createEnclosingFolders);
		try {
			FileUtils.writeByteArrayToFile(file, contents);
			trace("file created", path);
			WorkspaceResourceDeltaUtil.generateDeltas("create file", false, path);
			trace("delta created", path);
		} catch (final IOException e) {
			throw new WorkspaceOperationException("could not create file: " + path, e);
		}
	}

	/**
	 * Creates a file in the workspace and fills it with data from the specified source.
	 * 
	 * This method tries to overwrite any existing file but will fail if the file
	 * cannot be created or overwritten, e.g. if a folder with that name exists.
	 * 
	 * @param path the path of the file
	 * @param contentSource the source of the file contents
	 * @param createEnclosingFolders whether enclosing folders shall be created as required
	 */
	public static void createFile(final ResourcePath path, final InputStream contentSource, final boolean createEnclosingFolders) {
		final File file = prepareCreateResource(path, createEnclosingFolders);
		try {
			FileUtils.copyInputStreamToFile(contentSource, file);
			trace("file created", path);
			WorkspaceResourceDeltaUtil.generateDeltas("create file", false, path);
			trace("delta created", path);
		} catch (final IOException e) {
			throw new WorkspaceOperationException("could not create file: " + path, e);
		}
	}

	/**
	 * Creates a file in the workspace and fills it with data from the specified source.
	 * 
	 * This method tries to overwrite any existing file but will fail if the file
	 * cannot be created or overwritten, e.g. if a folder with that name exists.
	 * 
	 * @param path the path of the file
	 * @param contentSource the source of the file contents
	 * @param createEnclosingFolders whether enclosing folders shall be created as required
	 */
	public static void createFile(final ResourcePath path, final File contentSource, final boolean createEnclosingFolders) {
		final File file = prepareCreateResource(path, createEnclosingFolders);
		try {
			FileUtils.copyFile(contentSource, file);
			trace("file created", path);
			WorkspaceResourceDeltaUtil.generateDeltas("create file", false, path);
			trace("delta created", path);
		} catch (final IOException e) {
			throw new WorkspaceOperationException("could not create file: " + path, e);
		}
	}

	/**
	 * Reads a file from the workspace and returns its contents in binary form.
	 * If the file cannot be read, then the 'required' flag determines the effect:
	 * If required, then a {@link WorkspaceOperationException} is thrown, otherwise
	 * this method returns null. If the path denotes a folder then the same action
	 * is taken as if the file didn't exist.
	 * 
	 * @param path the path of the file to read
	 * @param required whether the file is required
	 * @return the file contents, or null if not required and the file does not exist
	 */
	public static byte[] readBinaryFile(final ResourcePath path, final boolean required) {
		final File file = map(path);
		if (file.isFile()) {
			try {
				return FileUtils.readFileToByteArray(file);
			} catch (final IOException e) {
				throw new WorkspaceOperationException("cannot read file: " + path, e);
			}
		} else if (required) {
			throw new WorkspaceResourceNotFoundException(path);
		} else {
			return null;
		}
	}

	/**
	 * Reads a file from the workspace and returns its contents as UTF-8
	 * decoded text. See {@link #readBinaryFile(ResourcePath, boolean)}
	 * for details on how the file is read.
	 * 
	 * @param path the path of the file to read
	 * @param required whether the file is required
	 * @return the file contents, or null if not required and the file does not exist
	 */
	public static String readTextFile(final ResourcePath path, final boolean required) {
		final byte[] binaryContents = readBinaryFile(path, required);
		if (binaryContents == null) {
			return null;
		}
		return new String(binaryContents, Charset.forName("utf-8"));
	}

	/**
	 * Reads a file from the workspace and transfers its contents to the specified
	 * destination. See {@link #readBinaryFile(ResourcePath, boolean)}
	 * for details on how the file is read. If the file is not required,
	 * then the return value of this method indicates success or failure.
	 * 
	 * @param path the path of the file to read
	 * @param destination the destination to transfer the file to
	 * @param required whether the file is required
	 * @return true on success, false if not required and the file did not exist
	 */
	public static boolean transferFile(final ResourcePath path, final OutputStream destination, final boolean required) {
		final File file = map(path);
		if (file.isFile()) {
			try {
				FileUtils.copyFile(file, destination);
				return true;
			} catch (final IOException e) {
				throw new WorkspaceOperationException("cannot transfer file: " + path, e);
			}
		} else if (required) {
			throw new WorkspaceResourceNotFoundException(path);
		} else {
			return false;
		}
	}

	/**
	 * Copies a file within the workspace. See {@link #readBinaryFile(ResourcePath, boolean)}
	 * for details on how the file is read. If the file is not required,
	 * then the return value of this method indicates success or failure.
	 * 
	 * @param sourcePath the path to read the file from
	 * @param destinationPath the path to write the file to
	 * @param required whether the file is required
	 * @return true on success, false if not required and the file did not exist
	 */
	public static boolean copyFile(final ResourcePath sourcePath, final ResourcePath destinationPath, final boolean required) {
		final File sourceFile = map(sourcePath);
		if (sourceFile.isFile()) {
			final File destinationFile = map(destinationPath);
			try {
				FileUtils.copyFile(sourceFile, destinationFile);
				return true;
			} catch (final IOException e) {
				throw new WorkspaceOperationException("cannot copy file " + sourcePath + " to " + destinationPath, e);
			}
		} else if (required) {
			throw new WorkspaceResourceNotFoundException(sourcePath);
		} else {
			return false;
		}
	}
	
	/**
	 * Deletes the specified resource.
	 * @param path the resource path
	 */
	public static void delete(final ResourcePath path) {
		final File resource = map(path);
		if (!resource.exists()) {
			throw new WorkspaceOperationException("resource does not exist: " + path);
		}
		delete(resource);
		trace("finished deleting resource", path);
		WorkspaceResourceDeltaUtil.generateDeltas("delete resource", true, path);
		trace("delta created", path);
	}

	/**
	 * Deletes the specified resources.
	 * @param paths the resource paths
	 */
	public static void delete(final Collection<? extends ResourcePath> paths) {
		for (final ResourcePath path : paths) {
			delete(path);
		}
	}

	/**
	 * Deletes the specified resources.
	 * @param paths the resource paths
	 */
	public static void delete(final ResourcePath[] paths) {
		for (final ResourcePath path : paths) {
			delete(path);
		}
	}

	/**
	 * Recursively deletes a file or folder.
	 * @param resource the resource to delete
	 */
	public static void delete(final File resource) {
		logger.trace("deleting resource recursively: " + resource);
		if (resource.isDirectory()) {
			for (final File child : resource.listFiles()) {
				delete(child);
			}
		}
		resource.delete();
	}

	/**
	 * Creates a resource marker.
	 * @param path the path of the resource
	 * @param origin the origin of the marker
	 * @param meaning the meaning of the marker
	 * @param line the line of the marker's position
	 * @param column the column of the marker's position
	 * @param message the marker message
	 */
	public static void createMarker(ResourcePath path, final MarkerOrigin origin, final MarkerMeaning meaning, final long line, final long column, final String message) {
		final long workspaceId = 1;
		path = path.withTrailingSeparator(false);
		if (!map(path).exists()) {
			throw new WorkspaceResourceNotFoundException(path);
		}
		trace("will create resource marker now", path);
		final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QMarkers.markers);
		insert.set(QMarkers.markers.workspaceId, workspaceId);
		insert.set(QMarkers.markers.path, path.toString());
		insert.set(QMarkers.markers.origin, origin.toString());
		insert.set(QMarkers.markers.meaning, meaning.toString());
		insert.set(QMarkers.markers.line, line);
		insert.set(QMarkers.markers.column, column);
		insert.set(QMarkers.markers.message, message);
		insert.execute();
	}

	/**
	 * Deletes the markers for a single resource in the workspace.
	 * 
	 * @param path the path of the resource
	 * @param origin the origin, or null to delete markers from all origins
	 */
	public static void deleteMarkers(ResourcePath path, final MarkerOrigin origin) {
		path = path.withTrailingSeparator(false);
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.path.eq(path.toString()));
		if (origin != null) {
			delete.where(QMarkers.markers.origin.eq(origin.toString()));
		}
		trace("will delete resource markers now", path);
		delete.execute();
	}

	/**
	 * Deletes the markers for multiple resources in the workspace.
	 * 
	 * @param paths the paths of the resource
	 * @param origin the origin, or null to delete markers from all origins
	 */
	public static void deleteMarkers(final Collection<? extends ResourcePath> paths, final MarkerOrigin origin) {
		final List<String> pathStrings = new ArrayList<String>();
		for (final ResourcePath path : paths) {
			pathStrings.add(path.withTrailingSeparator(false).toString());
		}
		deleteMarkers(pathStrings, origin);
	}

	/**
	 * Deletes the markers for multiple resources in the workspace.
	 * 
	 * @param paths the paths of the resource
	 * @param origin the origin, or null to delete markers from all origins
	 */
	public static void deleteMarkers(final ResourcePath[] paths, final MarkerOrigin origin) {
		final List<String> pathStrings = new ArrayList<String>();
		for (final ResourcePath path : paths) {
			pathStrings.add(path.withTrailingSeparator(false).toString());
		}
		deleteMarkers(pathStrings, origin);
	}

	/**
	 * Helper method for the above deleteMarkers() methods.
	 */
	private static void deleteMarkers(final List<String> paths, final MarkerOrigin origin) {
		logger.trace("will delete resource markers now" + StringUtils.join(paths, ", "));
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.path.in(paths));
		if (origin != null) {
			delete.where(QMarkers.markers.origin.eq(origin.toString()));
		}
		delete.execute();
	}

	/**
	 * Deletes all markers for the resource with the specified path as well as all descendant resources.
	 * 
	 * @param path the path of the resource
	 * @param origin the origin, or null to delete markers from all origins
	 */
	public static void deleteMarkersRecursively(ResourcePath path, final MarkerOrigin origin) {

		// delete markers for the specified resource
		deleteMarkers(path, origin);

		// delete markers for descendant resources
		path = path.withTrailingSeparator(true);
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.path.startsWith(path.toString()));
		if (origin != null) {
			delete.where(QMarkers.markers.origin.eq(origin.toString()));
		}
		trace("will delete resource markers now using path prefix", path);
		delete.execute();

	}

	/**
	 * Fetches the markers for a all resources.
	 * @param meaningFilter the requested marker meanings, or null to request all markers
	 * @param limit the maximum number of markers to fetch, or a negative number for no limit
	 * @return the markers
	 */
	public static List<FetchMarkerResult> fetchAllMarkers(final MarkerMeaning[] meaningFilter, final long limit) {

		// if no meaning is accepted, the result must be empty
		if (meaningFilter != null && meaningFilter.length == 0) {
			logger.debug("fetchAllMarkers: no accepted meaning -> no markers");
			return new ArrayList<FetchMarkerResult>();
		}

		// fetch markers
		if (logger.isTraceEnabled()) {
			logger.trace("fetching markers, meaning: [" + (meaningFilter == null ? "*" : StringUtils.join(meaningFilter, ", ")) + "] ...");
		}
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query = query.from(QMarkers.markers);
		if (meaningFilter != null) {
			query = query.where(QMarkers.markers.meaning.in(ArrayUtil.toStringArray(meaningFilter)));
		}
		query.limit(limit);
		final List<Markers> rawMarkers = query.list(QMarkers.markers);
		logger.trace("markers fetched.");

		// build FetchMarkerResult objects
		final ArrayList<FetchMarkerResult> result = new ArrayList<FetchMarkerResult>();
		for (final Markers marker : rawMarkers) {
			result.add(new FetchMarkerResult(marker));
		}
		return result;

	}

	/**
	 * Fetches the markers for a single resource.
	 * @param path the path of the resource
	 * @param meaningFilter the requested marker meanings, or null to request all markers
	 * @param limit the maximum number of markers to fetch, or a negative number for no limit
	 * @return the markers
	 */
	public static List<FetchMarkerResult> fetchSingleResourceMarkers(final ResourcePath path, final MarkerMeaning[] meaningFilter, final long limit) {

		// if no meaning is accepted, the result must be empty
		if (meaningFilter != null && meaningFilter.length == 0) {
			logger.debug("FetchSingleResourceMarkersOperation: no accepted meaning -> no markers");
			return new ArrayList<FetchMarkerResult>();
		}

		// fetch markers
		if (logger.isTraceEnabled()) {
			logger.trace("fetching markers, meaning: [" + (meaningFilter == null ? "*" : StringUtils.join(meaningFilter, ", ")) + "] ...");
		}
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query = query.from(QMarkers.markers);
		query = query.where(QMarkers.markers.path.eq(path.withTrailingSeparator(false).toString()));
		if (meaningFilter != null) {
			query = query.where(QMarkers.markers.meaning.in(ArrayUtil.toStringArray(meaningFilter)));
		}
		query.limit(limit);
		final List<Markers> rawMarkers = query.list(QMarkers.markers);
		logger.trace("markers fetched.");

		// build FetchMarkerResult objects
		final ArrayList<FetchMarkerResult> result = new ArrayList<FetchMarkerResult>();
		for (final Markers marker : rawMarkers) {
			result.add(new FetchMarkerResult(marker));
		}
		return result;

	}

}
