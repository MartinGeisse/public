/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.services;

/**
 * Container interface for various workbench services.
 */
public interface IWorkbenchServicesProvider {

	/**
	 * Returns the editor service.
	 * @return the editor service
	 */
	public IWorkbenchEditorService getEditorService();
	
}
