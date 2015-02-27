/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.elements;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.guiserver.application.wicket.GuiWicketApplication;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.content.IComponentGroupConfigurationVisitor;
import name.martingeisse.guiserver.configuration.content.IComponentGroupConfigurationVisitorAcceptor;
import name.martingeisse.guiserver.configuration.content.UrlSubpathComponentGroupConfiguration;
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
		PageParameters identifyingParameters = new PageParameters();
		identifyingParameters.add(CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME, getPath());
		application.mount(new ParameterMountedRequestMapper(getPath(), ConfigurationDefinedPage.class, identifyingParameters));
		mountComponentUrls(application, getPath(), content.getComponents());
	}

	/**
	 * 
	 */
	private void mountComponentUrls(GuiWicketApplication application, String pathPrefix, final IComponentGroupConfigurationVisitorAcceptor acceptor) {
		
		// find the components that want to provide the next URL segment
		final List<UrlSubpathComponentGroupConfiguration> subpathComponents = new ArrayList<>();
		acceptor.accept(new IComponentGroupConfigurationVisitor() {
			
			@Override
			public boolean beginVisit(ComponentGroupConfiguration componentConfiguration) {
				if (componentConfiguration != acceptor && componentConfiguration instanceof UrlSubpathComponentGroupConfiguration) {
					subpathComponents.add((UrlSubpathComponentGroupConfiguration)componentConfiguration);
					return false;
				} else {
					return true;
				}
			}

			@Override
			public void endVisit(ComponentGroupConfiguration componentConfiguration) {
			}
			
		});
		
		// find the relevant page parameters (multiple components might use the same),
		// but try to keep the order intact since it looks nicer
		final List<String> urlSegments = new ArrayList<>();
		for (UrlSubpathComponentGroupConfiguration component : subpathComponents) {
			for (String parameterName : component.getSubpathSegmentParameterNames()) {
				if (!urlSegments.contains(parameterName)) {
					urlSegments.add(parameterName);
				}
			}
		}
		
		// TODO detect if two *nested* components want to use the same parameter and throw an error!
		// That Only works for siblings!
		
		// build the mount URL
		StringBuilder builder = new StringBuilder(pathPrefix);
		for (String segment : urlSegments) {
			builder.append("/${").append(segment).append('}');
		}
		String parameterizedPath = builder.toString();
		PageParameters identifyingParameters = new PageParameters();
		identifyingParameters.add(CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME, getPath());

		// mount the current URL first to give it less priority when generating URLs
		System.out.println(parameterizedPath);
		application.mount(new ParameterMountedRequestMapper(parameterizedPath, ConfigurationDefinedPage.class, identifyingParameters));

		// allow nested components to contribute their paths too
		for (UrlSubpathComponentGroupConfiguration component : subpathComponents) {
			mountComponentUrls(application, parameterizedPath, component);
		}
		
	}
	
}
