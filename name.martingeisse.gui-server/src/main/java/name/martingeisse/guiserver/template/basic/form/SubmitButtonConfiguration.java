/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.basic.form;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.template.AbstractSingleComponentConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

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
