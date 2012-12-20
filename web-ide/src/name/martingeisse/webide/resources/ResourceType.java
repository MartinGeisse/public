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
	 * used for a user's root node ("mount space") to mount workspaces
	 */
	MOUNT_SPACE,
	
	/**
	 * a workspace
	 */
	WORKSPACE,
	
	/**
	 * a project within a workspace
	 */
	PROJECT,
	
	/**
	 * a folder within a project or other folder
	 */
	FOLDER,
	
	/**
	 * a file within a project or folder
	 */
	FILE;
	
}
