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
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.util.ArrayUtil;
import name.martingeisse.webide.entity.Markers;
import name.martingeisse.webide.entity.QMarkers;
import name.martingeisse.webide.entity.QWorkspaceResourceDeltas;
import name.martingeisse.webide.entity.QWorkspaceTasks;
import name.martingeisse.webide.entity.QWorkspaces;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * This class represents workspace resources, both potential and
 * existing ones. That is, an instance of this class can be
 * created for any resource that is in principle allowed to
 * exist in the workspace. Methods then allow to check if the
 * resource actually exists as well as to obtain its properties
 * (resource type, modification time etc.), to manipulate resources,
 * and to read and write file contents. In this sense, this
 * class closely resembles the {@link File} class, except that
 * it represents workspace resources and not file system files.
 * 
 * An instance of this class is determined either by a workspace
 * and an absolute resource path, or alternatively, a file system
 * file that lies within a workspace. Resource paths used
 * by resource handles do not have a trailing separator; it is
 * removed automatically.
 */
public final class ResourceHandle implements Serializable {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ResourceHandle.class);

	/**
	 * Text for META_ROOT.
	 */
	private static String META_ROOT_PATH = "/var/projects/ide/workspace";

	/**
	 * Text for META_ROOT with a trailing slash appended.
	 */
	private static String META_ROOT_PATH_SLASH = "/var/projects/ide/workspace/";

	/**
	 * The parent folder in the file system that contains the workspace roots.
	 */
	private static File META_ROOT = new File(META_ROOT_PATH);

	/**
	 * the workspaceId
	 */
	private final long workspaceId;

	/**
	 * the path
	 */
	private final ResourcePath path;

	/**
	 * the resource
	 */
	private final File resource;

	// ---------------------------------------------------------------------------------------------------------
	// constructors
	// ---------------------------------------------------------------------------------------------------------

	/**
	 * Constructor for the specified resource in the workspace
	 * that is associated with the constructor calling thread.
	 * 
	 * @param path the path of the resource
	 */
	public ResourceHandle(final ResourcePath path) {
		this(getCurrentWorkspaceId(), path);
	}

	/**
	 * Constructor for the specified resource in the
	 * specified workspace.
	 * 
	 * @param workspaceId the ID of the workspace that contains the resource
	 * represented by this handle
	 * @param path the path of the resource
	 */
	public ResourceHandle(final long workspaceId, final ResourcePath path) {
		if (!path.isLeadingSeparator()) {
			throw new IllegalArgumentException("cannot create a ResourceHandle from a relative path");
		}
		this.workspaceId = workspaceId;
		this.path = path;
		this.resource = new File(getWorkspaceRootFolder(workspaceId), path.withTrailingSeparator(false).toString());
	}

	private static long getCurrentWorkspaceId() {
		return 1;
	}

	private static File getWorkspaceRootFolder(final long workspaceId) {
		return new File(META_ROOT, Long.toString(workspaceId));
	}

	/**
	 * Constructor for the specified file. The file must lie within
	 * the workspace associated with the constructor calling thread
	 * to avoid accidentally using a file from a foreign workspace.
	 * 
	 * @param file the file for the workspace resource
	 */
	public ResourceHandle(final File file) {
		this(file, false);
	}

	/**
	 * Constructor for the specified file.
	 * 
	 * If allowForeignWorkspace is true then the file must lie within
	 * the workspace associated with the constructor calling thread
	 * to avoid accidentally using a file from a foreign workspace.
	 * 
	 * Otherwise, the file must lie in any workspace (instances of
	 * this class cannot represent non-workspace-resource files).
	 * 
	 * @param resource the resource to create a handle for
	 * @param allowForeignWorkspace whether resources from other
	 * workspaces than the current one are allowed.
	 */
	public ResourceHandle(final File resource, final boolean allowForeignWorkspace) {

		// parameter checks
		if (!resource.isAbsolute()) {
			throw new IllegalArgumentException("cannot create a ResourceHandle from a relative File object");
		}

		// make the path lies within the meta root
		String resourcePath = resource.getPath();
		if (!resourcePath.startsWith(META_ROOT_PATH_SLASH)) {
			throw new IllegalArgumentException("resource is not part of any workspace: " + resource);
		}
		resourcePath = resourcePath.substring(META_ROOT_PATH_SLASH.length());

		// obtain the workspace ID and workspace root
		final int slashIndex = resourcePath.indexOf('/');
		long workspaceId;
		if (slashIndex == -1) {
			workspaceId = -1;
		} else {
			try {
				workspaceId = Long.parseLong(resourcePath.substring(0, slashIndex));
			} catch (final NumberFormatException e) {
				workspaceId = -1;
			}
		}
		final File workspaceRoot = getWorkspaceRootFolder(workspaceId);
		if (workspaceId < 1 || !workspaceRoot.exists()) {
			throw new IllegalArgumentException("resource is not part of any workspace: " + resource);
		}

		// initialize fields
		this.workspaceId = workspaceId;
		this.path = new ResourcePath(resourcePath.substring(slashIndex)).withTrailingSeparator(false);
		this.resource = resource;

	}

	/**
	 * Internal constructor.
	 */
	private ResourceHandle(final long workspaceId, final ResourcePath path, final File resource) {
		this.workspaceId = workspaceId;
		this.path = path;
		this.resource = resource;
	}

	// ---------------------------------------------------------------------------------------------------------
	// basic methods
	// ---------------------------------------------------------------------------------------------------------

	/**
	 * Getter method for the workspaceId.
	 * @return the workspaceId
	 */
	public long getWorkspaceId() {
		return workspaceId;
	}

	/**
	 * Getter method for the path.
	 * @return the path
	 */
	public ResourcePath getPath() {
		return path;
	}

	/**
	 * Getter method for the name (the last segment of the path).
	 * Returns null for the workspace root.
	 * @return the name or null
	 */
	public String getName() {
		return (path.getSegmentCount() == 0 ? null : path.getLastSegment());
	}

	/**
	 * Getter method for the name extension.
	 * Returns null if no extension is present.
	 * @return the name extension
	 */
	public String getExtension() {
		return (path.getSegmentCount() == 0 ? null : path.getExtension());
	}

	/**
	 * Getter method for the resource.
	 * @return the resource
	 */
	public File getResource() {
		return resource;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object untypedOther) {
		if (untypedOther instanceof ResourceHandle) {
			final ResourceHandle other = (ResourceHandle)untypedOther;
			return (workspaceId == other.workspaceId && path.equals(other.path));
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(workspaceId).append(path).toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{ResourceHandle: WS-ID " + workspaceId + ", path: " + path + "}";
	}

	// ---------------------------------------------------------------------------------------------------------
	// resource manipulation methods
	// ---------------------------------------------------------------------------------------------------------

	/**
	 * Returns true if and only if this resource actually exists in the workspace.
	 * @return whether this resource exists
	 */
	public boolean exists() {
		return resource.exists();
	}

	/**
	 * Returns true if and only if this resource exists and is a regular file.
	 * @return whether this resource exists and is a file
	 */
	public boolean isFile() {
		return resource.isFile();
	}

	/**
	 * Returns true if and only if this resource exists and is a folder.
	 * @return whether this resource exists and is a folder
	 */
	public boolean isFolder() {
		return resource.isDirectory();
	}

	/**
	 * Returns the type of this resource.
	 * @return the type
	 */
	public ResourceType getType() {
		return isFile() ? ResourceType.FILE : isFolder() ? ResourceType.FOLDER : ResourceType.OTHER;
	}

	/**
	 * Returns a resource handle for the parent resource of the resource for this handle.
	 * 
	 * @return the parent handle
	 */
	public ResourceHandle getParent() {
		if (path.getSegmentCount() == 0) {
			return null;
		} else {
			return new ResourceHandle(workspaceId, path.removeLastSegment(false), resource.getParentFile());
		}
	}

	/**
	 * Returns a resource handle for a child resource of the resource for this handle.
	 * 
	 * @param name the name of the child resource
	 * @return the child handle
	 */
	public ResourceHandle getChild(final String name) {
		return new ResourceHandle(workspaceId, path.appendSegment(name, false), new File(resource, name));
	}

	/**
	 * Checks whether any child resources for this resource exist
	 * in the file system.
	 * @return true if child resources exist, false if not
	 */
	public boolean hasChildren() {
		return resource.list().length > 0;
	}

	/**
	 * Returns an array containing handles for all child resources that
	 * actually exist in the file system, or null if this resource is not a folder.
	 * 
	 * @return the child handles
	 */
	public ResourceHandle[] getChildrenArray() {
		final String[] names = resource.list();
		if (names == null) {
			return null;
		}
		final ResourceHandle[] children = new ResourceHandle[names.length];
		for (int i = 0; i < names.length; i++) {
			children[i] = getChild(names[i]);
		}
		return children;
	}

	/**
	 * Returns a list containing handles for all child resources that
	 * actually exist in the file system, or null if this resource is not a folder.
	 * 
	 * @return the child handles
	 */
	public List<ResourceHandle> getChildrenList() {
		final ResourceHandle[] childrenArray = getChildrenArray();
		if (childrenArray == null) {
			return null;
		}
		final List<ResourceHandle> result = new ArrayList<ResourceHandle>();
		for (final ResourceHandle child : childrenArray) {
			result.add(child);
		}
		return result;
	}

	/**
	 * Returns a resource handle for another resource with the specified path,
	 * using this resource as the origin for relative paths.
	 * 
	 * @param path the path of the resource
	 * @return the handle of the specified resource
	 */
	public ResourceHandle get(final ResourcePath path) {
		return new ResourceHandle(workspaceId, this.path.concat(path, true));
	}

	/**
	 * Returns either this resource (if it is a folder), or its parent folder.
	 * This is useful to locate a folder from a user-selected resource.
	 * 
	 * @return the folder
	 */
	public ResourceHandle getFolder() {
		if (isFolder()) {
			return this;
		}
		final ResourceHandle parent = getParent();
		if (parent == null) {
			throw new IllegalStateException("cannot locate folder for resource: " + path);
		}
		return parent.getFolder();
	}

	/**
	 * Creates a folder for this handle as well as all its enclosing folders, returning
	 * without error if the folder already exists.
	 */
	public void createMissingFolders() {

		// handle already-existing folders
		if (exists()) {
			if (!isFolder()) {
				throw new WorkspaceOperationException("resource exists but is not a folder: " + path);
			}
			return;
		}

		// handle the root path -- if it doesn't already exist, there is an error
		if (path.getSegmentCount() == 0) {
			throw new WorkspaceOperationException("workspace root doesn't exist in the file system");
		}

		// create the folder
		if (!resource.mkdir()) {
			throw new WorkspaceOperationException("could not create folder: " + path);
		}

		// create deltas for the modification
		generateDelta("auto-create enclosing folders", false);

	}

	/**
	 * Helper method that creates enclosing folders only if requested so by the caller.
	 * Otherwise it checks that the parent folder exists and is indeed a folder.
	 * Then, this method ensures that the specified resource does not yet exist,
	 * or if it exists, that it is a regular file and that the 'overwrite' flag is set.
	 */
	private void prepareCreate(final boolean createEnclosingFolders, final boolean overwrite) {

		// parameter checks
		debug("prepareCreate (autocreate = " + createEnclosingFolders + ")", path);
		if (path.getSegmentCount() == 0) {
			throw new IllegalArgumentException("cannot create a resource at the workspace root");
		}

		// create / check parent
		final ResourceHandle parent = getParent();
		if (createEnclosingFolders) {
			trace("will create enclosing folders if needed", parent.getPath());
			parent.createMissingFolders();
		} else if (parent.isFolder()) {
			trace("enclosing folder exists", parent.getPath());
		} else {
			throw new WorkspaceOperationException("parent folder does not exist: " + parent.getPath());
		}

		// check for collision
		if (exists()) {
			if (!(overwrite && isFile())) {
				throw new WorkspaceResourceCollisionException(path);
			}
		}

		trace("prepareCreate finished", path);
	}

	/**
	 * Creates a folder in the workspace. This method will fail if a file or folder with
	 * the same name already exists. See {@link #createMissingFolders()} for a way to
	 * create folders in a way that succeeds if the folder already exists.
	 * 
	 * @param createEnclosingFolders whether enclosing folders shall be created as required
	 */
	public void createFolder(final boolean createEnclosingFolders) {
		prepareCreate(createEnclosingFolders, false);
		if (!resource.mkdir()) {
			throw new WorkspaceOperationException("could not create folder: " + path);
		}
		trace("folder created", path);
		generateDelta("create folder", false);
		trace("delta created", path);
	}

	/**
	 * Creates a file in the workspace and fills it with UTF-8 encoded text.
	 * 
	 * This method tries to overwrite any existing file but will fail if the file
	 * cannot be created or overwritten, e.g. if a folder with that name exists.
	 * 
	 * @param contents the text contents (assuming UTF-8 encoding)
	 * @param createEnclosingFolders whether enclosing folders shall be created as required
	 * @param overwrite whether to overwrite an existing file if present (folders are never overwritten)
	 */
	public void writeFile(final String contents, final boolean createEnclosingFolders, final boolean overwrite) {
		writeFile(contents == null ? null : contents.getBytes(Charset.forName("utf-8")), createEnclosingFolders, overwrite);
	}

	/**
	 * Creates a file in the workspace and fills it with binary data.
	 * 
	 * This method tries to overwrite any existing file but will fail if the file
	 * cannot be created or overwritten, e.g. if a folder with that name exists.
	 * 
	 * @param contents the binary contents
	 * @param createEnclosingFolders whether enclosing folders shall be created as required
	 * @param overwrite whether to overwrite an existing file if present (folders are never overwritten)
	 */
	public void writeFile(final byte[] contents, final boolean createEnclosingFolders, final boolean overwrite) {
		prepareCreate(createEnclosingFolders, overwrite);
		try {
			FileUtils.writeByteArrayToFile(resource, contents);
			trace("file created", path);
			generateDelta("create file", false);
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
	 * @param contentSource the source of the file contents
	 * @param createEnclosingFolders whether enclosing folders shall be created as required
	 * @param overwrite whether to overwrite an existing file if present (folders are never overwritten)
	 */
	public void writeFile(final InputStream contentSource, final boolean createEnclosingFolders, final boolean overwrite) {
		prepareCreate(createEnclosingFolders, overwrite);
		try {
			FileUtils.copyInputStreamToFile(contentSource, resource);
			trace("file created", path);
			generateDelta("create file", false);
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
	 * @param contentSource the source of the file contents
	 * @param createEnclosingFolders whether enclosing folders shall be created as required
	 * @param overwrite whether to overwrite an existing file if present (folders are never overwritten)
	 */
	public void writeFile(final File contentSource, final boolean createEnclosingFolders, final boolean overwrite) {
		prepareCreate(createEnclosingFolders, overwrite);
		try {
			FileUtils.copyFile(contentSource, resource);
			trace("file created", path);
			generateDelta("create file", false);
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
	 * @param required whether the file is required
	 * @return the file contents, or null if not required and the file does not exist
	 */
	public byte[] readBinaryFile(final boolean required) {
		if (isFile()) {
			try {
				return FileUtils.readFileToByteArray(resource);
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
	 * decoded text. See {@link #readBinaryFile(boolean)}
	 * for details on how the file is read.
	 * 
	 * @param required whether the file is required
	 * @return the file contents, or null if not required and the file does not exist
	 */
	public String readTextFile(final boolean required) {
		final byte[] binaryContents = readBinaryFile(required);
		if (binaryContents == null) {
			return null;
		}
		return new String(binaryContents, Charset.forName("utf-8"));
	}

	/**
	 * Reads a file from the workspace and transfers its contents to the specified
	 * destination. See {@link #readBinaryFile(boolean)}
	 * for details on how the file is read. If the file is not required,
	 * then the return value of this method indicates success or failure.
	 * 
	 * @param destination the destination to transfer the file to
	 * @param required whether the file is required
	 * @return true on success, false if not required and the file did not exist
	 */
	public boolean copyFileTo(final OutputStream destination, final boolean required) {
		if (isFile()) {
			try {
				FileUtils.copyFile(resource, destination);
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
	 * Copies a file within the workspace. See {@link #readBinaryFile(boolean)}
	 * for details on how the file is read. If the file is not required,
	 * then the return value of this method indicates success or failure.
	 * 
	 * @param destinationPath the path to write the file to
	 * @param required whether the file is required
	 * @return true on success, false if not required and the file did not exist
	 */
	public boolean copyFileTo(final ResourcePath destinationPath, final boolean required) {
		if (isFile()) {
			final ResourceHandle destination = new ResourceHandle(workspaceId, destinationPath);
			try {
				FileUtils.copyFile(resource, destination.getResource());
				return true;
			} catch (final IOException e) {
				throw new WorkspaceOperationException("cannot copy file " + path + " to " + destinationPath, e);
			}
		} else if (required) {
			throw new WorkspaceResourceNotFoundException(path);
		} else {
			return false;
		}
	}

	/**
	 * Copies a file within the workspace or across workspaces.
	 * See {@link #readBinaryFile(boolean)} for details on how the file is read.
	 * If the file is not required, then the return value of this method
	 * indicates success or failure.
	 * 
	 * @param destination the resource handle that determines where the file is copied to
	 * @param required whether the file is required
	 * @return true on success, false if not required and the file did not exist
	 */
	public boolean copyFileTo(final ResourceHandle destination, final boolean required) {
		if (isFile()) {
			try {
				FileUtils.copyFile(resource, destination.getResource());
				return true;
			} catch (final IOException e) {
				throw new WorkspaceOperationException("cannot copy file " + path + " to " + destination, e);
			}
		} else if (required) {
			throw new WorkspaceResourceNotFoundException(path);
		} else {
			return false;
		}
	}

	/**
	 * Deletes this resource.
	 * 
	 * @return false if the resource did not exist, true if it existed and was deleted
	 * @throws WorkspaceOperationException if the resource exists and could not be deleted
	 */
	public boolean delete() throws WorkspaceOperationException {
		trace("Workspace.delete starting for workspace resource", path);
		if (!exists()) {
			logger.trace("resource did not exist");
			return false;
		}
		final boolean result = deleteInternal(resource);
		trace("Workspace.delete creating deltas for workspace resource", path);
		generateDelta("delete workspace resource", true);
		trace("Workspace.delete finished for workspace resource", path);
		return result;
	}

	/**
	 * Recursively deletes a file or folder, but does not generate deltas.
	 * 
	 * @param resource the resource to delete
	 * @return false if the resource did not exist, true if it existed and was deleted
	 * @throws WorkspaceOperationException if the resource exists and could not be deleted
	 */
	private static boolean deleteInternal(final File resource) {
		logger.trace("deleting file system resource recursively: " + resource);
		if (!resource.exists()) {
			logger.trace("resource did not exist");
			return false;
		}
		if (resource.isDirectory()) {
			logger.trace("deleting child resources of folder: " + resource);
			for (final File child : resource.listFiles()) {
				deleteInternal(child);
			}
			logger.trace("deleted child resources of folder: " + resource);
		}
		if (!resource.delete()) {
			throw new WorkspaceOperationException("could not delete file system resource: " + resource);
		}
		logger.trace("file system resource deleted: " + resource);
		return true;
	}

	// ---------------------------------------------------------------------------------------------------------
	// marker manipulation methods
	// ---------------------------------------------------------------------------------------------------------

	/**
	 * Creates a marker for this resource.
	 * 
	 * @param origin the origin of the marker
	 * @param meaning the meaning of the marker
	 * @param line the line of the marker's position
	 * @param column the column of the marker's position
	 * @param message the marker message
	 */
	public void createMarker(final MarkerOrigin origin, final MarkerMeaning meaning, final long line, final long column, final String message) {
		if (!exists()) {
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
	 * Deletes markers for this resource.
	 * 
	 * @param origin the origin of the markers to delete, or null to delete markers from all origins
	 */
	public void deleteMarkers(final MarkerOrigin origin) {
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.path.eq(path.toString()));
		if (origin != null) {
			delete.where(QMarkers.markers.origin.eq(origin.toString()));
		}
		trace("will delete resource markers now", path);
		delete.execute();
	}

	/**
	 * Deletes all markers for this resource as well as all descendant resources.
	 * 
	 * @param origin the origin, or null to delete markers from all origins
	 */
	public void deleteMarkersRecursively(final MarkerOrigin origin) {

		// delete markers for the specified resource
		deleteMarkers(origin);

		// delete markers for descendant resources
		final String pathPrefix = (path.toString() + '/');
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.path.startsWith(pathPrefix));
		if (origin != null) {
			delete.where(QMarkers.markers.origin.eq(origin.toString()));
		}
		logger.trace("will delete resource markers now using path prefix" + pathPrefix);
		delete.execute();

	}

	/**
	 * Fetches the markers for this resource.
	 * 
	 * @param meaningFilter the requested marker meanings, or null to request all markers
	 * @param limit the maximum number of markers to fetch, or a negative number for no limit
	 * @return the markers
	 */
	public List<FetchMarkerResult> fetchMarkers(final MarkerMeaning[] meaningFilter, final long limit) {

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
		query = query.where(QMarkers.markers.path.eq(path.toString()));
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

	// ---------------------------------------------------------------------------------------------------------
	// static batch marker manipulation methods
	// ---------------------------------------------------------------------------------------------------------

	/**
	 * Deletes the markers for multiple resources in the workspace.
	 * 
	 * @param workspaceId the ID of the workspace that contains the markers
	 * @param paths the paths of the resource
	 * @param origin the origin, or null to delete markers from all origins
	 */
	public static void deleteMarkers(final long workspaceId, final Collection<? extends ResourcePath> paths, final MarkerOrigin origin) {
		final List<String> pathStrings = new ArrayList<String>();
		for (final ResourcePath path : paths) {
			pathStrings.add(path.withTrailingSeparator(false).toString());
		}
		deleteMarkers(workspaceId, pathStrings, origin);
	}

	/**
	 * Deletes the markers for multiple resources in the workspace.
	 * 
	 * @param workspaceId the ID of the workspace that contains the markers
	 * @param paths the paths of the resource
	 * @param origin the origin, or null to delete markers from all origins
	 */
	public static void deleteMarkers(final long workspaceId, final ResourcePath[] paths, final MarkerOrigin origin) {
		final List<String> pathStrings = new ArrayList<String>();
		for (final ResourcePath path : paths) {
			pathStrings.add(path.withTrailingSeparator(false).toString());
		}
		deleteMarkers(workspaceId, pathStrings, origin);
	}

	/**
	 * Helper method for the above deleteMarkers() methods.
	 */
	private static void deleteMarkers(final long workspaceId, final List<String> paths, final MarkerOrigin origin) {
		logger.trace("will delete resource markers now" + StringUtils.join(paths, ", "));
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.path.in(paths));
		if (origin != null) {
			delete.where(QMarkers.markers.workspaceId.eq(workspaceId));
			delete.where(QMarkers.markers.origin.eq(origin.toString()));
		}
		delete.execute();
	}

	/**
	 * Fetches the markers for a all resources.
	 * 
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

	// ---------------------------------------------------------------------------------------------------------
	// logging and other helper methods
	// ---------------------------------------------------------------------------------------------------------

	/**
	 * Logs the specified path at DEBUG level (optimized).
	 * @param prefix the prefix to log
	 * @param path the path to log
	 */
	@SuppressWarnings("unused")
	private static final void debug(final String prefix, final ResourcePath path) {
		if (logger.isDebugEnabled()) {
			logger.debug(prefix + ": " + path);
		}
	}

	/**
	 * Logs the specified path at DEBUG level (optimized).
	 * @param prefix the prefix to log
	 * @param paths the paths to log
	 */
	@SuppressWarnings("unused")
	private static final void debug(final String prefix, final ResourcePath[] paths) {
		if (logger.isDebugEnabled()) {
			logger.debug(prefix + ": " + StringUtils.join(paths, ", "));
		}
	}

	/**
	 * Logs the specified path at TRACE level (optimized).
	 * @param prefix the prefix to log
	 * @param path the path to log
	 */
	@SuppressWarnings("unused")
	private static final void trace(final String prefix, final ResourcePath path) {
		if (logger.isTraceEnabled()) {
			logger.trace(prefix + ": " + path);
		}
	}

	/**
	 * Logs the specified path at TRACE level (optimized).
	 * @param prefix the prefix to log
	 * @param paths the paths to log
	 */
	@SuppressWarnings("unused")
	private static final void trace(final String prefix, final ResourcePath[] paths) {
		if (logger.isTraceEnabled()) {
			logger.trace(prefix + ": " + StringUtils.join(paths, ", "));
		}
	}

	/**
	 * Generates resource deltas for the specified resources as well as a delta consumption task
	 * and sets the workspace to "building" state.
	 * 
	 * Note: This class used to have a separate method that creates deltas without
	 * also creating a delta consumption task. This method was intended for
	 * the case that more deltas will follow, but is dangerous: At the time
	 * the "last" deltas arrive and the caller intends to add a consumption
	 * task, the set of "last" deltas might be empty and so no consumption
	 * task is added, despite there being deltas from previous calls! Instead,
	 * this class now still skips the task if no deltas are being added, but
	 * always adds a task if at least one delta is being added, even if more
	 * deltas will follow. The consumption task itself should merge with
	 * subsequent tasks.
	 * 
	 * @param callerForLogging the caller of this method (simple string used for logging)
	 * @param deep whether to generate "deep" deltas for modified folders that also
	 * affect folder contents
	 * @param resourceHandle the resource handle
	 */
	private void generateDelta(final String callerForLogging, final boolean deep) {
		SQLInsertClause insert;

		logger.trace(callerForLogging + ": creating resource delta for resource: " + this + " (deep: " + deep + ") ...");
		insert = EntityConnectionManager.getConnection().createInsert(QWorkspaceResourceDeltas.workspaceResourceDeltas);
		insert.set(QWorkspaceResourceDeltas.workspaceResourceDeltas.workspaceId, workspaceId);
		insert.set(QWorkspaceResourceDeltas.workspaceResourceDeltas.path, path.toString());
		insert.set(QWorkspaceResourceDeltas.workspaceResourceDeltas.isDeep, deep);
		insert.execute();
		logger.trace(callerForLogging + ": finished creating resource delta.");

		logger.trace(callerForLogging + ": creating resource delta consumption task ...");
		insert = EntityConnectionManager.getConnection().createInsert(QWorkspaceTasks.workspaceTasks);
		insert.set(QWorkspaceResourceDeltas.workspaceResourceDeltas.workspaceId, workspaceId);
		insert.set(QWorkspaceTasks.workspaceTasks.command, "name.martingeisse.webide.resources.consume_deltas");
		insert.execute();
		logger.trace(callerForLogging + ": finished creating resource delta consumption task, will set the workspace to 'building' state.");
		final SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QWorkspaces.workspaces);
		update.where(QWorkspaces.workspaces.id.eq(workspaceId));
		update.set(QWorkspaces.workspaces.isBuilding, true);
		update.execute();
		logger.trace(callerForLogging + ": finished setting the workspace to 'building' state.");

	}

}
