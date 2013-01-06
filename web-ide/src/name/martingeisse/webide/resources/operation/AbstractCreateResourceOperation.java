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

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * Base implementation for operations that create a resource.
 */
public abstract class AbstractCreateResourceOperation extends SingleResourceOperation {

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
	protected final WorkspaceResources createEnclosingFoldersIfNeeded(final IWorkspaceOperationContext context) {
		ResourcePath parentPath = getPath().removeLastSegment(false);
		if (createEnclosingFolders) {
			return createFolders(context, parentPath);
		} else {
			WorkspaceResources enclosingFolder = fetchResource(context, parentPath);
			if (enclosingFolder == null) {
				throw new WorkspaceOperationException("parent folder of path " + getPath() + " does not exist");
			}
			if (enclosingFolder.getType().equals(ResourceType.FILE.name())) {
				throw new WorkspaceOperationException("parent resource of path " + getPath() + " is a file, not a folder");
			}
			return enclosingFolder;
		}
	}
	
	/**
	 * Creates the specified folder and all enclosing folders recursively.
	 */
	private WorkspaceResources createFolders(final IWorkspaceOperationContext context, ResourcePath path) {
		
		// check for workspace root paths, project paths
		if (path.getSegmentCount() < 2) {
			WorkspaceResources container = fetchResource(context, path);
			if (container == null) {
				throw new RuntimeException("container resource " + path + " does not exist and cannot be created automatically");
			}
			return container;
		}
		
		// first create the parent recursively
		WorkspaceResources parent = createFolders(context, path.removeLastSegment(false));
		
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
			return existing;
		}
		
		// create a new object to collect data and for the return value
		WorkspaceResources folder = new WorkspaceResources();
		folder.setParentId(parent.getId());
		folder.setType(ResourceType.FOLDER.name());
		folder.setName(path.getLastSegment());

		// persist the new folder in the database
		SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QWorkspaceResources.workspaceResources);
		insert.set(QWorkspaceResources.workspaceResources.parentId, folder.getParentId());
		insert.set(QWorkspaceResources.workspaceResources.type, folder.getType());
		insert.set(QWorkspaceResources.workspaceResources.name, folder.getName());
		long id = insert.executeWithKey(Long.class);
		
		// return the resource object
		folder.setId(id);
		return folder;
		
	}

}
