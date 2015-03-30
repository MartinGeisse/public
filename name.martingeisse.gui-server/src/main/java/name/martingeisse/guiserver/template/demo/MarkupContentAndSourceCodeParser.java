/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template.demo;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.XmlStreamReaderTee;
import name.martingeisse.guiserver.xml.content.ContentParser;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

/**
 * A parser for {@link MarkupContentAndSourceCode} that wraps a parser for
 * {@link MarkupContent} and also copies the parsed XML code.
 */
public class MarkupContentAndSourceCodeParser implements ContentParser<MarkupContentAndSourceCode> {

	/**
	 * the wrappedParser
	 */
	private final ContentParser<MarkupContent<ComponentGroupConfiguration>> wrappedParser;

	/**
	 * Constructor.
	 * @param wrappedParser the wrapped content parser
	 */
	public MarkupContentAndSourceCodeParser(ContentParser<MarkupContent<ComponentGroupConfiguration>> wrappedParser) {
		this.wrappedParser = wrappedParser;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.content.ContentParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader)
	 */
	@Override
	public MarkupContentAndSourceCode parse(MyXmlStreamReader reader) throws XMLStreamException {
		StringWriter stringWriter = new StringWriter();
		XMLStreamWriter writer = buildXmlStreamWriter(stringWriter);
		MarkupContent<ComponentGroupConfiguration> markupContent = wrappedParser.parse(new MyXmlStreamReader(new XmlStreamReaderTee(reader, writer, true)));
		String result = stringWriter.toString().replace(" xmlns:gui=\"http://guiserver.martingeisse.name/v1\"", "").trim();
		
		// TODO write cleaning-up XML writer
		
		// unfortunately, XMLStreamWriter doesn't generate empty elements automatically, and we cannot easily
		// detect them while copying from a reader...
		result = result.replaceAll("(\\<[^\\<\\>\\/\\\"]+(?:\\\"[^\\\"]*\\\"[^\\<\\>\\/\\\"]*)*)\\>\\<\\/[^\\<\\>\\/]+\\>", "$1/>");
		
		return new MarkupContentAndSourceCode(markupContent, result);
	}
	
	/**
	 * 
	 */
	private XMLStreamWriter buildXmlStreamWriter(Writer w) throws XMLStreamException {
		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		xmlOutputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", true);
		return new IndentingXMLStreamWriter(xmlOutputFactory.createXMLStreamWriter(w));
	}

}
