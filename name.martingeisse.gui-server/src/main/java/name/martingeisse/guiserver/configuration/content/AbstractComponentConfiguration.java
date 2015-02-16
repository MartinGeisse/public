/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;

/**
 * Base class for component configurations.
 */
public abstract class AbstractComponentConfiguration implements ComponentConfiguration {

	/**
	 * the id
	 */
	private String id;

	/**
	 * Constructor.
	 */
	public AbstractComponentConfiguration() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		this.id = getIdPrefix() + assembler.getComponentAccumulatorSize();
		if (this instanceof IConfigurationSnippet) {
			IConfigurationSnippet snippet = (IConfigurationSnippet)this;
			snippet.setSnippetHandle(assembler.addSnippet(snippet));
		}
		assembler.addComponent(this);
	}
	
	/**
	 * Returns the prefix for the component ID. Subclasses may override this
	 * method to provide a prefix that gives a hint towards the meaning of the
	 * component, making the generated Wicket markup easier to understand
	 * (especially for debugging purposes).
	 * 
	 * @return the component ID prefix
	 */
	protected String getIdPrefix() {
		return "component";
	}
	
	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#accept(name.martingeisse.guiserver.configuration.content.IComponentConfigurationVisitor)
	 */
	@Override
	public void accept(IComponentConfigurationVisitor visitor) {
		if (visitor.beginVisit(this)) {
			visitor.endVisit(this);
		}
	}
	
}
