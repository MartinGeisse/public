/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import name.martingeisse.guiserver.application.page.AbstractApplicationPage;
import name.martingeisse.guiserver.configuration.Configuration;
import name.martingeisse.guiserver.configuration.ConfigurationDefinedPageMounter;
import name.martingeisse.guiserver.configuration.PageConfiguration;

/**
 * The most common kind of page. The configuration defines this page using content
 * elements.
 */
public class ConfigurationDefinedPage extends AbstractApplicationPage {

	/**
	 * the cachedPageConfiguration
	 */
	private transient PageConfiguration cachedPageConfiguration = null;
	
	/**
	 * Constructor.
	 * 
	 * @param pageParameters the page parameters
	 */
	public ConfigurationDefinedPage(PageParameters pageParameters) {
		super(pageParameters);
	}
	
	/**
	 * Getter method for the page configuration.
	 * @return the page configuration.
	 */
	public final PageConfiguration getPageConfiguration() {
		if (cachedPageConfiguration == null) {
			cachedPageConfiguration = resolvePageConfiguration();
		}
		return cachedPageConfiguration;
	}
	
	/**
	 * Getter method for the page configuration path.
	 * @return the page configuration path
	 */
	protected PageConfiguration resolvePageConfiguration() {
		String pageConfigurationPath = getPageParameters().get(ConfigurationDefinedPageMounter.CONFIGURATION_PATH_PAGE_PARAMETER_NAME).toString();
		if (pageConfigurationPath == null) {
			throw new RuntimeException("page configuration key not specified in page parameters");
		}
		return Configuration.getInstance().getElementAbsolute(pageConfigurationPath, PageConfiguration.class);
	}
	
}
