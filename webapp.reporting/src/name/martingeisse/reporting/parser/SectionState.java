/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import org.xml.sax.Attributes;

import name.martingeisse.reporting.document.Section;

/**
 * This class handles section contents.
 */
public class SectionState implements IParserState {

	/**
	 * the section
	 */
	private final Section section;
	
	/**
	 * Constructor.
	 */
	public SectionState() {
		this.section = new Section();
	}
	
	/**
	 * Constructor.
	 * @param section the section to fill with contents
	 */
	public SectionState(Section section) {
		this.section = section;
	}
	
	/**
	 * Getter method for the section.
	 * @return the section
	 */
	public Section getSection() {
		return section;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startState(name.martingeisse.reporting.parser.IParserStateContext)
	 */
	@Override
	public void startState(IParserStateContext context) {
		System.out.println("* start state");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public void consumeReturnData(IParserStateContext context, Object data) {
		System.out.println("* consume return data: " + data);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes) {
		System.out.println("* start element: " + namespaceUri + " -- " + name);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(IParserStateContext context, String namespaceUri, String name) {
		System.out.println("* end element: " + namespaceUri + " -- " + name);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public void consumeText(IParserStateContext context, String text) {
		System.out.println("* text: " + text);
	}

}
