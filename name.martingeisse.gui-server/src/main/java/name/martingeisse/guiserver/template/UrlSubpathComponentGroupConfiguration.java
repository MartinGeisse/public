/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template;

/**
 * This interface is implemented by component group configurations that
 * want to provide URL subpaths.
 */
public interface UrlSubpathComponentGroupConfiguration extends ComponentGroupConfiguration {

	/**
	 * @return the names of the page parameters mapped to subpath segments
	 */
	public String[] getSubpathSegmentParameterNames();
	
}
