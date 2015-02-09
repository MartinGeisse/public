/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

/**
 * This interface is implemented by component configurations that
 * want to provide URL subpaths.
 */
public interface UrlSubpathComponentConfiguration extends ComponentConfiguration {

	/**
	 * @return the names of the page parameters mapped to subpath segments
	 */
	public String[] getSubpathSegmentParameterNames();
	
}
