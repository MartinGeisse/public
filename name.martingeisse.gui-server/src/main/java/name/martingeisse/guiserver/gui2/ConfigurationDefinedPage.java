/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.gui2;

import name.martingeisse.guiserver.application.page.AbstractApplicationPage;
import name.martingeisse.guiserver.configurationNew.Configuration;
import name.martingeisse.guiserver.configurationNew.PageConfiguration;

import org.apache.wicket.request.mapper.parameter.PageParameters;

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
		String pageConfigurationPath = getPageParameters().get(PageConfiguration.CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME).toString();
		if (pageConfigurationPath == null) {
			throw new RuntimeException("page configuration path not specified in page parameters");
		}
		return Configuration.getInstance().getElement(PageConfiguration.class, pageConfigurationPath);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Page#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		PageConfiguration pageConfiguration = getPageConfiguration();
		// TODO add(new ContentElementRepeater("elements", pageConfiguration.getContentElements()));
	}
	
}