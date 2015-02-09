/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.StringUtils;

/**
 * This parser is able to parse a list of objects, expressed as a sequence
 * of special elements with nothing but whitespace in between. The parser
 * expects to be invoked inside an enclosing element (i.e. after skipping
 * the opening tag of that element), and continues until it finds the
 * corresponding closing tag -- which is simply a closing tag for which it
 * did not see the opening tag.
 * 
 * Each contained element must be a special element, and the local names of
 * those elements can optionally be matched against a whitelist before passing
 * it to a subclass method.
 *
 * @param <C> the component type
 */
public abstract class ObjectListParser<C> implements INestedMarkupParser<C> {

	/**
	 * the localNameWhitelist
	 */
	private final Set<String> localNameWhitelist;
	
	/**
	 * Constructor.
	 * @param localNameWhitelist the whitelist for allowed local names for the contained elements.
	 * Note that if this array is empty, the whitelist is assumed to be nonexistent instead of
	 * empty.
	 */
	public ObjectListParser(String... localNameWhitelist) {
		this(buildSet(localNameWhitelist));
	}
	
	/**
	 * TODO check if there's something cool for this in Apache Commons
	 */
	private static Set<String> buildSet(String[] elements) {
		if (elements == null || elements.length == 0) {
			return null;
		}
		HashSet<String> result = new HashSet<>();
		for (String element : elements) {
			result.add(element);
		}
		return result;
	}
	
	/**
	 * Constructor.
	 * @param localNameWhitelist the whitelist for allowed local names for the contained elements
	 */
	public ObjectListParser(Collection<String> localNameWhitelist) {
		this(new HashSet<>(localNameWhitelist));
	}
	
	/**
	 * Constructor.
	 * @param localNameWhitelist the whitelist for allowed local names for the contained elements
	 */
	public ObjectListParser(Set<String> localNameWhitelist) {
		this.localNameWhitelist = localNameWhitelist;
	}


	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.INestedMarkupParser#parse(name.martingeisse.guiserver.xml.ContentStreams)
	 */
	@Override
	public final void parse(ContentStreams<C> streams) throws XMLStreamException {
		loop: while (true) {
			switch (streams.getReader().getEventType()) {

			case XMLStreamConstants.START_ELEMENT: {
				String localName = streams.recognizeStartSpecialElement();
				if (localName == null) {
					throw new RuntimeException("found raw markup in object list");
				}
				if (localNameWhitelist != null && !localNameWhitelist.contains(localName)) {
					throw new InvalidSpecialElementException("element not expected here: " + localName + ". Expected: " + StringUtils.join(localNameWhitelist, ", "));
				}
				handleSpecialElement(streams);
				break;
			}

			case XMLStreamConstants.END_ELEMENT: {
				break loop;
			}

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.ENTITY_REFERENCE:
			case XMLStreamConstants.COMMENT:
				streams.next();
				break;

			default:
				throw new RuntimeException("invalid XML event while parsing object list: " + streams.getReader().getEventType());

			}
		}
	}

	/**
	 * This method gets called when an opening special tag is encountered.
	 * It should process the input until the corresponding closing tag is
	 * found, then skip over it before returning.
	 * 
	 * Implementations can obtain the local name of the start element from the
	 * reader from the streams object. If a whitelist was passed to the
	 * constructor, then this method only gets called if the whitelist contains
	 * the local name.
	 * 
	 * @param streams the content streams
	 * @throws XMLStreamException on XML processing errors
	 */
	protected abstract void handleSpecialElement(ContentStreams<C> streams) throws XMLStreamException;

}
