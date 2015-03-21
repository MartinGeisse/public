/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.bootstrap.form;

import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.form.FormConfiguration;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.BindElement;
import name.martingeisse.guiserver.xml.result.MarkupContent;

/**
 * Bootstrap-specific implementation of {@link FormConfiguration}. This class generates
 * slightly different markup (note: actually the markup is the same as for the normal
 * form at this time, but we want the bootstrap components to be "complete", so we still
 * define a specialized form configuration).
 */
@BindElement(localName = "bsForm", attributes = {@BindAttribute(name = "backendUrl")}, acceptsMarkupContent = true)
public class BootstrapFormConfiguration extends FormConfiguration {

	/**
	 * Constructor.
	 * @param backendUrl the backend URL
	 * @param markupContent the markup content
	 */
	public BootstrapFormConfiguration(String backendUrl, MarkupContent<ComponentGroupConfiguration> markupContent) {
		super(backendUrl, markupContent);
	}

}
