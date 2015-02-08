/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

/**
 * This is the common {@link INestedMarkupParser} implementation. It handles
 * the usual "raw markup with embedded components". Any special elements are
 * passed to a set of named {@link IElementParser}s. If none matches, the
 * element gets passed to a subclass method.
 *
 * @param <C> the component type
 */
public class MixedNestedMarkupParser<C> implements INestedMarkupParser<C> {

	/**
	 * the specialElementParsers
	 */
	private final Map<String, IElementParser<C>> specialElementParsers = new HashMap<>();
	
	/**
	 * Adds a parser for a special element.
	 * @param localName the local name of the special element
	 * @param parser the parser to add
	 */
	public final void addSpecialElementParser(String localName, IElementParser<C> parser) {
		specialElementParsers.put(localName, parser);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.INestedMarkupParser#parse(name.martingeisse.guiserver.xml.ContentStreams)
	 */
	@Override
	public final void parse(ContentStreams<C> streams) throws XMLStreamException {
		int nesting = 0;
		loop: while (true) {
			switch (streams.getReader().getEventType()) {

			case XMLStreamConstants.START_ELEMENT: {
				String localName = streams.recognizeStartSpecialElement();
				if (localName != null) {
					handleSpecialElementInParserOrSubclass(streams);
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
				
			case XMLStreamConstants.COMMENT:
				streams.next();
				break;

			default:
				throw new RuntimeException("invalid XML event while parsing nested content: " + streams.getReader().getEventType());

			}
		}
	}

	/**
	 * This method gets called when an opening special tag is encountered.
	 * It looks for a matching registered parser, and delegates to the
	 * {@link #handleSpecialElement(ContentStreams)} method if none can be
	 * found.
	 * 
	 * This method should process the input until the corresponding closing
	 * tag is found, then skip over it before returning.
	 * 
	 * @param streams the content streams
	 * @throws XMLStreamException on XML processing errors
	 */
	protected final void handleSpecialElementInParserOrSubclass(ContentStreams<C> streams) throws XMLStreamException {
		String localName = streams.getReader().getLocalName();
		IElementParser<C> parser = specialElementParsers.get(localName);
		if (parser != null) {
			parser.parse(streams);
		} else {
			handleSpecialElement(streams);
		}
	}
	
	/**
	 * This method gets called when an opening special tag is encountered for
	 * which no {@link IElementParser} is registered. It should process the
	 * input until the corresponding closing tag is found, then skip over
	 * it before returning.
	 * 
	 * Implementations can obtain the local name of the start element from the
	 * reader from the streams object.
	 * 
	 * The default implementation throws an {@link InvalidSpecialElementException}.
	 * 
	 * @param streams the content streams
	 * @throws XMLStreamException on XML processing errors
	 */
	protected void handleSpecialElement(ContentStreams<C> streams) throws XMLStreamException {
		throw new InvalidSpecialElementException("unknown special element: " + streams.getReader().getLocalName());
	}

}
