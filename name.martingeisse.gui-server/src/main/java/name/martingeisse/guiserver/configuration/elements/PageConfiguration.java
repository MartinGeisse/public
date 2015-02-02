/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.elements;

import name.martingeisse.guiserver.application.wicket.GuiWicketApplication;
import name.martingeisse.guiserver.gui.ConfigurationDefinedPage;
import name.martingeisse.wicket.util.ParameterMountedRequestMapper;

import org.apache.wicket.request.mapper.parameter.PageParameters;


/**
 * The configuration for a page.
 */
public final class PageConfiguration extends ConfigurationElement {

	/**
	 * the CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME
	 */
	public static final String CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME = "__INTERNAL_CONFIGURATION_ELEMENT_PATH__";
	
	/**
	 * the CONFIGURATION_FILENAME_SUFFIX
	 */
	public static final String CONFIGURATION_FILENAME_SUFFIX = ".page.xml";
	
	/**
	 * the content
	 */
	private ConfigurationElementContent content;
	
	/**
	 * Constructor.
	 * @param path the path to this page
	 * @param markupSourceCode the source code for the page's markup
	 */
	public PageConfiguration(String path, ConfigurationElementContent content) {
		super(path);
		this.content = content;
	}

	/**
	 * Getter method for the content.
	 * @return the content
	 */
	public ConfigurationElementContent getContent() {
		return content;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configurationNew.ConfigurationElement#getBackendUriPath()
	 */
	@Override
	public String getBackendUriPath() {
		return getPath() + CONFIGURATION_FILENAME_SUFFIX;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configurationNew.ConfigurationElement#mountWicketUrls(name.martingeisse.guiserver.application.wicket.GuiWicketApplication)
	 */
	@Override
	public void mountWicketUrls(GuiWicketApplication application) {
		String path = getPath();
		PageParameters identifyingParameters = new PageParameters();
		identifyingParameters.add(CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME, path);
		application.mount(new ParameterMountedRequestMapper(getPath(), ConfigurationDefinedPage.class, identifyingParameters));
	}
	
}
