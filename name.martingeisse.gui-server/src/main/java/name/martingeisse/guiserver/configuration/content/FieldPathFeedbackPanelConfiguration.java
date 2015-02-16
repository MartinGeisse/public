/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.gui.DisappearingFeedbackPanel;
import name.martingeisse.guiserver.gui.FieldPathBehavior;
import name.martingeisse.guiserver.gui.FieldPathFeedbackMessageFilter;
import name.martingeisse.guiserver.xml.attribute.BindAttribute;
import name.martingeisse.guiserver.xml.element.BindComponentElement;
import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;

import org.apache.wicket.Component;

/**
 * A panel that shows feedback messages for a form component with a {@link FieldPathBehavior}.
 */
@BindComponentElement(localName = "feedback", attributes = {
	@BindAttribute(name = "name")
})
public final class FieldPathFeedbackPanelConfiguration extends AbstractComponentConfiguration {

	/**
	 * the path
	 */
	private final String path;

	/**
	 * Constructor.
	 * @param path the field path to show feedback messages for
	 */
	public FieldPathFeedbackPanelConfiguration(String path) {
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("div");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getId());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new DisappearingFeedbackPanel(getId(), new FieldPathFeedbackMessageFilter(path));
	}

}
