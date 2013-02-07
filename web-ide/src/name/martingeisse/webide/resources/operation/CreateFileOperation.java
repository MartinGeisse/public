/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.nio.charset.Charset;

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
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.WorkspaceOperationContext)
	 */
	@Override
	protected void perform(final WorkspaceOperationContext context) {
		FetchResourceResult parentResource = createEnclosingFoldersIfNeeded(context);
		trace("will create file now", getPath());
		long id = insert(parentResource.getId(), getPath().getLastSegment(), ResourceType.FILE, (contents == null ? new byte[0] : contents));
		WorkspaceCache.onCreate(id, parentResource.getId(), getPath());
		WorkspaceResourceDeltaUtil.generateDeltas("create file", getPath());
	}

}
