/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.handle;

import name.martingeisse.webide.resources.ResourcePath;

/**
 * Base type for resource handles. Each instance represents an existing
 * or potential workspace resource. Instances are handles in the sense
 * that they may refer to missing resources, and that multiple handles
 * may represent the same resource. Methods exist to access the actual
 * resource behind the handle.
 * 
 * No two resources with the same parent resource may have the same
 * name (note that mount spaces and workspace roots do not have names). 
 * This implies that within a project or folder, there must not be a
 * file and a folder with the same name. However, a file and folder
 * with the same parent and the same name are still considered two
 * different resources; they just must not exist at the same time. They
 * are also represented by two different handles. (Special note to
 * those with Eclipse experience: Consider the case that a single
 * operation replaces a file by a folder with the same name, or vice
 * versa. Eclipse considers this a change of the resource type, although
 * it also considers files and folders to be different resources,
 * which is somewhat inconsistent. This API, in contrast, treats
 * such a replacement as a remove/add pair of different resources that
 * happen to have the same name).
 * 
 * Each resource has a path that specifies its location within its
 * workspace. The workspace root has the absolute path "/". Workspaces
 * themselves are not addressed by a path within the mount space;
 * this makes paths independent of mounting.
 * 
 * Handles are immutable: They will always refer to the same path and
 * resource type after creation. The {@link #equals(Object)} and
 * {@link #hashCode()} methods are implemented accordingly. This
 * implies that "moving" this resource actually results in deletion
 * of this resource and creation of another resource. Methods that
 * return other handles may or may not return the same handle on
 * multiple invocations.
 */
public interface IResourceHandle {

	/**
	 * Returns the path for this resource.
	 * @return the path
	 */
	public ResourcePath getPath();
	
	// TODO: probably remove in favor of operations
	
}
