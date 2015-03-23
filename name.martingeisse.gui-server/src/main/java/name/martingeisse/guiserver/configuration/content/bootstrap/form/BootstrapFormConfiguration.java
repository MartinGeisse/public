/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.bootstrap.form;

import name.martingeisse.guiserver.configuration.content.basic.form.FormConfiguration;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

/**
 * Bootstrap-specific implementation of {@link FormConfiguration}. This class generates
 * slightly different markup (note: actually the markup is the same as for the normal
 * form at this time, but we want the bootstrap components to be "complete", so we still
 * define a specialized form configuration).
 */
@StructuredElement
@RegisterComponentElement(localName = "bsForm")
public class BootstrapFormConfiguration extends FormConfiguration {
}
