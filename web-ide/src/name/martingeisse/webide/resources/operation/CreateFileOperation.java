/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.nio.charset.Charset;

import com.mysema.query.sql.dml.SQLInsertClause;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;

/**
 * This operation creates a file in the workspace.
 */
public final class CreateFileOperation extends AbstractCreateResourceOperation {

	/**
	 * the contents
	 */
	private final byte[] contents;

	/**
	 * Constructor.
	 * @param path the path of the file
	 * @param contents the text contents (assuming UTF-8 encoding)
	 * @param createEnclosingFolders whether enclosing folders shall be
	 * created as required
	 */
	public CreateFileOperation(final ResourcePath path, final String contents, boolean createEnclosingFolders) {
		this(path, contents == null ? null : contents.getBytes(Charset.forName("utf-8")), createEnclosingFolders);
	}

	/**
	 * Constructor.
	 * @param path the path of the file
	 * @param contents the binary contents
	 * @param createEnclosingFolders whether enclosing folders shall be
	 * created as required
	 */
	public CreateFileOperation(final ResourcePath path, final byte[] contents, boolean createEnclosingFolders) {
		super(path, createEnclosingFolders);
		this.contents = contents;
	}

	/**
	 * Getter method for the contents.
	 * @return the contents
	 */
	public byte[] getContents() {
		return contents;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.IWorkspaceOperationContext)
	 */
	@Override
	protected void perform(final IWorkspaceOperationContext context) {
		WorkspaceResources parentResource = createEnclosingFoldersIfNeeded(context);
		final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QWorkspaceResources.workspaceResources);
		insert.set(QWorkspaceResources.workspaceResources.name, getPath().getLastSegment());
		insert.set(QWorkspaceResources.workspaceResources.contents, (contents == null ? new byte[0] : contents));
		insert.set(QWorkspaceResources.workspaceResources.parentId, parentResource.getId());
		insert.set(QWorkspaceResources.workspaceResources.type, ResourceType.FILE.name());
		insert.execute();
	}

}
