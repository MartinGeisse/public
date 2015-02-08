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
 * @param <R> the type of an additional return value returned by this parser
 */
public interface IElementParser<R> {

	/**
	 * Parses nested content.
	 * @param streams the streams used for parsing
	 * @return some return value that can be consumed by the calling parser
	 * @throws XMLStreamException on XML processing errors
	 */
	public R parse(ContentStreams streams) throws XMLStreamException;
	
}
