/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import java.io.File;
import java.io.FileInputStream;

import name.martingeisse.reporting.document.Document;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This class provides a single static method to parse report definitions.
 */
public class ReportDefinitionParser {

	/**
	 * Prevent instantiation.
	 */
	private ReportDefinitionParser() {
	}

	/**
	 * Parses the specified report definition file.
	 * @param definitionFile the definition file to parse
	 * @return the unbound document
	 */
	public static Document parse(final File definitionFile) {
		try {
			final FileInputStream fileInputStream = new FileInputStream(definitionFile);
			final InputSource inputSource = new InputSource(fileInputStream);
			final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(new MainHandler());
			xmlReader.parse(inputSource);
			fileInputStream.close();
			return new Document();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
