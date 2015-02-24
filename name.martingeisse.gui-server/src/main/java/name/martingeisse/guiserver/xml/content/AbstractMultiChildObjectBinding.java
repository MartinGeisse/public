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
 * Parses (potentially) multiple child elements by mapping each child to an object,
 * then creates an object representing the result from those objects.
 * 
 * The default implementation is {@link MultiChildObjectBinding}, which uses a list
 * of the child objects as the result.
 */
public abstract class AbstractMultiChildObjectBinding<E, R> implements XmlContentObjectBinding<R> {

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
	private final ElementObjectBinding<E> childElementObjectBinding;

	/**
	 * Constructor.
	 * @param optional whether the child object is optional
	 * @param childObjectElementNameFilter the name filter for child object elements
	 * @param childElementObjectBinding the element-to-object binding for the child object 
	 */
	public AbstractMultiChildObjectBinding(boolean optional, String[] childObjectElementNameFilter, ElementObjectBinding<E> childElementObjectBinding) {
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
	public R parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		List<E> result = new ArrayList<>();
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
		return mapChildrenToResult(result);
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
	
	/**
	 * Maps the list of child objects (as parsed from child elements) to
	 * a result object.
	 * 
	 * @param children the children
	 * @return the result
	 */
	protected abstract R mapChildrenToResult(List<E> children);
	
}
