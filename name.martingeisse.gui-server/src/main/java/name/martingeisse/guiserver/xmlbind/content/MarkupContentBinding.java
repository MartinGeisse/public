/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.content;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xmlbind.MarkupContent;
import name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding;

/**
 * Binds XML content as mixed raw/component markup.
 */
public final class MarkupContentBinding implements ElementObjectBinding<MarkupContent> {

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public MarkupContent parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		// TODO
		return null;
	}

}
