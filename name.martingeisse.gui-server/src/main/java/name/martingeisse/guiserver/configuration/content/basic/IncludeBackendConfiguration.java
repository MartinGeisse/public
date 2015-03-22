/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractSingleComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.gui.HttpModel;
import name.martingeisse.guiserver.xml.builder.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xml.builder.BindPropertyAttribute;
import name.martingeisse.guiserver.xml.builder.BindComponentElement;
import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * This configuration represents a wicket panel that loads its content from the backend.
 */
@BindComponentElement(localName = "includeBackend", attributes = {
	@BindPropertyAttribute(name = "url"), @BindPropertyAttribute(name = "escape", optionality = AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT, defaultValue = "true")
})
public final class IncludeBackendConfiguration extends AbstractSingleComponentConfiguration {

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
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("wicket:container");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new Label(getComponentId(), new HttpModel(url)).setEscapeModelStrings(escape);
	}

}
