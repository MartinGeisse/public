/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.result;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.configuration.content.IConfigurationSnippet;

/**
 * Helper class that takes component-enhanced markup and
 * creates the final component/markup/snippet configuration from
 * that.
 *
 * @param <C> the component type
 */
public final class ConfigurationAssembler<C> {

	/**
	 * the markupWriter
	 */
	private final XMLStreamWriter markupWriter;

	/**
	 * the componentAccumulator
	 */
	private final List<C> componentAccumulator;

	/**
	 * the snippetAccumulator
	 */
	private final List<IConfigurationSnippet> snippetAccumulator;

	/**
	 * Constructor.
	 * @param markupWriter the object used to write Wicket markup
	 * @param componentAccumulator a list that accumulates the component configurations
	 * @param snippetAccumulator a list that accumulates globally indexed configuration snippets
	 */
	public ConfigurationAssembler(XMLStreamWriter markupWriter, List<C> componentAccumulator, List<IConfigurationSnippet> snippetAccumulator) {
		this.markupWriter = markupWriter;
		this.componentAccumulator = componentAccumulator;
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
	 * Adds a component to the component accumulator
	 * @param component the component to add
	 */
	public void addComponent(C component) {
		componentAccumulator.add(component);
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
	 * Creates an assembler like this one but with a different component accumulator.
	 * 
	 * @param componentAccumulator the component accumulator to use
	 * @return the new assembler
	 */
	public ConfigurationAssembler<C> withComponentAccumulator(List<C> newComponentAccumulator) {
		return new ConfigurationAssembler<>(markupWriter, newComponentAccumulator, snippetAccumulator);
	}
	
}
