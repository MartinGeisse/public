/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

/**
 * Selector for the basic resource types: root, workspace, project, folder, file.
 */
public enum ResourceType {

	/**
	 * A folder that contains other resources. The root resource of every workspace
	 * must be a folder. Folders do not have binary contents.
	 */
	FOLDER,
	
	/**
	 * A file within a project or folder. A file does not contain resources but
	 * has binary content.
	 */
	FILE,
	
	/**
	 * This resource type is used for any resource which the framework doesn't
	 * recognize to be of any known type.
	 */
	OTHER;
	
}
