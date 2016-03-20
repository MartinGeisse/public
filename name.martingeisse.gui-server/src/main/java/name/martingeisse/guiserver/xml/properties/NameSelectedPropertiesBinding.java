/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.properties;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.element.ElementParser;

/**
 * This binding selects one of several bindings based on
 * the element name.
 */
public final class NameSelectedPropertiesBinding<C, P extends ElementParser<?>> implements PropertiesBinding<C, P> {

	/**
	 * the bindings
	 */
	private final Map<String, PropertiesBinding<C, ? extends P>> bindings = new HashMap<>();

	/**
	 * Adds a properties binding.
	 * 
	 * @param localElementName the local element name that selects the specified binding
	 * @param binding the binding to invoke for that element name
	 */
	public void addBinding(String localElementName, PropertiesBinding<C, ? extends P> binding) {
		bindings.put(localElementName, binding);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.properties.PropertiesBinding#bind(name.martingeisse.guiserver.xml.MyXmlStreamReader, java.lang.Object)
	 */
	@Override
	public void bind(MyXmlStreamReader reader, C target) throws XMLStreamException {
		PropertiesBinding<C, ? extends P> selectedBinding = bindings.get(reader.getLocalName());
		if (selectedBinding == null) {
			throw new RuntimeException("unknown property element: " + reader.getLocalName());
		}
		selectedBinding.bind(reader, target);
	}

}
