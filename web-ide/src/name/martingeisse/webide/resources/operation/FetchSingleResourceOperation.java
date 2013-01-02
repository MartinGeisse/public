/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;

/**
 * Fetches the resource type and contents for a single resource and
 * makes them available through a getter method.
 */
public final class FetchSingleResourceOperation extends SingleResourceOperation {

	/**
	 * the type
	 */
	private ResourceType type;
	
	/**
	 * the contents
	 */
	private byte[] contents;
	
	/**
	 * Constructor.
	 * @param path the path of the resource
	 */
	public FetchSingleResourceOperation(final ResourcePath path) {
		super(path);
	}

	/**
	 * Getter method for the type.
	 * @return the type
	 */
	public ResourceType getType() {
		return type;
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
	protected void perform(IWorkspaceOperationContext context) {
		WorkspaceResources resource = fetchResource(context);
		this.type = ResourceType.valueOf(resource.getType());
		this.contents = resource.getContents();
	}
	
}
