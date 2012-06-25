/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.states;

import name.martingeisse.reporting.parser.IParserReturnTypeAdapterFactory;
import name.martingeisse.reporting.parser.IParserReturnTypeAdapterProvider;
import name.martingeisse.reporting.parser.IParserState;
import name.martingeisse.reporting.parser.IParserStateContext;
import name.martingeisse.reporting.parser.NopReturnTypeAdapterFactory;
import name.martingeisse.reporting.parser.UnexpectedTextException;

import org.xml.sax.Attributes;

/**
 * Base implementation of {@link IParserState}.
 */
public class AbstractParserState implements IParserState {

	/**
	 * the expectedReturnTypeByParent
	 */
	private Class<?> expectedReturnTypeByParent;

	/**
	 * the supportedReturnType
	 */
	private Class<?> supportedReturnType;
	
	/**
	 * the returnTypeAdapterFactory
	 */
	private IParserReturnTypeAdapterFactory returnTypeAdapterFactory;

	/**
	 * Getter method for the expectedReturnTypeByParent.
	 * @return the expectedReturnTypeByParent
	 */
	public Class<?> getExpectedReturnTypeByParent() {
		return expectedReturnTypeByParent;
	}

	/**
	 * Getter method for the supportedReturnType.
	 * @return the supportedReturnType
	 */
	public final Class<?> getSupportedReturnType() {
		return supportedReturnType;
	}
	
	/**
	 * Getter method for the returnTypeAdapterFactory.
	 * @return the returnTypeAdapterFactory
	 */
	public IParserReturnTypeAdapterFactory getReturnTypeAdapterFactory() {
		return returnTypeAdapterFactory;
	}

	/**
	 * This method can be used by states that only support one or a few possible return
	 * types when returning data to their parent state. It looks for the first supported
	 * type that is statically assignable to the expected return type (throwing an exception
	 * if no such type can be found). Both the expected and supported type are stored
	 * and can later be obtained via getter methods.
	 * 
	 * The parent state may pass null to indicate that it accepts any return value,
	 * no matter its type. In this case, the expected return type field of this state is
	 * set to null, and the supported type chosen is set to the first supported type
	 * (unless no types are supported by this state at all, in which case this field
	 * is also set to null).
	 * 
	 * @param context the parser state context
	 * @param expectedReturnTypeByParent the return type expected by the parent state,
	 * i.e. the "actually expected return type".
	 * @param supportedReturnTypes the return types supported by this class, i.e. the
	 * "expected expected return types".
	 */
	protected final void initializeReturnType(final IParserStateContext context, final Class<?> expectedReturnTypeByParent, final Class<?>... supportedReturnTypes) {

		// initialization
		this.expectedReturnTypeByParent = expectedReturnTypeByParent;
		this.supportedReturnType = null;
		this.returnTypeAdapterFactory = null;
		
		// if the parent state doesn't care about the return type, we just choose the first supported type
		if (expectedReturnTypeByParent == null) {
			this.supportedReturnType = (supportedReturnTypes.length == 0 ? null : supportedReturnTypes[0]);
			this.returnTypeAdapterFactory = NopReturnTypeAdapterFactory.instance;
			return;
		}
		
		// otherwise we use the type adapter factory to choose an adapter (NOP-adaptation is implicitly supported)
		IParserReturnTypeAdapterProvider adapterProvider = context.getTypeAdapterProvider();
		for (Class<?> supportedReturnType : supportedReturnTypes) {
			IParserReturnTypeAdapterFactory factory = adapterProvider.findReturnTypeAdapterFactory(expectedReturnTypeByParent, supportedReturnType);
			if (factory != null) {
				this.supportedReturnType = supportedReturnType;
				this.returnTypeAdapterFactory = factory;
				return;
			}
		}
		
		// otherwise, there is no suitable factory, so we throw an exception
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
	 * This method can be used instead of context.popState() and uses the return type adapter factory
	 * determined by initializeReturnType() to pass an adapted return data to the parent state instead
	 * of the original return data
	 * @param originalReturnData the original return data
	 */
	protected final void popStateWithAdaptedReturnData(IParserStateContext context, Object originalReturnData) {
		context.popState(returnTypeAdapterFactory.createAdapter(expectedReturnTypeByParent, supportedReturnType, originalReturnData));
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startState(IParserStateContext context, Class<?> expectedReturnType, String namespaceUri, String name, Attributes attributes) {
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
	 * This method is useful for use in the consumeText() method for elements that
	 * are expected to be empty or at least contain only sub-elements, not character
	 * content. It trim()s the characters, then throws an exception if any (non-space)
	 * characters are left.
	 * @param text the characters passed to consumeText()
	 * @param info the info string to pass to the exception constructor in case of errors
	 */
	protected final void noTextExpected(String text, final String info) {
		text = text.trim();
		if (!text.isEmpty()) {
			throw new UnexpectedTextException(text, info);
		}
	}

}
