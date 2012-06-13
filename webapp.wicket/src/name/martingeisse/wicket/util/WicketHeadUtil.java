/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * Helper functions for use in head sections and header contributors.
 */
public class WicketHeadUtil {

	/**
	 * Prevent instantiation.
	 */
	private WicketHeadUtil() {
	}
	
	/**
	 * Renders a Javascript reference to a file that is placed in the package of the
	 * specified class and whose name is the simple name of the class, with the ".js"
	 * extension appended. The reference is identified by the class's canonical name.
	 * 
	 * @param response the header response to which the reference shall be added
	 * @param c the class whose code shall be included
	 */
	public static void includeClassJavascript(IHeaderResponse response, Class<?> c) {
		response.renderJavaScriptReference(new PackageResourceReference(c, c.getSimpleName() + ".js"), c.getCanonicalName());
	}
	
}
