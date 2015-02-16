/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.form;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xml.element.ElementObjectBinding;

/**
 * Binds child elements inside a form field to {@link FormFieldModifier} objects.
 */
public final class FormFieldModifierBinding implements ElementObjectBinding<FormFieldModifier> {

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.element.ElementObjectBinding#parse(name.martingeisse.guiserver.xml.DatabindingXmlStreamReader)
	 */
	@Override
	public FormFieldModifier parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		// TODO
		return null;
	}

}
