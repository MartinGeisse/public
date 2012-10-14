/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import name.martingeisse.api.handler.misc.NamedResourceFolderHandler;
import name.martingeisse.api.handler.misc.RequestDumpHandler;

/**
 * The main application handler.
 */
public class ApplicationHandler extends NamedResourceFolderHandler {

	/**
	 * Constructor.
	 */
	public ApplicationHandler() {
		getResources().put("foo", new RequestDumpHandler());
		getResources().put("bar", new RequestDumpHandler());
		getResources().put("cache", new CacheTestHandler());
		getResources().put("query", new QueryCacheTestHandler());
		getResources().put("multi", new MultiQueryCacheTestHandler());
	}
	
}
