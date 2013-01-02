/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.handle;

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
 * Handles are immutable: They will always refer to the same resource
 * after creation. The {@link #equals(Object)} and {@link #hashCode()}
 * methods are implemented accordingly. Methods that return other
 * handles may or may not return the same handle on multiple invocations.
 */
public interface IResourceHandle {

}
