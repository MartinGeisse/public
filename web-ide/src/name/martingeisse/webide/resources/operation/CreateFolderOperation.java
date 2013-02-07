/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;

/**
 * This operation creates a folder in the workspace.
 */
public final class CreateFolderOperation extends AbstractCreateResourceOperation {

	/**
	 * Constructor.
	 * @param path the path of the folder
	 * @param createEnclosingFolders whether enclosing folders shall be
	 * created as required
	 */
	public CreateFolderOperation(final ResourcePath path, boolean createEnclosingFolders) {
		super(path, createEnclosingFolders);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.WorkspaceOperationContext)
	 */
	@Override
	protected void perform(final WorkspaceOperationContext context) {
		FetchResourceResult parentResource = createEnclosingFoldersIfNeeded(context);
		trace("will create folder now", getPath());
		long id = insert(parentResource.getId(), getPath().getLastSegment(), ResourceType.FOLDER, new byte[0]);
		WorkspaceCache.onCreate(id, parentResource.getId(), getPath());
		WorkspaceResourceDeltaUtil.generateDeltas("create folder", getPath());
	}

}
