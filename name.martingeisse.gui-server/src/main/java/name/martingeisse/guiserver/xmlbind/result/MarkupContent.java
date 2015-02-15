/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.result;

import javax.xml.stream.XMLStreamException;

/**
 * A piece of properly nested markup that may contain components of type C.
 *
 * @param <C> the type of components used in markup content
 */
public final class MarkupContent<C> {

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
