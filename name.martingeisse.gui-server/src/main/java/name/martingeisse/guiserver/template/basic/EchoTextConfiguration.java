/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template.basic;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.template.AbstractSingleComponentConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.template.model.NamedModelReferenceBehavior;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * This configuration outputs text from a model. Any HTML special characters
 * will be escaped.
 */
@StructuredElement
@RegisterComponentElement(localName = "text")
public final class EchoTextConfiguration extends AbstractSingleComponentConfiguration {

	/**
	 * the modelReferenceSpecification
	 */
	private String modelReferenceSpecification;

	/**
	 * Setter method for the modelReferenceSpecification.
	 * @param modelReferenceSpecification the modelReferenceSpecification to set
	 */
	@BindAttribute(name = "model")
	public void setModelReferenceSpecification(String modelReferenceSpecification) {
		this.modelReferenceSpecification = modelReferenceSpecification;
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
		return new Label(getComponentId()).add(new NamedModelReferenceBehavior(modelReferenceSpecification));
	}

}
