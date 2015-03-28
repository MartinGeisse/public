/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template.basic;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.component.HttpModel;
import name.martingeisse.guiserver.template.AbstractSingleComponentConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.xml.builder.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * This configuration represents a wicket panel that loads its content from the backend.
 */
@StructuredElement
@RegisterComponentElement(localName = "includeBackend")
public final class IncludeBackendConfiguration extends AbstractSingleComponentConfiguration {

	/**
	 * the url
	 */
	private String url;

	/**
	 * the escape
	 */
	private boolean escape;

	/**
	 * Setter method for the url.
	 * @param url the url to set
	 */
	@BindAttribute(name = "url")
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Setter method for the escape.
	 * @param escape the escape to set
	 */
	@BindAttribute(name = "escape", optionality = AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT, defaultValue = "true")
	public void setEscape(boolean escape) {
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
