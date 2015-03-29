/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.element;

import org.apache.wicket.request.mapper.ICompoundRequestMapper;

/**
 * The base class for configuration elements.
 * 
 * Each element has a path that identifies it among elements of the
 * same type. Elements with different type may use the same path. This
 * is useful, for example, to define a page with a form and a
 * corresponding form API with the same name, which keeps things simple. 
 * 
 * Each element has a backend URL that is used to access it in the
 * backend server. This URL is built from the element's path and type.
 */
public abstract class Element {

	/**
	 * the path
	 */
	private final String path;

	/**
	 * Constructor.
	 * @param path the path to this element
	 */
	public Element(String path) {
		this.path = path;
	}

	/**
	 * Getter method for the path.
	 * @return the path
	 */
	public final String getPath() {
		return path;
	}

	/**
	 * Mounts all Wicket URLs needed by this element.
	 * @param application the application used to mount URLs
	 */
	public void mountWicketUrls(ICompoundRequestMapper compoundRequestMapper) {		
	}
	
}
