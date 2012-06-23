/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import org.xml.sax.Attributes;

/**
 * Base implementation of {@link IParserState}.
 */
public class AbstractParserState implements IParserState {

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class)
	 */
	@Override
	public void startState(IParserStateContext context, Class<?> expectedReturnType) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public void consumeReturnData(IParserStateContext context, Object data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes) {
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
	}

	/**
	 * This method can be used by states that only support a single possible return type
	 * when returning data to their parent state. It checks whether the supported
	 * return type is assignable to the expected return type from the parent state, and
	 * if not, throws an exception.
	 * @param supportedReturnType the return type supported by this class, i.e. the
	 * "expected expected return type".
	 * @param expectedReturnTypeByParent the return type expected by the parent state,
	 * i.e. the "actually expected return type".
	 */
	protected void checkOnlyPossibleReturnType(Class<?> supportedReturnType, Class<?> expectedReturnTypeByParent) {
		if (!expectedReturnTypeByParent.isAssignableFrom(supportedReturnType)) {
			throw new RuntimeException("Parser state " + getClass().getSimpleName() + " only supports return type " + supportedReturnType +
				" but the parent state expects incompatible return type " + expectedReturnTypeByParent);
		}
	}

	/**
	 * This method is useful for use in the consumeText() method for elements that
	 * are expected to be empty or at least contain only sub-elements, not character
	 * content. It trim()s the characters, then throws an exception if any (non-space)
	 * characters are left.
	 * @param text the characters passed to consumeText()
	 * @param info the info string to pass to the exception constructor in case of errors
	 */
	protected void noTextExpected(String text, String info) {
		text = text.trim();
		if (!text.isEmpty()) {
			throw new UnexpectedTextException(text, info);
		}
	}
	
}
