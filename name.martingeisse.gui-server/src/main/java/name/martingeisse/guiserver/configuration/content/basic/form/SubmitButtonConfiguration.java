/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic.form;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractSingleComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;
import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;

import org.apache.wicket.Component;

/**
 * This configuration generates a submit button.
 */
@StructuredElement
@RegisterComponentElement(localName = "submit")
public final class SubmitButtonConfiguration extends AbstractSingleComponentConfiguration {

	/**
	 * Constructor.
	 */
	public SubmitButtonConfiguration() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("input");
		assembler.getMarkupWriter().writeAttribute("type", "submit");
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return null;
	}

}
