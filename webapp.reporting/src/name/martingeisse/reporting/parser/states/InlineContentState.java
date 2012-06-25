/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.states;

import org.xml.sax.Attributes;

import name.martingeisse.reporting.document.FormattedCompoundInlineItem;
import name.martingeisse.reporting.document.IBlockItem;
import name.martingeisse.reporting.document.IInlineItem;
import name.martingeisse.reporting.document.InlineFormattingInstruction;
import name.martingeisse.reporting.document.Paragraph;
import name.martingeisse.reporting.document.TextInlineItem;
import name.martingeisse.reporting.parser.IParserStateContext;
import name.martingeisse.reporting.parser.ParserConstants;
import name.martingeisse.reporting.parser.UnexpectedElementException;

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
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startState(IParserStateContext context, Class<?> expectedReturnType, String namespaceUri, String name, Attributes attributes) {
		initializeReturnType(context, expectedReturnType, IInlineItem.class, IBlockItem.class);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public void consumeReturnData(IParserStateContext context, Object data) {
		result.getSubItems().add((IInlineItem)data);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes) {
		if (ParserConstants.CORE_NAMESPACE.equals(namespaceUri)) {
			InlineFormattingInstruction formattingInstruction = InlineFormattingInstruction.findByHtmlElement(name);
			if (formattingInstruction != null) {
				context.pushState(new InlineContentState(formattingInstruction), IInlineItem.class, namespaceUri, name, attributes);
				return;
			}
		}
		throw new UnexpectedElementException(namespaceUri, name, "expected formatted inline content");
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(IParserStateContext context, String namespaceUri, String name) {
		context.popState(getExpectedReturnTypeByParent() == IBlockItem.class ? new Paragraph(result) : result);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public void consumeText(IParserStateContext context, String text) {
		result.getSubItems().add(new TextInlineItem(text));
	}
	
}
