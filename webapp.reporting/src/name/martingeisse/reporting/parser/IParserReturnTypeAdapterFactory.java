/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

/**
 * This interface is implemented by parser modules to provide return type
 * adapters, i.e. glue code to make a parsed element behave as expected
 * by its parent element.
 */
public interface IParserReturnTypeAdapterFactory {

	/**
	 * Checks whether this factory can adapt the specified types.
	 * @param expectedReturnTypeByParent the return type expected by the parent state
	 * @param supportedReturnType the supported return type by the sub-state
	 * @return try if this adapter is supported, false if not
	 */
	public boolean supportsAdapter(final Class<?> expectedReturnTypeByParent, final Class<?> supportedReturnType);

	/**
	 * Creates an adapter for the return value of a parser state.
	 * @param expectedReturnTypeByParent the return type expected by the parent state
	 * @param supportedReturnType the supported return type by the sub-state
	 * @param originalReturnData the original return data from the sub-state
	 * @return the adapted return data to pass to the parent state
	 */
	public Object createAdapter(final Class<?> expectedReturnTypeByParent, final Class<?> supportedReturnType, final Object originalReturnData);
	
}
