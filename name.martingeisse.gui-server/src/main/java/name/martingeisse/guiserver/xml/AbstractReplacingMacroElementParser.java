/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import name.martingeisse.guiserver.xml.attribute.AttributeSpecification;

/**
 * Parses a special element that is just a "macro" for a piece of raw markup.
 * The contents of the special element are discarded and replaced by the
 * markup from the macro.
 * 
 * This class provides attribute parsing for its subclasses.
 *
 * @param <C> the component type
 */
public abstract class AbstractReplacingMacroElementParser<C> implements IElementParser<C> {

	/**
	 * the attributeSpecifications
	 */
	private final AttributeSpecification[] attributeSpecifications;
	
	/**
	 * Constructor.
	 * @param attributeSpecifications the attribute specification
	 */
	public AbstractReplacingMacroElementParser(AttributeSpecification... attributeSpecifications) {
		this.attributeSpecifications = attributeSpecifications;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.IElementParser#parse(name.martingeisse.guiserver.xml.ContentStreams)
	 */
	@Override
	public final void parse(ContentStreams<C> streams) throws XMLStreamException {
		XMLStreamReader reader = streams.getReader();

		// read and skip over the element
		Object[] attributeValues = new Object[attributeSpecifications.length];
		for (int i=0; i<attributeSpecifications.length; i++) {
			attributeValues[i] = attributeSpecifications[i].parse(reader);
		}
		reader.next();
		streams.skipNestedContent();
		reader.next();

		// expand the macro
		writeMarkup(streams, attributeValues);
		
	}

	/**
	 * Writes the output markup.
	 * 
	 * @param streams the content streams
	 * @param attributeValues the parsed attributes from the "invoking" element
	 * @throws XMLStreamException on XML processing errors
	 */
	protected abstract void writeMarkup(ContentStreams<C> streams, Object[] attributeValues) throws XMLStreamException;

}
