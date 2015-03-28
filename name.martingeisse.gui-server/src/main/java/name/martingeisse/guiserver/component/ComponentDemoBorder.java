/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.component;

import name.martingeisse.guiserver.configuration.Configuration;
import name.martingeisse.guiserver.template.demo.ComponentDemoConfiguration;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * Shows a "source code" section below one or more components.
 */
public class ComponentDemoBorder extends Border {

	/**
	 * the configurationHandle
	 */
	private final int configurationHandle;

	/**
	 * the cachedConfiguration
	 */
	private transient ComponentDemoConfiguration cachedConfiguration;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param configuration the configuration
	 */
	public ComponentDemoBorder(String id, ComponentDemoConfiguration configuration) {
		super(id);
		this.configurationHandle = configuration.getSnippetHandle();
		this.cachedConfiguration = configuration;
		addToBorder(new Label("sourceCode", new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				return getConfiguration().getSourceCode();
			}
		}));
	}

	/**
	 * Getter method for the configuration.
	 * @return the configuration.
	 */
	public final ComponentDemoConfiguration getConfiguration() {
		if (cachedConfiguration == null) {
			cachedConfiguration = (ComponentDemoConfiguration)Configuration.getInstance().getSnippet(configurationHandle);
		}
		return cachedConfiguration;
	}

}
