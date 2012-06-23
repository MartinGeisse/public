/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import org.xml.sax.Attributes;

import name.martingeisse.reporting.document.IBlockItem;
import name.martingeisse.reporting.document.IInlineItem;
import name.martingeisse.reporting.document.Paragraph;
import name.martingeisse.reporting.document.Section;
import name.martingeisse.reporting.util.ToStringUtil;

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
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class)
	 */
	@Override
	public void startState(IParserStateContext context, Class<?> expectedReturnType) {
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
		} else if (data instanceof IInlineItem) {
			section.getDirectContents().getSubItems().add(new Paragraph((IInlineItem)data));
		} else {
			throw new UnexpectedReturnDataException(data, "expected Section, IBlockItem or IInlineItem");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes) {
		if (ParserUtil.isCoreElement(namespaceUri, name, "section")) {
			throw new RuntimeException("NOT YET IMPLEMENTED"); // TODO
		} else if (section.getSubsections().isEmpty() && ParserUtil.isCoreElement(namespaceUri, name, "p")) {
			context.pushState(new InlineContentState(), IInlineItem.class);
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
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public void consumeText(IParserStateContext context, String text) {
		noTextExpected(text, "expected sub-sections or block content");
	}

}
