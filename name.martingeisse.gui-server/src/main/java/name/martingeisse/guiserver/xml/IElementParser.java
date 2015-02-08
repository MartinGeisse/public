/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import javax.xml.stream.XMLStreamException;

/**
 * Implementations can pick up parsing at a starting tag and
 * continue until they encounter the corresponding closing tag.
 * This parser will skip that final closing tag before returning.
 *
 * @param <C> the component type
 */
public interface IElementParser<C> {

	/**
	 * Parses nested content.
	 * 
	 * @param streams the streams used for parsing
	 * @throws XMLStreamException on XML processing errors
	 */
	public void parse(ContentStreams<C> streams) throws XMLStreamException;
	
}
