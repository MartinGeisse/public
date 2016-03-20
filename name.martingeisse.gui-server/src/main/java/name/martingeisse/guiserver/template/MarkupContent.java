/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template;

import java.util.List;

import javax.xml.stream.XMLStreamException;

/**
 * A piece of properly nested markup that may contain components of type C.
 *
 * @param <C> the type of components used in markup content
 */
public final class MarkupContent<C extends ConfigurationAssemblerAcceptor<C>> {

	/**
	 * the entries
	 */
	private final MarkupContentEntry<C>[] entries;

	/**
	 * Constructor.
	 * @param entries the entries
	 */
	public MarkupContent(MarkupContentEntry<C>[] entries) {
		this.entries = entries;
	}

	/**
	 * Constructor for a list of components without raw markup.
	 * @param components the components
	 */
	@SuppressWarnings("unchecked")
	public MarkupContent(List<C> components) {
		this.entries = createArray(components.size());
		for (int i=0; i<components.size(); i++) {
			entries[i] = new MarkupContentEntry.ComponentGroup<C>(components.get(i));
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private MarkupContentEntry<C>[] createArray(int size) {
		return (MarkupContentEntry<C>[])(new MarkupContentEntry<?>[size]);
	}
	
	/**
	 * Assembles the configuration.
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	public void assemble(ConfigurationAssembler<C> assembler) throws XMLStreamException {
		for (MarkupContentEntry<C> entry : entries) {
			entry.assemble(assembler);
		}
	}

}
