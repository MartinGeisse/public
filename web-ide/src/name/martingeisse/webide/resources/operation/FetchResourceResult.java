/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.io.Serializable;

import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;

/**
 * This class is a simple container for information about a resource: path, type,
 * and contents. It is used to return the resource data in workspace operations.
 * 
 * The result also contains an ID that is valid and unique among all resources
 * in all workspaces only during the workspace operation. This means it has a
 * meaning while the operation is running and for the result of the operation,
 * but subsequent operations may return another ID for the resource and/or
 * another resource with that ID.
 */
public final class FetchResourceResult implements Serializable {

	/**
	 * the id
	 */
	private final long id;
	
	/**
	 * the parentId
	 */
	private final Long parentId;

	/**
	 * the path
	 */
	private final ResourcePath path;

	/**
	 * the type
	 */
	private final ResourceType type;

	/**
	 * the contents
	 */
	private final byte[] contents;

	/**
	 * TODO: remove
	 * This constructor is currently needed by the workspace implementation. It should not exist.
	 * @return the fake root
	 */
	public static FetchResourceResult createFakeRoot() {
		ResourcePath path = new ResourcePath("/");
		WorkspaceResources fakeResource = new WorkspaceResources();
		fakeResource.setId(0L);
		fakeResource.setParentId(null);
		fakeResource.setName("");
		fakeResource.setType(ResourceType.FOLDER.name());
		fakeResource.setContents(null);
		return new FetchResourceResult(path, fakeResource);
	}
	
	/**
	 * Constructor.
	 * @param path the path
	 * @param resource the database record for the resource
	 */
	FetchResourceResult(final ResourcePath path, final WorkspaceResources resource) {
		this.id = resource.getId();
		this.parentId = resource.getParentId();
		this.path = path;
		this.type = ResourceType.valueOf(resource.getType());
		this.contents = resource.getContents();
	}

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Getter method for the parentId.
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}
	
	/**
	 * Getter method for the path.
	 * @return the path
	 */
	public ResourcePath getPath() {
		return path;
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
	
}
