/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.element.ElementParser;

/**
 * Parses XML content to produce mixed raw/component markup.
 * 
 * This parser delegates to a configurable {@link ElementParser} to handle component elements.
 * 
 * @param <C> the type of components used in markup content
 */
public final class MarkupContentParser<C extends ConfigurationAssemblerAcceptor<C>> implements ContentParser<MarkupContent<C>> {

	/**
	 * the specialElementComponentParser
	 */
	private ElementParser<C> specialElementComponentParser;

	/**
	 * Constructor.
	 */
	public MarkupContentParser() {
	}

	/**
	 * Constructor.
	 * @param specialElementComponentParser the special-element-to-component-configuration-parser
	 */
	public MarkupContentParser(ElementParser<C> specialElementComponentParser) {
		this.specialElementComponentParser = specialElementComponentParser;
	}

	/**
	 * Getter method for the specialElementComponentParser.
	 * @return the specialElementComponentParser
	 */
	public ElementParser<C> getSpecialElementComponentParser() {
		return specialElementComponentParser;
	}

	/**
	 * Setter method for the specialElementComponentParser.
	 * @param specialElementComponentParser the specialElementComponentParser to set
	 */
	public void setSpecialElementComponentParser(ElementParser<C> specialElementComponentParser) {
		this.specialElementComponentParser = specialElementComponentParser;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.content.ContentParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader)
	 */
	@Override
	public MarkupContent<C> parse(MyXmlStreamReader reader) throws XMLStreamException {
		if (specialElementComponentParser == null) {
			throw new IllegalStateException("component element parser not set");
		}
		List<MarkupContentEntry<C>> entries = new ArrayList<MarkupContentEntry<C>>();
		int nesting = 0;
		loop: while (true) {
			switch (reader.getEventType()) {

			case XMLStreamConstants.START_ELEMENT:
				if (reader.getNamespaceURI() != null) {
					C component = specialElementComponentParser.parse(reader);
					entries.add(new MarkupContentEntry.ComponentGroup<C>(component));
				} else {
					MarkupContentEntry.Attribute[] attributes = new MarkupContentEntry.Attribute[reader.getAttributeCount()];
					for (int i = 0; i < attributes.length; i++) {
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
