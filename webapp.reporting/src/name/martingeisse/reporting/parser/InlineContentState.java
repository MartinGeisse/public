/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import org.xml.sax.Attributes;

import name.martingeisse.reporting.document.FormattedCompoundInlineItem;
import name.martingeisse.reporting.document.IInlineItem;
import name.martingeisse.reporting.document.InlineFormattingInstruction;
import name.martingeisse.reporting.document.TextInlineItem;

/**
 * This state consumes inline content and builds an {@link IInlineItem} from it.
 */
public class InlineContentState extends AbstractParserState {

	/**
	 * the result
	 */
	private final FormattedCompoundInlineItem result;
	
	/**
	 * Constructor.
	 */
	public InlineContentState() {
		this(InlineFormattingInstruction.NONE);
	}
	
	/**
	 * Constructor.
	 * @param formattingInstruction the formatting instruction to apply to the contents
	 */
	public InlineContentState(InlineFormattingInstruction formattingInstruction) {
		this.result = new FormattedCompoundInlineItem(formattingInstruction);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class)
	 */
	@Override
	public void startState(IParserStateContext context, Class<?> expectedReturnType) {
		checkOnlyPossibleReturnType(IInlineItem.class, expectedReturnType);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes) {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(IParserStateContext context, String namespaceUri, String name) {
		if (ParserUtil.isCoreElement(namespaceUri, name, "p")) { // TODO only needed for now because formatting tags aren't recognized
			context.popState(result);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public void consumeText(IParserStateContext context, String text) {
		result.getSubItems().add(new TextInlineItem(text));
	}
	
}
