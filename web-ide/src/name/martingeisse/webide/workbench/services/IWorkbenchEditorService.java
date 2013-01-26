/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.services;

import name.martingeisse.webide.resources.ResourcePath;

/**
 * Allows to interact with workbench editors.
 */
public interface IWorkbenchEditorService {

	/**
	 * Opens the default editor for the specified path.
	 * @param path the path of the resource to edit.
	 */
	public void openDefaultEditor(ResourcePath path);

	/**
	 * Opens a non-default editor for the specified path.
	 * @param path the path of the resource to edit.
	 * @param editorId the ID of the editor extension to use
	 */
	public void openEditor(ResourcePath path, String editorId);
	
}
