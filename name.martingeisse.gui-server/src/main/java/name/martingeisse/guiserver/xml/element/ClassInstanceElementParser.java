/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.element;

import java.lang.reflect.Constructor;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.attribute.AttributeParser;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.properties.PropertiesBinding;

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
	 * the attributeBindings
	 */
	private final PropertiesBinding<T, ? extends AttributeParser<?>>[] attributeBindings;

	/**
	 * the contentParser
	 */
	private final PropertiesBinding<T, ? extends ContentParser<?>> contentBinding;

	/**
	 * Constructor.
	 * @param constructor the constructor of the target class to call
	 * @param attributeBindings the attribute bindings
	 * @param contentBinding the content binding, or null if no content is allowed for the element
	 */
	public ClassInstanceElementParser(Constructor<? extends T> constructor, PropertiesBinding<T, ? extends AttributeParser<?>>[] attributeBindings, PropertiesBinding<T, ? extends ContentParser<?>> contentBinding) {
		this.constructor = constructor;
		this.attributeBindings = attributeBindings;
		this.contentBinding = contentBinding;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.element.ElementParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader)
	 */
	@Override
	public final T parse(MyXmlStreamReader reader) throws XMLStreamException {
		String elementLocalName = reader.getLocalName();
		T instance;
		try {
			instance = constructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (PropertiesBinding<T, ? extends AttributeParser<?>> attributeBinding : attributeBindings) {
			attributeBinding.bind(reader, instance);
		}
		reader.next();
		if (contentBinding == null) {
			reader.skipWhitespace();
			if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
				throw new RuntimeException("unexpected content in element " + elementLocalName);
			}
		} else {
			contentBinding.bind(reader, instance);
		}
		reader.next();
		return instance;
	}

}
