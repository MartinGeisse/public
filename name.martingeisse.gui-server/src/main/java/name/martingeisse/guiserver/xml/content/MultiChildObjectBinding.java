/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.content;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xml.element.ElementObjectBinding;

/**
 * Binds XML content to potentially multiple child objects of the object to which the enclosing element
 * was bound.
 */
public final class MultiChildObjectBinding<T> implements XmlContentObjectBinding<List<T>> {

	/**
	 * the optional
	 */
	private final boolean optional;

	/**
	 * the childElementFilter
	 */
	private final String[] childObjectElementNameFilter;

	/**
	 * the childElementObjectBinding
	 */
	private final ElementObjectBinding<T> childElementObjectBinding;

	/**
	 * Constructor.
	 * @param optional whether the child object is optional
	 * @param childObjectElementNameFilter the name filter for child object elements
	 * @param childElementObjectBinding the element-to-object binding for the child object 
	 */
	public MultiChildObjectBinding(boolean optional, String[] childObjectElementNameFilter, ElementObjectBinding<T> childElementObjectBinding) {
		if (childElementObjectBinding == null) {
			throw new IllegalArgumentException("childElementObjectBinding argument is null");
		}
		this.optional = optional;
		this.childObjectElementNameFilter = childObjectElementNameFilter;
		this.childElementObjectBinding = childElementObjectBinding;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public List<T> parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		List<T> result = new ArrayList<>();
		while (true) {
			reader.skipWhitespace();
			if (reader.getEventType() == XMLStreamConstants.END_ELEMENT) {
				break;
			} else if (reader.getEventType() != XMLStreamConstants.START_ELEMENT) {
				throw new RuntimeException("unexpected content");
			} else if (!isAcceptedByFilter(reader.getLocalName())) {
				throw new RuntimeException("unexpected element: " + reader.getLocalName());
			}
			result.add(childElementObjectBinding.parse(reader));
		}
		if (!optional && result.isEmpty()) {
			throw new RuntimeException("expected at least one child element");
		}
		return result;
	}

	/**
	 * 
	 */
	private boolean isAcceptedByFilter(String localName) {
		if (childObjectElementNameFilter == null) {
			return true;
		}
		for (String filterElement : childObjectElementNameFilter) {
			if (filterElement.equals(localName)) {
				return true;
			}
		}
		return false;
	}
	
}
