/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.babel.api;

import name.martingeisse.api.handler.misc.NamedResourceFolderHandler;

/**
 * The "root" folder handler.
 */
public final class BabelApiRootHandler extends NamedResourceFolderHandler {

	/**
	 * Constructor.
	 */
	public BabelApiRootHandler() {
		
		// add the "v1" subfolder and its actions
		NamedResourceFolderHandler v1 = new NamedResourceFolderHandler();
		getResources().put("v1", v1);
		v1.getResources().put("cakephp-po", new DownloadCakeTranslationFileApiHandler());
		
	}
	
}
