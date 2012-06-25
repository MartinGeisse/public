/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import org.xml.sax.Attributes;

/**
 * Implementations are able to create a parser state, given the XML element
 * namespace/name and attributes as well as the expected return type of the
 * parent state. 
 */
public interface IParserStateFactory {

	/**
	 * Creates a parser state.
	 * @param adapterProvider the adapter provider that can be used to create return
	 * type adapters in case the expected return type cannot be directly supported.
	 * @param expectedReturnType the return type expected by the parent state
	 * @param namespaceUri the namespace URI of the element that started the state
	 * @param name the local name of the element that started the state
	 * @param attributes the attributes of the element that started the state
	 * @return the parser state
	 */
	public IParserState createParserState(IParserReturnTypeAdapterProvider adapterProvider, Class<?> expectedReturnType, String namespaceUri, String name, Attributes attributes);
	
}
