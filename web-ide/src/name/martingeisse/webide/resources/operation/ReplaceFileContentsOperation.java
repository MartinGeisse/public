/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.nio.charset.Charset;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;

import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * This operation replaces a file's contents.
 */
public final class ReplaceFileContentsOperation extends SingleResourceOperation {

	/**
	 * the contents
	 */
	private final byte[] contents;

	/**
	 * Constructor.
	 * @param path the path of the file
	 * @param contents the text contents (assuming UTF-8 encoding)
	 */
	public ReplaceFileContentsOperation(final ResourcePath path, final String contents) {
		this(path, contents == null ? null : contents.getBytes(Charset.forName("utf-8")));
	}

	/**
	 * Constructor.
	 * @param path the path of the file
	 * @param contents the binary contents
	 */
	public ReplaceFileContentsOperation(final ResourcePath path, final byte[] contents) {
		super(path);
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
		WorkspaceResources resource = fetchResource(context);
		if (ResourceType.valueOf(resource.getType()) != ResourceType.FILE) {
			throw new WorkspaceOperationException("cannot replace the contents of a resource of type: " + resource.getType());
		}
		final SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QWorkspaceResources.workspaceResources);
		update.where(QWorkspaceResources.workspaceResources.id.eq(resource.getId()));
		update.set(QWorkspaceResources.workspaceResources.contents, (contents == null ? new byte[0] : contents));
		update.execute();
	}

}
