/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.component;

import name.martingeisse.guiserver.configuration.Configuration;
import name.martingeisse.guiserver.template.demo.ComponentDemoConfiguration;
import name.martingeisse.wicket.component.codemirror.CodeMirrorBehavior;
import name.martingeisse.wicket.component.codemirror.modes.CodeMirrorModes;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

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
		IModel<String> sourceCodeModel = new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				return getConfiguration().getSourceCode();
			}
		};
		final CodeMirrorBehavior codeMirrorBehavior = new CodeMirrorBehavior(CodeMirrorModes.XML) {
			@Override
			protected void renderOptionsArgument(StringBuilder builder) {
				builder.append("{readOnly: true}");
			}
		};
		addToBorder(new TextArea<>("sourceCode", sourceCodeModel).add(codeMirrorBehavior));
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
