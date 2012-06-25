/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

/**
 * This interface is passed to the state factories of parser modules
 * to adapt states to various return types.
 */
public interface IParserReturnTypeAdapterProvider {

	/**
	 * Finds an appropriate adapter factory for the specified types. Returns null
	 * if no such adapter can be found.
	 * 
	 * @param expectedReturnTypeByParent the return type expected by the parent state
	 * @param supportedReturnType the return type supported by the child state
	 * @return the adapter factory to use for the specified types
	 */
	public IParserReturnTypeAdapterFactory findReturnTypeAdapterFactory(final Class<?> expectedReturnTypeByParent, final Class<?> supportedReturnType);
	
}
