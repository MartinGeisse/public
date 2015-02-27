/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.result;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.configuration.content.IConfigurationSnippet;
import name.martingeisse.guiserver.xml.ConfigurationAssemblerAcceptor;

/**
 * Helper class that takes component-enhanced markup and
 * creates the final component/markup/snippet configuration from
 * that.
 *
 * @param <CG> the component group type
 */
public final class ConfigurationAssembler<CG extends ConfigurationAssemblerAcceptor<CG>> {

	/**
	 * the markupWriter
	 */
	private final XMLStreamWriter markupWriter;

	/**
	 * the componentGroupAccumulator
	 */
	private final List<CG> componentGroupAccumulator;

	/**
	 * the snippetAccumulator
	 */
	private final List<IConfigurationSnippet> snippetAccumulator;

	/**
	 * Constructor.
	 * @param markupWriter the object used to write Wicket markup
	 * @param componentGroupAccumulator a list that accumulates the component configurations
	 * @param snippetAccumulator a list that accumulates globally indexed configuration snippets
	 */
	public ConfigurationAssembler(XMLStreamWriter markupWriter, List<CG> componentGroupAccumulator, List<IConfigurationSnippet> snippetAccumulator) {
		this.markupWriter = markupWriter;
		this.componentGroupAccumulator = componentGroupAccumulator;
		this.snippetAccumulator = snippetAccumulator;
	}

	/**
	 * Getter method for the markupWriter.
	 * @return the markupWriter
	 */
	public XMLStreamWriter getMarkupWriter() {
		return markupWriter;
	}
	
	/**
	 * Returns the size of the component group accumulator, i.e. the number of
	 * components accumulated so far.
	 * 
	 * @return the component group accumulator size
	 */
	public int getComponentGroupAccumulatorSize() {
		return componentGroupAccumulator.size();
	}
	
	/**
	 * Adds a component to the component group accumulator
	 * @param component the component to add
	 */
	public void addComponentGroup(CG component) {
		componentGroupAccumulator.add(component);
	}

	/**
	 * Adds a snippet and returns its handle.
	 * @param snippet the snippet to add
	 * @return the handle
	 */
	public int addSnippet(IConfigurationSnippet snippet) {
		int handle = snippetAccumulator.size();
		snippetAccumulator.add(snippet);
		return handle;
	}
	
	/**
	 * Creates an assembler like this one but with a different component group accumulator.
	 * 
	 * @param componentGroupAccumulator the component group accumulator to use
	 * @return the new assembler
	 */
	public ConfigurationAssembler<CG> withComponentGroupAccumulator(List<CG> newComponentGroupAccumulator) {
		return new ConfigurationAssembler<>(markupWriter, newComponentGroupAccumulator, snippetAccumulator);
	}
	
}
