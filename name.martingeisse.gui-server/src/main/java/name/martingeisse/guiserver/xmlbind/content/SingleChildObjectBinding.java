/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.content;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.StringUtils;

import name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding;

/**
 * Binds XML content to zero or one child of the object to which the enclosing element
 * was bound.
 */
public final class SingleChildObjectBinding<T> implements ElementObjectBinding<T> {

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
	public SingleChildObjectBinding(boolean optional, String[] childObjectElementNameFilter, ElementObjectBinding<T> childElementObjectBinding) {
		this.optional = optional;
		this.childObjectElementNameFilter = childObjectElementNameFilter;
		this.childElementObjectBinding = childElementObjectBinding;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public T parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		reader.skipWhitespace();
		if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
			if (!isAcceptedByFilter(reader.getLocalName())) {
				throw new RuntimeException("unexpected element: " + reader.getLocalName());
			}
			T result = childElementObjectBinding.parse(reader);
			reader.skipWhitespace();
			if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
				throw new RuntimeException("only a single child element expected");
			}
			return result;
		} else if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
			throw new RuntimeException("unexpected content");
		} else if (optional) {
			return null;
		} else {
			if (childObjectElementNameFilter == null) {
				throw new RuntimeException("expected child element");
			} else {
				throw new RuntimeException("expected child element of any of these types: " + StringUtils.join(childObjectElementNameFilter));
			}
		}
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
