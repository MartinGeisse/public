/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.javascript;

/**
 * Base class for interceptors that do not produce an
 * extra parameter. This class leaves the type parameter
 * open since it can be used for any type.
 * 
 * @param <T> the extra parameter type
 */
public abstract class AbstractVoidInterceptor<T> implements IJavascriptInteractionInterceptor<T> {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.javascript.IJavascriptInteractionInterceptor#convertParameter(java.lang.Object)
	 */
	@Override
	public final T convertParameter(Object jsonData) {
		return null;
	}
	
}
