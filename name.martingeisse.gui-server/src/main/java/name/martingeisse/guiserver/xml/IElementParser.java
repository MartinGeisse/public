/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import javax.xml.stream.XMLStreamException;

/**
 * Implementations can pick up parsing at a starting tag and
 * continue until they encounter the corresponding closing tag.
 * This parser will skip that final closing tag before returning.
 */
public interface IElementParser {

	/**
	 * Parses nested content.
	 * 
	 * @param streams the streams used for parsing
	 * @throws XMLStreamException on XML processing errors
	 */
	public void parse(ContentStreams streams) throws XMLStreamException;
	
}
