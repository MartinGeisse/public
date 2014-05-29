/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.core.request.handler.IPageClassRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * Utility methods to deal with pages.
 */
public final class WicketPageUtil {

	/**
	 * Redirects to a new instance of the current page class, with the current page
	 * parameters. Useful to escape when stuck in a stateful page with outdated values.
	 */
	public static void setResponsePageToNewPageInstance() {
		RequestCycle requestCycle = RequestCycle.get();
		IPageClassRequestHandler requestHandler = requestCycle.find(IPageClassRequestHandler.class);
		if (requestHandler == null) {
			throw new IllegalStateException("no IPageClassRequestHandler found");
		}
		requestCycle.setResponsePage(requestHandler.getPageClass(), requestHandler.getPageParameters());
	}
	
	/**
	 * Prevent instantiation.
	 */
	private WicketPageUtil() {
	}
	
}
