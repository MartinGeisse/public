/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.intercept;

import name.martingeisse.api.handler.misc.ClasspathResourceHandler;

/**
 * This handler mounts JQuery as "jquery.js" in the root path.
 */
public class JqueryHandler extends ClasspathResourceHandler {

	/**
	 * Constructor.
	 */
	public JqueryHandler() {
		super("text/javascript", "utf-8", null, "jquery-1.8.2.min.js");
	}
	
}
