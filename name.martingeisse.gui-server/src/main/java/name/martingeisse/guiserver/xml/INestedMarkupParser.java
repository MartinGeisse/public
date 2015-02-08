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
 * @param <C> the component type
 */
public interface INestedMarkupParser<C> {

	/**
	 * Parses nested content.
	 * 
	 * @param streams the streams used for parsing
	 * @throws XMLStreamException on XML processing errors
	 */
	public void parse(ContentStreams<C> streams) throws XMLStreamException;
	
}
