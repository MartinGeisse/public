/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import name.martingeisse.guiserver.application.page.AbstractApplicationPage;
import name.martingeisse.guiserver.configuration.Configuration;
import name.martingeisse.guiserver.configuration.PageConfiguration;

/**
 * The most common kind of page. The configuration defines this page using content
 * elements.
 */
public class ConfigurationDefinedPage extends AbstractApplicationPage {

	/**
	 * Constructor.
	 * 
	 * @param pageParameters the page parameters
	 */
	public ConfigurationDefinedPage(PageParameters pageParameters) {
		String pageConfigurationKey = pageParameters.get("pageConfigurationKey").toString();
		if (pageConfigurationKey == null) {
			throw new RuntimeException("pageConfigurationKey not specified");
		}
		PageConfiguration pageConfiguration = Configuration.getInstance().getElementAbsolute(pageConfigurationKey, PageConfiguration.class);
	}
	
}
