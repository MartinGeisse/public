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

	/**
	 * the expectedReturnType
	 */
	private Class<?> expectedReturnType;

	/**
	 * the supportedReturnType
	 */
	private Class<?> supportedReturnType;

	/**
	 * Getter method for the expectedReturnType.
	 * @return the expectedReturnType
	 */
	public final Class<?> getExpectedReturnType() {
		return expectedReturnType;
	}

	/**
	 * Setter method for the expectedReturnType.
	 * @param expectedReturnType the expectedReturnType to set
	 */
	public final void setExpectedReturnType(final Class<?> expectedReturnType) {
		this.expectedReturnType = expectedReturnType;
	}

	/**
	 * Getter method for the supportedReturnType.
	 * @return the supportedReturnType
	 */
	public Class<?> getSupportedReturnType() {
		return supportedReturnType;
	}

	/**
	 * Setter method for the supportedReturnType.
	 * @param supportedReturnType the supportedReturnType to set
	 */
	public void setSupportedReturnType(final Class<?> supportedReturnType) {
		this.supportedReturnType = supportedReturnType;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class)
	 */
	@Override
	public void startState(final IParserStateContext context, final Class<?> expectedReturnType) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public void consumeReturnData(final IParserStateContext context, final Object data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(final IParserStateContext context, final String namespaceUri, final String name, final Attributes attributes) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(final IParserStateContext context, final String namespaceUri, final String name) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public void consumeText(final IParserStateContext context, final String text) {
	}

	/**
	 * This method can be used by states that only support one or a few possible return
	 * types when returning data to their parent state. It looks for the first supported
	 * type that is statically assignable to the expected return type (throwing an exception
	 * if no such type can be found). Both the expected and supported type are stored
	 * and can later be obtained via getter methods.
	 * @param expectedReturnTypeByParent the return type expected by the parent state,
	 * i.e. the "actually expected return type".
	 * @param supportedReturnTypes the return types supported by this class, i.e. the
	 * "expected expected return types".
	 */
	protected void initializeReturnType(final Class<?> expectedReturnTypeByParent, final Class<?>... supportedReturnTypes) {
		
		// look for a matching type
		for (Class<?> supportedType : supportedReturnTypes) {
			if (expectedReturnTypeByParent.isAssignableFrom(supportedType)) {
				this.expectedReturnType = expectedReturnTypeByParent;
				this.supportedReturnType = supportedType;
				return;
			}
		}
		
		// throw an exception
		StringBuilder builder = new StringBuilder();
		builder.append("Parser state ").append(getClass().getCanonicalName());
		builder.append(" cannot handle expected return type ").append(expectedReturnTypeByParent.getCanonicalName());
		builder.append(" -- supported types are:");
		for (Class<?> type : supportedReturnTypes) {
			builder.append(' ').append(type.getCanonicalName());
		}
		throw new RuntimeException(builder.toString());
		
	}

	/**
	 * This method is useful for use in the consumeText() method for elements that
	 * are expected to be empty or at least contain only sub-elements, not character
	 * content. It trim()s the characters, then throws an exception if any (non-space)
	 * characters are left.
	 * @param text the characters passed to consumeText()
	 * @param info the info string to pass to the exception constructor in case of errors
	 */
	protected void noTextExpected(String text, final String info) {
		text = text.trim();
		if (!text.isEmpty()) {
			throw new UnexpectedTextException(text, info);
		}
	}

}
