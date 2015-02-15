/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler;

/**
 * This interface is implemented by objects which are able to assemble (parts of)
 * the configuration using a configuration assembler.
 *
 * @param <C> the concrete subtype of this interface
 */
public interface ConfigurationAssemblerAcceptor<C extends ConfigurationAssemblerAcceptor<C>> {

	/**
	 * Assembles the configuration.
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	public abstract void assemble(ConfigurationAssembler<C> assembler) throws XMLStreamException;

}
