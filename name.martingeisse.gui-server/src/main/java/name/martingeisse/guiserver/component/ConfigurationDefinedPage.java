/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.component;

import name.martingeisse.guiserver.configuration.element.xml.PageConfiguration;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * The most common kind of page. The configuration defines this page using content
 * elements.
 */
public class ConfigurationDefinedPage extends AbstractConfigurationDefinedPage {

	/**
	 * Constructor.
	 * 
	 * @param pageParameters the page parameters
	 */
	public ConfigurationDefinedPage(PageParameters pageParameters) {
		super(pageParameters, getPageConfigurationPath(pageParameters));
	}

	/**
	 * 
	 */
	private static String getPageConfigurationPath(PageParameters pageParameters) {
		String pageConfigurationPath = pageParameters.get(PageConfiguration.CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME).toString();
		if (pageConfigurationPath == null) {
			throw new RuntimeException("page configuration path not specified in page parameters");
		}
		return pageConfigurationPath;
	}
	
}
