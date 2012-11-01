/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import name.martingeisse.api.handler.misc.NamedResourceFolderHandler;
import name.martingeisse.api.handler.misc.RequestDumpHandler;
import name.martingeisse.api.i18n.LocalizationUtil;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;
import name.martingeisse.apidemo.localization.LocalizationHandler;

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
		getResources().put("query", new QueryCacheTestHandler());
		getResources().put("multi", new MultiQueryCacheTestHandler());
		getResources().put("time", new TimeTestHandler());
		getResources().put("keys", new MultiKeyCacheTestHandler());
		getResources().put("counter", new SessionCounterHandler());
		getResources().put("localization", new LocalizationHandler());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.misc.NamedResourceFolderHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		LocalizationUtil.pushContext(getClass());
		try {
			super.handle(requestCycle, path);
		} finally {
			LocalizationUtil.popContext();
		}
	}
	
}
