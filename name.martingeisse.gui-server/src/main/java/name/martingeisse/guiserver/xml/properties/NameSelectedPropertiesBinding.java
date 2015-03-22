/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.properties;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.element.ElementParser;

import com.google.common.collect.ImmutableMap;

/**
 * This binding selects one of several bindings based on
 * the element name.
 */
public final class NameSelectedPropertiesBinding<C, P extends ElementParser<?>> implements PropertiesBinding<C, P> {

	/**
	 * the bindings
	 */
	private final Map<String, PropertiesBinding<C, ? extends P>> bindings;

	/**
	 * Constructor.
	 * @param bindings the bindings
	 */
	public NameSelectedPropertiesBinding(Map<String, ? extends PropertiesBinding<C, ? extends P>> bindings) {
		this.bindings = ImmutableMap.copyOf(bindings);
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
