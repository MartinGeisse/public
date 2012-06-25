/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

/**
 * This "adapter factory" simply returns the original value. It is used
 * when no adaptation is necessary.
 * 
 * This is a singleton class.
 */
public final class NopReturnTypeAdapterFactory implements IParserReturnTypeAdapterFactory {

	/**
	 * the instance
	 */
	public static final NopReturnTypeAdapterFactory instance = new NopReturnTypeAdapterFactory();
	
	/**
	 * Constructor.
	 */
	private NopReturnTypeAdapterFactory() {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserReturnTypeAdapterFactory#supportsAdapter(java.lang.Class, java.lang.Class)
	 */
	@Override
	public boolean supportsAdapter(Class<?> expectedReturnTypeByParent, Class<?> supportedReturnType) {
		return expectedReturnTypeByParent.isAssignableFrom(supportedReturnType);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserReturnTypeAdapterFactory#createAdapter(java.lang.Class, java.lang.Class, java.lang.Object)
	 */
	@Override
	public Object createAdapter(Class<?> expectedReturnTypeByParent, Class<?> supportedReturnType, Object originalReturnData) {
		return originalReturnData;
	}
	
}
