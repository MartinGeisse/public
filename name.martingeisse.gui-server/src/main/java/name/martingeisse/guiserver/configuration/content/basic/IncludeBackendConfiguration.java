/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.gui.HttpModel;
import name.martingeisse.guiserver.xml.attribute.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xml.attribute.BindAttribute;
import name.martingeisse.guiserver.xml.element.BindComponentElement;
import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * This configuration represents a wicket panel that loads its content from the backend.
 */
@BindComponentElement(localName = "includeBackend", attributes = {
	@BindAttribute(name = "url"), @BindAttribute(name = "escape", optionality = AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT, defaultValue = "true")
})
public final class IncludeBackendConfiguration extends AbstractComponentConfiguration {

	/**
	 * the url
	 */
	private final String url;

	/**
	 * the escape
	 */
	private final boolean escape;

	/**
	 * Constructor.
	 * @param url the URL to load from
	 * @param escape whether to escape HTML special characters
	 */
	public IncludeBackendConfiguration(String url, boolean escape) {
		this.url = url;
		this.escape = escape;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("wicket:container");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getId());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new Label(getId(), new HttpModel(url)).setEscapeModelStrings(escape);
	}

}
