/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.element;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader;

import com.google.common.collect.ImmutableMap;

/**
 * This element-to-object binding selects one of several bindings based on
 * the element name.
 */
public final class ElementNameSelectedObjectBinding<T> implements ElementObjectBinding<T> {

	/**
	 * the bindings
	 */
	private final Map<String, ElementObjectBinding<? extends T>> bindings;

	/**
	 * Constructor.
	 * @param bindings the bindings
	 */
	public ElementNameSelectedObjectBinding(Map<String, ElementObjectBinding<? extends T>> bindings) {
		this.bindings = ImmutableMap.copyOf(bindings);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public T parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		ElementObjectBinding<? extends T> selectedBinding = bindings.get(reader.getLocalName());
		if (selectedBinding == null) {
			throw new RuntimeException("unknown element: " + reader.getLocalName());
		}
		return selectedBinding.parse(reader);
	}

}
