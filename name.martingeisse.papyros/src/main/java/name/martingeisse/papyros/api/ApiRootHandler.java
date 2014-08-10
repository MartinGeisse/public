/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.api;

import name.martingeisse.api.handler.misc.NamedResourceFolderHandler;

/**
 * The "root" folder handler.
 */
public final class ApiRootHandler extends NamedResourceFolderHandler {

	/**
	 * Constructor.
	 */
	public ApiRootHandler() {
		
		// add the "v1" subfolder and its actions
		NamedResourceFolderHandler v1 = new NamedResourceFolderHandler();
		getResources().put("v1", v1);
		v1.getResources().put("render", new RenderTemplateApiHandler());
		
		
	}
	
}
