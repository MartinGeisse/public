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
	 * This method should only be called directly after creating the reader. It checks that
	 * the document element is a special element with the specified local name and skips over
	 * the opening tag, otherwise throws an exception.
	 * 
	 * @param expectedLocalName the expected local name
	 */
	public void expectSpecialDocumentElement(String expectedLocalName) throws XMLStreamException {
		if (getEventType() != XMLStreamConstants.START_DOCUMENT) {
			throw new IllegalStateException("reader is not at the START_DOCUMENT");
		}
		next();
		if (getNamespaceURI() == null || !getLocalName().equals(expectedLocalName)) {
			throw new RuntimeException("invalid document element, expected special element " + expectedLocalName);
		}
		next();
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
