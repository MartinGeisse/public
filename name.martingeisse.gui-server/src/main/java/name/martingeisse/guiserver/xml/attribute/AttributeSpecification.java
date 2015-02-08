/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.attribute;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Specifies a known attribute in the XML format.
 */
public final class AttributeSpecification {

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
	private final Object defaultValue;
	
	/**
	 * the parser
	 */
	private final IAttributeParser parser;

	/**
	 * Constructor for a mandatory attribute.
	 * 
	 * @param name the attribute name
	 * @param parser the parser for the attribute
	 */
	public AttributeSpecification(String name, IAttributeParser parser) {
		this(name, false, parser);
	}
	
	/**
	 * Constructor
	 * .
	 * @param name the attribute name
	 * @param optional whether the attribute is optional
	 * @param parser the parser for the attribute
	 */
	public AttributeSpecification(String name, boolean optional, IAttributeParser parser) {
		this(name, optional, null, parser);
	}
	
	/**
	 * Constructor.
	 * @param name the attribute name
	 * @param optional whether the attribute is optional
	 * @param defaultValue the default value to use if the attribute is optional and absent
	 * @param parser the parser for the attribute
	 */
	public AttributeSpecification(String name, boolean optional, Object defaultValue, IAttributeParser parser) {
		this.name = name;
		this.optional = optional;
		this.defaultValue = defaultValue;
		this.parser = parser;
	}
	
	/**
	 * Parses the attribute from the specified reader. The reader must be located
	 * at a START_ELEMENT. This method won't move the reader.
	 * 
	 * @param reader the reader
	 * @return the parsed attribute value
	 * @throws XMLStreamException on XML processing errors
	 */
	public Object parse(XMLStreamReader reader) throws XMLStreamException {
		String specifiedValue = reader.getAttributeValue(null, name);
		if (specifiedValue == null) {
			if (optional) {
				return defaultValue;
			} else {
				throw new MissingAttributeException(name);
			}
		} else {
			return parser.parse(specifiedValue);
		}
	}

}
