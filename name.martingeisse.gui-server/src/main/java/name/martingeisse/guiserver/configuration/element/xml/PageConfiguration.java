/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.element.xml;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.guiserver.component.ConfigurationDefinedPage;
import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.IComponentGroupConfigurationVisitor;
import name.martingeisse.guiserver.template.IComponentGroupConfigurationVisitorAcceptor;
import name.martingeisse.guiserver.template.Template;
import name.martingeisse.guiserver.template.UrlSubpathComponentGroupConfiguration;
import name.martingeisse.wicket.util.ParameterMountedRequestMapper;

import org.apache.wicket.request.mapper.ICompoundRequestMapper;
import org.apache.wicket.request.mapper.parameter.PageParameters;


/**
 * The configuration for a page.
 */
public final class PageConfiguration extends Element {

	/**
	 * the CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME
	 */
	public static final String CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME = "__INTERNAL_CONFIGURATION_ELEMENT_PATH__";
	
	/**
	 * the template
	 */
	private Template template;
	
	/**
	 * Constructor.
	 * @param path the path to this page
	 * @param markupSourceCode the source code for the page's markup
	 */
	public PageConfiguration(String path, Template template) {
		super(path);
		this.template = template;
	}

	/**
	 * Getter method for the template.
	 * @return the template
	 */
	public Template getTemplate() {
		return template;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.Element#mountWicketUrls(org.apache.wicket.request.mapper.ICompoundRequestMapper)
	 */
	@Override
	public void mountWicketUrls(ICompoundRequestMapper configurationDefinedRequestMapper) {
		PageParameters identifyingParameters = new PageParameters();
		identifyingParameters.add(CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME, getPath());
		if (!getPath().equals("/")) {
			configurationDefinedRequestMapper.add(new ParameterMountedRequestMapper(getPath(), ConfigurationDefinedPage.class, identifyingParameters));
		}
		mountComponentUrls(configurationDefinedRequestMapper, getPath(), template.getComponents());
	}

	/**
	 * 
	 */
	private void mountComponentUrls(ICompoundRequestMapper configurationDefinedRequestMapper, String pathPrefix, final IComponentGroupConfigurationVisitorAcceptor acceptor) {
		
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
		if (urlSegments.isEmpty()) {
			return;
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
		configurationDefinedRequestMapper.add(new ParameterMountedRequestMapper(parameterizedPath, ConfigurationDefinedPage.class, identifyingParameters));

		// allow nested components to contribute their paths too
		for (UrlSubpathComponentGroupConfiguration component : subpathComponents) {
			mountComponentUrls(configurationDefinedRequestMapper, parameterizedPath, component);
		}
		
	}
	
}
