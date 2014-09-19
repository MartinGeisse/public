/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.common.api;

import name.martingeisse.api.handler.misc.NamedResourceFolderHandler;
import name.martingeisse.slave_services.babel.api.BabelApiRootHandler;
import name.martingeisse.slave_services.papyros.api.PapyrosApiRootHandler;

/**
 * The "root" folder handler.
 */
public final class ApiRootHandler extends NamedResourceFolderHandler {

	/**
	 * Constructor.
	 */
	public ApiRootHandler() {
		getResources().put("papyros", new PapyrosApiRootHandler());
		getResources().put("babel", new BabelApiRootHandler());
	}
	
}
