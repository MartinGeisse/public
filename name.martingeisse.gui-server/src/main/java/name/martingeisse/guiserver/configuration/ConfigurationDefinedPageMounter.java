/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import java.util.Map;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import name.martingeisse.guiserver.application.wicket.GuiWicketApplication;
import name.martingeisse.guiserver.gui.ConfigurationDefinedPage;
import name.martingeisse.wicket.util.ParameterMountedRequestMapper;

/**
 * Mounts the pages defined in the configuration using a
 * {@link ParameterMountedRequestMapper} for each page.
 */
public final class ConfigurationDefinedPageMounter {

	/**
	 * the CONFIGURATION_PATH_PAGE_PARAMETER_NAME
	 */
	public static final String CONFIGURATION_PATH_PAGE_PARAMETER_NAME = "__INTERNAL_CONFIGURATION_PATH__";
	
	/**
	 * Prevent instantiation.
	 */
	private ConfigurationDefinedPageMounter() {
	}
	
	/**
	 * Mounts the pages.
	 * 
	 * @param application the application
	 */
	public static void mount(GuiWicketApplication application) {
		mount(application, Configuration.getInstance().getRootNamespace(), "");
	}

	/**
	 * 
	 */
	private static void mount(GuiWicketApplication application, ConfigurationNamespace namespace, String namespacePrefix) {
		for (Map.Entry<String, ConfigurationElement> element : namespace.getElements().entrySet()) {
			String configurationPath = namespacePrefix + '/' + element.getKey();
			ConfigurationElement value = element.getValue();
			if (value instanceof ConfigurationNamespace) {
				mount(application, (ConfigurationNamespace)value, configurationPath);
			} else if (value instanceof PageConfiguration) {
				PageConfiguration pageConfiguration = (PageConfiguration)value;
				PageParameters identifyingParameters = new PageParameters();
				identifyingParameters.add(CONFIGURATION_PATH_PAGE_PARAMETER_NAME, configurationPath);
				String urlPath = pageConfiguration.getUrlPath();
				if (!urlPath.isEmpty()) {
					application.mount(new ParameterMountedRequestMapper(urlPath, ConfigurationDefinedPage.class, identifyingParameters));
				}
			}
		}
	}

}
