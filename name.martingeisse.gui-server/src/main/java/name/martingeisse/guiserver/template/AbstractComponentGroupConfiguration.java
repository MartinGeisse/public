/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template;

import javax.xml.stream.XMLStreamException;

/**
 * Base class for component group configurations.
 */
public abstract class AbstractComponentGroupConfiguration implements ComponentGroupConfiguration {

	/**
	 * the baseId
	 */
	private String baseId;

	/**
	 * Constructor.
	 */
	public AbstractComponentGroupConfiguration() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		this.baseId = getBaseIdPrefix() + assembler.getComponentGroupAccumulatorSize();
		if (this instanceof IConfigurationSnippet) {
			IConfigurationSnippet snippet = (IConfigurationSnippet)this;
			snippet.setSnippetHandle(assembler.addSnippet(snippet));
		}
		assembler.addComponentGroup(this);
	}
	
	/**
	 * Returns the prefix for the component base ID. Subclasses may override this
	 * method to provide a prefix that gives a hint towards the meaning of the
	 * components, making the generated Wicket markup easier to understand
	 * (especially for debugging purposes).
	 * 
	 * The default base ID prefix is "component".
	 * 
	 * @return the component base ID prefix
	 */
	protected String getBaseIdPrefix() {
		return "component";
	}
	
	/**
	 * Getter method for the component base id.
	 * @return the id
	 */
	public final String getComponentBaseId() {
		return baseId;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#accept(name.martingeisse.guiserver.configuration.content.IComponentConfigurationVisitor)
	 */
	@Override
	public void accept(IComponentGroupConfigurationVisitor visitor) {
		if (visitor.beginVisit(this)) {
			visitor.endVisit(this);
		}
	}
	
}
