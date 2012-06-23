/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import name.martingeisse.reporting.document.IBlockItem;
import name.martingeisse.reporting.document.Section;

import org.xml.sax.Attributes;

/**
 * This class handles section contents.
 */
public class SectionState extends AbstractParserState {

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
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startState(IParserStateContext context, Class<?> expectedReturnType, String namespaceUri, String name, Attributes attributes) {
		initializeReturnType(expectedReturnType, Section.class);
		section.setTitle(attributes.getValue("", "title"));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public void consumeReturnData(IParserStateContext context, Object data) {
		if (data instanceof Section) {
			section.getSubsections().add((Section)data);
		} else if (data instanceof IBlockItem) {
			section.getDirectContents().getSubItems().add((IBlockItem)data);
		} else {
			throw new UnexpectedReturnDataException(data, "expected Section or IBlockItem");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes) {
		if (ParserUtil.isCoreElement(namespaceUri, name, "section")) {
			context.pushState(new SectionState(), Section.class, namespaceUri, name, attributes);
		} else if (section.getSubsections().isEmpty() && ParserUtil.isCoreElement(namespaceUri, name, "p")) {
			context.pushState(new InlineContentState(), IBlockItem.class, namespaceUri, name, attributes);
		} else {
			String info = (section.getSubsections().isEmpty() ? "expected <core:p> or <core:section>" : "expected <core:section>");
			throw new UnexpectedElementException(namespaceUri, name, info);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(IParserStateContext context, String namespaceUri, String name) {
		context.popState(section);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public void consumeText(IParserStateContext context, String text) {
		noTextExpected(text, "expected sub-sections or block content");
	}

}
