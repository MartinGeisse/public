/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.adapter;

import name.martingeisse.reporting.parser.IParserReturnTypeAdapterFactory;

/**
 * Base implementation of {@link IParserReturnTypeAdapterFactory} for the common
 * case: The factory expects a certain "original" type, i.e. type of original values
 * from the sub-state, and produces a certain "adapted" type, i.e. type of adapted
 * values (adapter instances) passed to the parent state. This class provides
 * type-safety to subclasses and implements supportsAdapter().
 * 
 * @param <ORIGINAL> the original type
 * @param <ADAPTED> the adapted type
 */
public abstract class AbstractAdapterFactory<ORIGINAL, ADAPTED> implements IParserReturnTypeAdapterFactory {

	/**
	 * the originalType
	 */
	private final Class<ORIGINAL> originalType;

	/**
	 * the adaptedType
	 */
	private final Class<ADAPTED> adaptedType;

	/**
	 * Constructor.
	 * @param originalType the class object for the original type
	 * @param adaptedType the class object for the adapted type
	 */
	public AbstractAdapterFactory(final Class<ORIGINAL> originalType, final Class<ADAPTED> adaptedType) {
		this.originalType = originalType;
		this.adaptedType = adaptedType;
	}

	/**
	 * Getter method for the originalType.
	 * @return the originalType
	 */
	public final Class<ORIGINAL> getOriginalType() {
		return originalType;
	}

	/**
	 * Getter method for the adaptedType.
	 * @return the adaptedType
	 */
	public final Class<ADAPTED> getAdaptedType() {
		return adaptedType;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserReturnTypeAdapterFactory#supportsAdapter(java.lang.Class, java.lang.Class)
	 */
	@Override
	public final boolean supportsAdapter(final Class<?> expectedReturnTypeByParent, final Class<?> supportedReturnType) {
		return (originalType.isAssignableFrom(supportedReturnType) && expectedReturnTypeByParent.isAssignableFrom(adaptedType));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserReturnTypeAdapterFactory#createAdapter(java.lang.Class, java.lang.Class, java.lang.Object)
	 */
	@Override
	public final Object createAdapter(Class<?> expectedReturnTypeByParent, Class<?> supportedReturnType, Object originalReturnData) {
		return expectedReturnTypeByParent.cast(createAdapter(originalType.cast(originalReturnData)));
	}

	/**
	 * Creates an adapter in a type-safe way.
	 * @param originalReturnData the original return data to adapt
	 * @return the adapter
	 */
	protected abstract ADAPTED createAdapter(ORIGINAL originalReturnData);

}
