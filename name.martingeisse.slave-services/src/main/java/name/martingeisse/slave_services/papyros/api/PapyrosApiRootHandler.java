/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.papyros.api;

import name.martingeisse.api.handler.misc.NamedResourceFolderHandler;
import name.martingeisse.slave_services.papyros.api.RenderTemplateApiHandler;

/**
 * The "root" folder handler.
 */
public final class PapyrosApiRootHandler extends NamedResourceFolderHandler {

	/**
	 * Constructor.
	 */
	public PapyrosApiRootHandler() {
		
		// add the "v1" subfolder and its actions
		NamedResourceFolderHandler v1 = new NamedResourceFolderHandler();
		getResources().put("v1", v1);
		v1.getResources().put("render", new RenderTemplateApiHandler());
		
	}
	
}
