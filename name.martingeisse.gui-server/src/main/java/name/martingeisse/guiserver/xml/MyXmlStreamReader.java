/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.apache.commons.lang3.StringUtils;

/**
 * This class extends {@link XMLStreamReader} with some helper methods.
 */
public final class MyXmlStreamReader extends StreamReaderDelegate {

	/**
	 * Constructor.
	 * @param reader the wrapped XML stream reader
	 */
	public MyXmlStreamReader(XMLStreamReader reader) {
		super(reader);
	}

	/**
	 * Skips all pure-whitespace events. If any non-whitespace text events are encountered, this
	 * method throws an exception.
	 * 
	 * @throws XMLStreamException on XML processing errors
	 */
	public void skipWhitespace() throws XMLStreamException {
		while (true) {
			switch (getEventType()) {

			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.COMMENT:
				next();
				break;

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.ENTITY_REFERENCE: {
				String text = getText();
				if (!StringUtils.isBlank(text)) {
					throw new RuntimeException("expected pure whitespace, found '" + text.trim() + "'");
				}
				next();
				break;
			}
				
			default:
				return;
				
			}
		}
	}
	
}
