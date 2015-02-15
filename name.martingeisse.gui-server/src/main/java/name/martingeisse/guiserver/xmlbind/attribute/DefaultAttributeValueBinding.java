/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.attribute;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xmlbind.value.TextValueBinding;

/**
 * The default implementation of {@link AttributeValueBinding}. This class
 * reads an attribute with a fixed name and uses a {@link TextValueBinding}
 * to parse its value.
 * 
 * Note that the default value is specified in the same format as in the
 * XML, that is, the default value gets parsed just as any other value.
 *
 * @param <T> the attribute value type
 */
public final class DefaultAttributeValueBinding<T> implements AttributeValueBinding<T> {

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
	 * the textValueBinding
	 */
	private final TextValueBinding<T> textValueBinding;

	/**
	 * the parsedDefaultValue
	 */
	private final T parsedDefaultValue;

	/**
	 * Constructor for a mandatory attribute.
	 * 
	 * @param name the attribute name
	 * @param textValueBinding the binding for the attribute value
	 */
	public DefaultAttributeValueBinding(String name, TextValueBinding<T> textValueBinding) {
		this(name, false, textValueBinding);
	}

	/**
	 * Constructor
	 * .
	 * @param name the attribute name
	 * @param optional whether the attribute is optional
	 * @param textValueBinding the binding for the attribute value
	 */
	public DefaultAttributeValueBinding(String name, boolean optional, TextValueBinding<T> textValueBinding) {
		this(name, optional, null, textValueBinding);
	}

	/**
	 * Constructor.
	 * @param name the attribute name
	 * @param optional whether the attribute is optional
	 * @param defaultValue the default value to use if the attribute is optional and absent
	 * @param textValueBinding the binding for the attribute value
	 */
	public DefaultAttributeValueBinding(String name, boolean optional, String defaultValue, TextValueBinding<T> textValueBinding) {
		if (name == null) {
			throw new IllegalArgumentException("name argument is null");
		}
		if (textValueBinding == null) {
			throw new IllegalArgumentException("textValueBinding argument is null");
		}
		this.name = name;
		this.optional = optional;
		this.defaultValue = defaultValue;
		try {
			this.parsedDefaultValue = (defaultValue == null ? null : textValueBinding.parse(defaultValue));
		} catch (Exception e) {
			throw new RuntimeException("failed to parse default value for attribute " + name, e);
		}
		this.textValueBinding = textValueBinding;
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
	 * Getter method for the textValueBinding.
	 * @return the textValueBinding
	 */
	public TextValueBinding<T> getTextValueBinding() {
		return textValueBinding;
	}

	/**
	 * Getter method for the parsedDefaultValue.
	 * @return the parsedDefaultValue
	 */
	public T getParsedDefaultValue() {
		return parsedDefaultValue;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.attribute.AttributeValueBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public T parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		String specifiedValue = reader.getAttributeValue(null, name);
		if (specifiedValue == null) {
			if (optional) {
				return parsedDefaultValue;
			} else {
				throw new MissingAttributeException(name);
			}
		} else {
			return textValueBinding.parse(specifiedValue);
		}
	}

}
