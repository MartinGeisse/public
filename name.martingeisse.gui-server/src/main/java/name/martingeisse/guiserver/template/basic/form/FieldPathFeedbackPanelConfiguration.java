/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template.basic.form;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.component.DisappearingFeedbackPanel;
import name.martingeisse.guiserver.component.FieldPathBehavior;
import name.martingeisse.guiserver.component.FieldPathFeedbackMessageFilter;
import name.martingeisse.guiserver.template.AbstractSingleComponentConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.Component;

/**
 * A panel that shows feedback messages for a form component with a {@link FieldPathBehavior}.
 */
@StructuredElement
@RegisterComponentElement(localName = "feedback")
public final class FieldPathFeedbackPanelConfiguration extends AbstractSingleComponentConfiguration {

	/**
	 * the path
	 */
	private String path;

	/**
	 * Setter method for the path.
	 * @param path the path to set
	 */
	@BindAttribute(name = "name")
	public void setPath(String path) {
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("div");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new DisappearingFeedbackPanel(getComponentId(), new FieldPathFeedbackMessageFilter(path));
	}

}
