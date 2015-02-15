/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.content;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding;
import name.martingeisse.guiserver.xmlbind.result.MarkupContent;
import name.martingeisse.guiserver.xmlbind.result.MarkupContentEntry;

/**
 * Binds XML content to mixed raw/component markup.
 * 
 * @param <C> the type of components used in markup content
 */
public final class MarkupContentBinding<C> implements ElementObjectBinding<MarkupContent<C>> {

	/**
	 * the specialElementComponentBinding
	 */
	private final ElementObjectBinding<C> specialElementComponentBinding;

	/**
	 * Constructor.
	 * @param specialElementComponentBinding the special-element-to-component-configuration-binding
	 */
	public MarkupContentBinding(ElementObjectBinding<C> specialElementComponentBinding) {
		this.specialElementComponentBinding = specialElementComponentBinding;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public MarkupContent<C> parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		List<MarkupContentEntry<C>> entries = new ArrayList<MarkupContentEntry<C>>();
		int nesting = 0;
		loop: while (true) {
			switch (reader.getEventType()) {

			case XMLStreamConstants.START_ELEMENT:
				if (reader.getNamespaceURI() != null) {
					C component = specialElementComponentBinding.parse(reader);
					entries.add(new MarkupContentEntry.Component<C>(component));
				} else {
					MarkupContentEntry.Attribute[] attributes = new MarkupContentEntry.Attribute[reader.getAttributeCount()];
					for (int i=0; i<attributes.length; i++) {
						String attributeLocalName = reader.getAttributeLocalName(i);
						String attributeValue = reader.getAttributeValue(i);
						attributes[i] = new MarkupContentEntry.Attribute(attributeLocalName, attributeValue);
					}
					entries.add(new MarkupContentEntry.RawOpeningTag<C>(reader.getLocalName(), attributes));
					reader.next();
					nesting++;
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				if (nesting > 0) {
					entries.add(new MarkupContentEntry.RawClosingTag<C>());
					reader.next();
					nesting--;
					break;
				} else {
					break loop;
				}

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.ENTITY_REFERENCE:
				entries.add(new MarkupContentEntry.Characters<C>(reader.getText()));
				reader.next();
				break;

			case XMLStreamConstants.COMMENT:
				reader.next();
				break;
				
			default:
				throw new RuntimeException("invalid XML event while skipping nested content: " + reader.getEventType());

			}
		}
		return new MarkupContent<C>(listToArray(entries));
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private MarkupContentEntry<C>[] listToArray(List<MarkupContentEntry<C>> entries) {
		return entries.toArray((MarkupContentEntry<C>[])(new MarkupContentEntry<?>[entries.size()]));
	}

}
