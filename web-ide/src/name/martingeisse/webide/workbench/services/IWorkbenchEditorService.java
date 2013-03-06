/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.services;

import name.martingeisse.webide.resources.ResourceHandle;

/**
 * Allows to interact with workbench editors.
 */
public interface IWorkbenchEditorService {

	/**
	 * Opens the default editor for the specified resource.
	 * @param resourceHandle the handle for the resource to edit.
	 */
	public void openDefaultEditor(ResourceHandle resourceHandle);

	/**
	 * Opens a non-default editor for the specified resource.
	 * @param resourceHandle the handle for the resource to edit.
	 * @param editorId the ID of the editor extension to use
	 */
	public void openEditor(ResourceHandle resourceHandle, String editorId);
	
}
