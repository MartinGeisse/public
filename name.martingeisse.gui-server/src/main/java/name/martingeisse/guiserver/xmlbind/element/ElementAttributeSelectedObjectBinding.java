/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.element;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader;

/**
 * This element-to-object binding selects one of several bindings based on
 * the value of an attribute.
 */
public final class ElementAttributeSelectedObjectBinding<T> implements ElementObjectBinding<T> {

	/**
	 * the attributeName
	 */
	private final String attributeName;

	/**
	 * the bindings
	 */
	private final Map<String, ElementObjectBinding<? extends T>> bindings;

	/**
	 * Constructor.
	 * @param attributeName the name of the attribute
	 * @param bindings the bindings
	 */
	public ElementAttributeSelectedObjectBinding(String attributeName, Map<String, ElementObjectBinding<? extends T>> bindings) {
		this.attributeName = attributeName;
		this.bindings = bindings;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public T parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		String attributeValue = reader.getAttributeValue(null, attributeName);
		if (attributeValue == null) {
			throw new RuntimeException("missing '" + attributeName + "' attribute");
		}
		ElementObjectBinding<? extends T> selectedBinding = bindings.get(attributeValue);
		if (selectedBinding == null) {
			throw new RuntimeException("unknown value for '" + attributeName + "' attribute: " + reader.getLocalName());
		}
		return selectedBinding.parse(reader);
	}

}
