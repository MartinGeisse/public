/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.attribute;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.value.ValueParser;

/**
 * The default implementation of {@link AttributeParser}. This class
 * reads a single attribute with a fixed name and uses a
 * {@link ValueParser} to parse its value.
 * 
 * Note that the default value is specified in the same format as in the
 * XML, that is, the default value gets parsed just as any other value.
 * This is because of the strongly restricted types that can be used in
 * the default value property of {@link BindAttribute} annotations.
 *
 * @param <T> the attribute value type
 */
public final class SimpleAttributeParser<T> implements AttributeParser<T> {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * the optional
	 */
	private final boolean optional;

	/**
	 * the defaultValue
	 */
	private final String defaultValue;

	/**
	 * the valueParser
	 */
	private final ValueParser<T> valueParser;

	/**
	 * the parsedDefaultValue
	 */
	private final T parsedDefaultValue;

	/**
	 * Constructor for a mandatory attribute.
	 * 
	 * @param name the attribute name
	 * @param valueParser the parser for the attribute value
	 */
	public SimpleAttributeParser(String name, ValueParser<T> valueParser) {
		this(name, false, valueParser);
	}

	/**
	 * Constructor
	 * .
	 * @param name the attribute name
	 * @param optional whether the attribute is optional
	 * @param valueParser the parser for the attribute value
	 */
	public SimpleAttributeParser(String name, boolean optional, ValueParser<T> valueParser) {
		this(name, optional, null, valueParser);
	}

	/**
	 * Constructor.
	 * @param name the attribute name
	 * @param optional whether the attribute is optional
	 * @param defaultValue the default value to use if the attribute is optional and absent
	 * @param valueParser the parser for the attribute value
	 */
	public SimpleAttributeParser(String name, boolean optional, String defaultValue, ValueParser<T> valueParser) {
		if (name == null) {
			throw new IllegalArgumentException("name argument is null");
		}
		if (valueParser == null) {
			throw new IllegalArgumentException("valueParser argument is null");
		}
		this.name = name;
		this.optional = optional;
		this.defaultValue = defaultValue;
		try {
			this.parsedDefaultValue = (defaultValue == null ? null : valueParser.parse(defaultValue));
		} catch (Exception e) {
			throw new RuntimeException("failed to parse default value for attribute " + name, e);
		}
		this.valueParser = valueParser;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the optional.
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * Getter method for the defaultValue.
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Getter method for the valueParser.
	 * @return the valueParser
	 */
	public ValueParser<T> getValueParser() {
		return valueParser;
	}

	/**
	 * Getter method for the parsedDefaultValue.
	 * @return the parsedDefaultValue
	 */
	public T getParsedDefaultValue() {
		return parsedDefaultValue;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.attribute.AttributeParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader)
	 */
	@Override
	public T parse(MyXmlStreamReader reader) throws XMLStreamException {
		String specifiedValue = reader.getAttributeValue(null, name);
		if (specifiedValue == null) {
			if (optional) {
				return parsedDefaultValue;
			} else {
				throw new MissingAttributeException(name);
			}
		} else {
			return valueParser.parse(specifiedValue);
		}
	}

}
