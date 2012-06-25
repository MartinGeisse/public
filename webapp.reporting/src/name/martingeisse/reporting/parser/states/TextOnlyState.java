/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.states;

import name.martingeisse.reporting.parser.IParserStateContext;

import org.xml.sax.Attributes;

/**
 * This state can be used in elements for which only text content is allowed.
 * The only supported return type is {@link String}.
 */
public class TextOnlyState extends AbstractTextOnlyState {

	/**
	 * Constructor.
	 */
	public TextOnlyState() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractTextOnlyState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startState(IParserStateContext context, Class<?> expectedReturnType, String namespaceUri, String name, Attributes attributes) {
		initializeReturnType(context, expectedReturnType, String.class);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractTextOnlyState#createReturnData(java.lang.String)
	 */
	@Override
	protected Object createReturnData(String text) {
		return text;
	}

}
