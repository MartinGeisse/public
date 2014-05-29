/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.javascript;

/**
 * Base class for interceptors whose extra parameter is a
 * simple string.
 */
public abstract class AbstractStringInterceptor implements IJavascriptInteractionInterceptor<String> {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.javascript.IJavascriptInteractionInterceptor#convertParameter(java.lang.Object)
	 */
	@Override
	public String convertParameter(Object jsonData) {
		return (jsonData instanceof String ? (String)jsonData : null);
	}
	
}
