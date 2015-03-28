/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.component;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * A special page implementation that is similar to {@link ConfigurationDefinedPage},
 * but is used for the root path, so its mounting logic must work slightly differently.
 */
public class ConfigurationDefinedHomePage extends AbstractConfigurationDefinedPage {

	/**
	 * Constructor.
	 * 
	 * @param pageParameters the page parameters
	 */
	public ConfigurationDefinedHomePage(PageParameters pageParameters) {
		super(pageParameters, "/");
	}

}
