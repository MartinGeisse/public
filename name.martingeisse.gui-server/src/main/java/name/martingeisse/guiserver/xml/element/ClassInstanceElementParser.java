/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.element;

import java.lang.reflect.Constructor;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.attribute.AttributeParser;
import name.martingeisse.guiserver.xml.content.ContentParser;

/**
 * Parses an XML element by creating an instance of a Java class.
 * This parser then accepts attributes and child elements to retrieve
 * properties of that instance.
 */
public class ClassInstanceElementParser<T> implements ElementParser<T> {

	/**
	 * the constructor
	 */
	private final Constructor<? extends T> constructor;

	/**
	 * the attributeParsers
	 */
	private final AttributeParser<?>[] attributeParsers;

	/**
	 * the contentParser
	 */
	private final ContentParser<?> contentParser;

	/**
	 * Constructor.
	 * @param constructor the constructor of the target class to call
	 * @param attributeParsers the attribute parsers
	 * @param contentParser the content parser, or null if no content is allowed for the element
	 */
	public ClassInstanceElementParser(Constructor<? extends T> constructor, AttributeParser<?>[] attributeParsers, ContentParser<?> contentParser) {

		// assign fields
		this.constructor = constructor;
		this.attributeParsers = attributeParsers;
		this.contentParser = contentParser;

		// make sure that the constructor matches our expectations
		Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
		if (constructorParameterTypes.length != getExpectedArgumentCount()) {
			throw new RuntimeException("number of constructor arguments doesn't match parser annotation for class " + constructor.getDeclaringClass());
		}

	}

	/**
	 * 
	 */
	private int getExpectedArgumentCount() {
		return attributeParsers.length + (contentParser == null ? 0 : 1);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.element.ElementParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader)
	 */
	@Override
	public final T parse(MyXmlStreamReader reader) throws XMLStreamException {
		String elementLocalName = reader.getLocalName();

		// create the constructor argument array
		int argumentCount = getExpectedArgumentCount();
		Object[] arguments = new Object[argumentCount];

		// parse attribute values
		for (int i = 0; i < attributeParsers.length; i++) {
			arguments[i] = attributeParsers[i].parse(reader);
		}

		// parse element content (if supported)
		reader.next();
		if (contentParser == null) {
			reader.skipWhitespace();
			if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
				throw new RuntimeException("unexpected content in element " + elementLocalName);
			}
		} else {
			arguments[arguments.length - 1] = contentParser.parse(reader);
		}
		reader.next();

		// create the instance
		try {
			return constructor.newInstance(arguments);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
