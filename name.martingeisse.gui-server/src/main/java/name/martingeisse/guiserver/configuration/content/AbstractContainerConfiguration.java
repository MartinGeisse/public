/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;
import name.martingeisse.guiserver.xml.result.MarkupContent;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;

import com.google.common.collect.ImmutableList;

/**
 * Base class for {@link MarkupContainer} component configurations.
 */
public abstract class AbstractContainerConfiguration extends AbstractComponentConfiguration {

	/**
	 * the markupContent
	 */
	private final MarkupContent<ComponentConfiguration> markupContent;

	/**
	 * the children
	 */
	private ComponentConfigurationList children;

	/**
	 * Constructor.
	 * @param markupContent the markup content
	 */
	public AbstractContainerConfiguration(MarkupContent<ComponentConfiguration> markupContent) {
		this.markupContent = markupContent;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembleContainerIntro(assembler);
		assembleContainerContents(assembler);
		assembleContainerOutro(assembler);
	}

	/**
	 * Assembles the container "intro". The default implementation renders an opening DIV
	 * tag with the component's wicket:id.
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	protected void assembleContainerIntro(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		writeOpeningComponentTag(assembler, "div");
	}

	/**
	 * Writes the opening component tag, using the specified local element name and taking
	 * the wicket:id attribute from the {@link #getId()} method. This method is useful for
	 * implementing {@link #assembleContainerIntro(ConfigurationAssembler)}.
	 * 
	 * Calling this method may be followed by writeAttribute() calls to the XML writer,
	 * to add further attributes to the component tag.
	 * 
	 * @param assembler the configuration assembler
	 * @param localName the local element name
	 * @throws XMLStreamException on XML stream processing errors
	 */
	protected final void writeOpeningComponentTag(ConfigurationAssembler<ComponentConfiguration> assembler, String localName) throws XMLStreamException {
		assembler.getMarkupWriter().writeStartElement(localName);
		assembler.getMarkupWriter().writeAttribute("wicket:id", getId());
	}

	/**
	 * Assembles the container contents. The default implementation invokes the assemble() method
	 * on the markup content, collecting all component configurations from that markup in a new
	 * list, and stores that list as this component's children.
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	protected void assembleContainerContents(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		List<ComponentConfiguration> childrenAccumulator = new ArrayList<ComponentConfiguration>();
		ConfigurationAssembler<ComponentConfiguration> subAssembler = assembler.withComponentAccumulator(childrenAccumulator);
		markupContent.assemble(subAssembler);
		this.children = new ComponentConfigurationList(ImmutableList.copyOf(childrenAccumulator));
	}
	
	/**
	 * Assembles the container "outro". The default implementation renders a single
	 * closing tag. This is appropriate if the intro renders a corresponding opening
	 * tag (and possibly other content, but no other *unclosed* opening tag).
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	protected void assembleContainerOutro(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		assembler.getMarkupWriter().writeEndElement();
	}
	
	/**
	 * Getter method for the children.
	 * @return the children
	 */
	protected final ComponentConfigurationList getChildren() {
		return children;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configurationNew.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		MarkupContainer container = buildContainer();
		children.buildAndAddComponents(container);
		return container;
	}

	/**
	 * Builds the container itself, not adding any children.
	 * @return the container
	 */
	protected abstract MarkupContainer buildContainer();

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#accept(name.martingeisse.guiserver.configuration.content.IComponentConfigurationVisitor)
	 */
	@Override
	public void accept(IComponentConfigurationVisitor visitor) {
		if (visitor.beginVisit(this)) {
			children.accept(visitor);
			visitor.endVisit(this);
		}
	}

}
