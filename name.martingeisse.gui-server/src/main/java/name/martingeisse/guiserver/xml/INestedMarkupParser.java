/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import javax.xml.stream.XMLStreamException;

/**
 * Implementations can pick up parsing at some point and continue
 * until they encounter a closing tag for which they did not
 * see the opening tag.
 *
 * @param <R> the type of an additional return value returned by this parser
 */
public interface INestedMarkupParser<R> {

	/**
	 * Parses nested content.
	 * @param streams the streams used for parsing
	 * @return some return value that can be consumed by the calling parser
	 * @throws XMLStreamException on XML processing errors
	 */
	public R parse(ContentStreams streams) throws XMLStreamException;
	
}
