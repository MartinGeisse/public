/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;

import org.apache.log4j.Logger;

import com.mysema.query.QueryException;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * Base implementation for operations that create a resource.
 */
public abstract class AbstractCreateResourceOperation extends SingleResourceOperation {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AbstractCreateResourceOperation.class);
	
	/**
	 * the createEnclosingFolders
	 */
	private boolean createEnclosingFolders;

	/**
	 * Constructor.
	 * @param path the path of the resource to create
	 * @param createEnclosingFolders whether enclosing folders shall be
	 * created as required
	 */
	public AbstractCreateResourceOperation(final ResourcePath path, boolean createEnclosingFolders) {
		super(path);
		this.createEnclosingFolders = createEnclosingFolders;
	}

	/**
	 * Creates all enclosing folders if that was specified in the constructor.
	 * Otherwise just fetches the enclosing folder for the path.
	 * Returns the {@link WorkspaceResources} object for the parent container.
	 */
	protected final FetchResourceResult createEnclosingFoldersIfNeeded(final WorkspaceOperationContext context) {
		ResourcePath parentPath = getPath().removeLastSegment(false);
		debug("enclosing folder (autocreate = " + createEnclosingFolders + ")", parentPath);
		if (createEnclosingFolders) {
			return createFolders(context, parentPath);
		} else {
			FetchResourceResult enclosingFolder = context.fetchResource(parentPath);
			if (enclosingFolder == null) {
				throw new WorkspaceOperationException("parent folder of path " + getPath() + " does not exist");
			}
			if (enclosingFolder.getType().equals(ResourceType.FILE.name())) {
				throw new WorkspaceOperationException("parent resource of path " + getPath() + " is a file, not a folder");
			}
			trace("enclosing folder exists", parentPath);
			return enclosingFolder;
		}
	}
	
	/**
	 * Creates the specified folder and all enclosing folders recursively.
	 */
	private FetchResourceResult createFolders(final WorkspaceOperationContext context, ResourcePath path) {
		
		// check for workspace root paths, project paths
		if (path.getSegmentCount() < 2) {
			FetchResourceResult container = context.fetchResource(path);
			if (container == null) {
				throw new RuntimeException("container resource " + path + " does not exist and cannot be created automatically");
			}
			trace("enclosing container exists", path);
			return container;
		}
		
		// first create the parent recursively
		FetchResourceResult parent = createFolders(context, path.removeLastSegment(false));
		
		// check for an existing folder
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QWorkspaceResources.workspaceResources);
		query.where(QWorkspaceResources.workspaceResources.parentId.eq(parent.getId()));
		query.where(QWorkspaceResources.workspaceResources.name.eq(path.getLastSegment()));
		WorkspaceResources existing = query.singleResult(QWorkspaceResources.workspaceResources);
		if (existing != null) {
			if (existing.getType().equals(ResourceType.FILE.name())) {
				throw new RuntimeException("resource " + path + " is a file, not a folder");
			}
			return new FetchResourceResult(path, existing);
		}
		
		// create a new object to collect data and for the return value
		WorkspaceResources folder = new WorkspaceResources();
		folder.setParentId(parent.getId());
		folder.setType(ResourceType.FOLDER.name());
		folder.setName(path.getLastSegment());

		// persist the new folder in the database
		trace("will create enclosing folder now", path);
		SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QWorkspaceResources.workspaceResources);
		insert.set(QWorkspaceResources.workspaceResources.parentId, folder.getParentId());
		insert.set(QWorkspaceResources.workspaceResources.type, folder.getType());
		insert.set(QWorkspaceResources.workspaceResources.name, folder.getName());
		insert.set(QWorkspaceResources.workspaceResources.contents, new byte[0]);
		long id = insert.executeWithKey(Long.class);
		WorkspaceCache.onCreate(id, folder.getParentId(), path);
		
		// return the resource object
		folder.setId(id);
		return new FetchResourceResult(path, folder);
		
	}
	
	/**
	 * Inserts a resource record into the database and returns its id.
	 */
	protected final long insert(long parentId, String name, ResourceType type, byte[] contents) {
		final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QWorkspaceResources.workspaceResources);
		insert.set(QWorkspaceResources.workspaceResources.name, name);
		insert.set(QWorkspaceResources.workspaceResources.contents, contents);
		insert.set(QWorkspaceResources.workspaceResources.parentId, parentId);
		insert.set(QWorkspaceResources.workspaceResources.type, type.name());
		try {
			return insert.executeWithKey(Long.class);
		} catch (QueryException e) {
			if (e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException) {
				throw new WorkspaceResourceCollisionException(getPath());
			}
			if (e.getCause() instanceof com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException) {
				throw new WorkspaceResourceCollisionException(getPath());
			}
			throw e;
		}
	}

}
