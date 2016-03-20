/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.element.xml;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.google.common.collect.ImmutableList;

import name.martingeisse.guiserver.configuration.ConfigurationException;
import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfigurationList;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.template.Template;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.content.ContentParser;

/**
 * Base class for all configuration elements that use a template,
 * such as pages or panels.
 */
public abstract class TemplateBasedElementParser implements DocumentParser {

	/**
	 * the templateParser
	 */
	private final ContentParser<MarkupContent<ComponentGroupConfiguration>> templateParser;

	/**
	 * Constructor.
	 * @param templateParser the template parser
	 */
	public TemplateBasedElementParser(ContentParser<MarkupContent<ComponentGroupConfiguration>> templateParser) {
		this.templateParser = templateParser;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.xml.DocumentParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader, java.lang.String, java.util.List)
	 */
	@Override
	public Element parse(MyXmlStreamReader reader, String path, List<IConfigurationSnippet> snippetAccumulator) throws XMLStreamException, ConfigurationException {

		// parse the file to obtain markup content
		reader.next();
		MarkupContent<ComponentGroupConfiguration> markupContent = templateParser.parse(reader);

		// assemble the final component configuration tree from the markup content
		String wicketMarkup;
		ComponentGroupConfigurationList components;
		try {
			StringWriter stringWriter = new StringWriter();
			XMLStreamWriter markupWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
			markupWriter.writeStartDocument();
			writeWicketMarkupIntro(markupWriter);
			List<ComponentGroupConfiguration> componentGroupAccumulator = new ArrayList<>();
			ConfigurationAssembler<ComponentGroupConfiguration> assembler = new ConfigurationAssembler<>(markupWriter, componentGroupAccumulator, snippetAccumulator);
			markupContent.assemble(assembler);
			writeWicketMarkupOutro(markupWriter);
			markupWriter.writeEndDocument();
			wicketMarkup = stringWriter.toString();
			components = new ComponentGroupConfigurationList(ImmutableList.copyOf(componentGroupAccumulator));
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}

		// TODO
		System.out.println("------------------------------------------");
		System.out.println("wicket markup for configuration element " + path);
		System.out.println();
		System.out.println(wicketMarkup);
		System.out.println("------------------------------------------");
		// TODO

		return createConfigurationElement(path, new Template(wicketMarkup, components));
	}

	/**
	 * Writes the markup "intro" at the top of the wicket markup file.
	 * 
	 * @param markupWriter the XML writer that is used to write the markup file
	 * @throws XMLStreamException on XML processing errors
	 */
	protected abstract void writeWicketMarkupIntro(XMLStreamWriter markupWriter) throws XMLStreamException;
	
	/**
	 * Writes the markup "outro" at the bottom of the wicket markup file.
	 * 
	 * @param markupWriter the XML writer that is used to write the markup file
	 * @throws XMLStreamException on XML processing errors
	 */
	protected abstract void writeWicketMarkupOutro(XMLStreamWriter markupWriter) throws XMLStreamException;
	
	/**
	 * Creates the configuration element.
	 * 
	 * @param path the path
	 * @param template the template
	 * @return the configuration element
	 */
	protected abstract Element createConfigurationElement(String path, Template template);
	
}
