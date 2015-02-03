/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;

import com.google.common.collect.ImmutableList;

/**
 *
 */
public abstract class ContentParser {

	/**
	 * the streams
	 */
	private final ContentStreams streams;
	
	/**
	 * the componentCount
	 */
	private int componentCount;
	
	/**
	 * Constructor.
	 * 
	 * @param streams the content streams
	 */
	public ContentParser(ContentStreams streams) {
		this.streams = streams;
		this.componentCount = 0;
	}
	
	/**
	 * Getter method for the streams.
	 * @return the streams
	 */
	public final ContentStreams getStreams() {
		return streams;
	}
	
	/**
	 * Generates a new component ID.
	 * @param prefix the prefix to use for the ID
	 * @return the new component ID
	 */
	public String newComponentId(String prefix) {
		String id = prefix + componentCount;
		componentCount++;
		return id;
	}

	/**
	 * Handles content until a closing tag is encountered to which the start tag has already
	 * been handled, i.e. this method only parses a snippet of properly nested content.
	 *
	 * Any special GUI element encountered is passed to handleSpecialElement().
	 */
	public void parseNestedContent() throws XMLStreamException {
		int nesting = 0;
		loop: while (true) {
			switch (streams.getReader().getEventType()) {

			case XMLStreamConstants.START_ELEMENT: {
				String localName = streams.recognizeStartSpecialElement();
				if (localName != null) {
					handleSpecialElement(localName);
				} else {
					streams.copyEvent();
					streams.next();
					nesting++;
				}
				break;
			}

			case XMLStreamConstants.END_ELEMENT: {
				if (nesting > 0) {
					streams.copyEvent();
					streams.next();
					nesting--;
					break;
				} else {
					break loop;
				}
			}

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.ENTITY_REFERENCE:
				streams.copyEvent();
				streams.next();
				break;

			default:
				throw new RuntimeException("invalid XML event: " + streams.getReader().getEventType());

			}
		}
	}

	/**
	 * Parses the content of a component. This is similar to {@link #parseNestedContent()},
	 * except that it uses a new component accumulator for the contents and returns the
	 * elements of that accumulator.
	 */
	public ImmutableList<ComponentConfiguration> parseComponentContent() throws XMLStreamException {
		streams.beginComponentAccumulator();
		parseNestedContent();
		return streams.finishComponentAccumulator();
	}

	/**
	 * Handles a special GUI element.
	 */
	protected abstract void handleSpecialElement(String localName) throws XMLStreamException;
	
}
