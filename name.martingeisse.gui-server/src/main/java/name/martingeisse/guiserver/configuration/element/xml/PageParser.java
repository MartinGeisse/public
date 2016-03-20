/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.element.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.template.Template;
import name.martingeisse.guiserver.xml.content.ContentParser;

/**
 * Parses a page template.
 */
public class PageParser extends TemplateBasedElementParser {

	/**
	 * Constructor.
	 * @param templateParser the template parser
	 */
	public PageParser(ContentParser<MarkupContent<ComponentGroupConfiguration>> templateParser) {
		super(templateParser);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.xml.TemplateBasedElementParser#writeWicketMarkupIntro(javax.xml.stream.XMLStreamWriter)
	 */
	@Override
	protected void writeWicketMarkupIntro(XMLStreamWriter markupWriter) throws XMLStreamException {
		markupWriter.writeStartElement("html");
		markupWriter.writeStartElement("body");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.xml.TemplateBasedElementParser#writeWicketMarkupOutro(javax.xml.stream.XMLStreamWriter)
	 */
	@Override
	protected void writeWicketMarkupOutro(XMLStreamWriter markupWriter) throws XMLStreamException {
		markupWriter.writeEndElement();
		markupWriter.writeEndElement();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.xml.TemplateBasedElementParser#createConfigurationElement(java.lang.String, name.martingeisse.guiserver.template.Template)
	 */
	@Override
	protected Element createConfigurationElement(String path, Template template) {
		return new PageConfiguration(path, template);
	}

}
