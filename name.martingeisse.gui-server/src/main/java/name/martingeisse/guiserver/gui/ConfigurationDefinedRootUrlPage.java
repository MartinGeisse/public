/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.gui;

import name.martingeisse.guiserver.configuration.Configuration;
import name.martingeisse.guiserver.configuration.PageConfiguration;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Specialization of {@link ConfigurationDefinedPage} for the root URL.
 * The mapping logic works slightly different for that URL due to the
 * way Wicket works.
 */
public class ConfigurationDefinedRootUrlPage extends ConfigurationDefinedPage {

	/**
	 * Constructor.
	 * 
	 * @param pageParameters the page parameters
	 */
	public ConfigurationDefinedRootUrlPage(PageParameters pageParameters) {
		super(pageParameters);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.gui.ConfigurationDefinedPage#resolvePageConfiguration()
	 */
	@Override
	protected PageConfiguration resolvePageConfiguration() {
		return Configuration.getInstance().getRootUrlPageConfiguration();
	}
	
}
