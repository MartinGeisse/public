/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.javascript;

/**
 * This interceptor never calls the original action.
 * @param <T> the parameter type
 */
public final class SkipInterceptor<T> extends AbstractVoidInterceptor<T> {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.javascript.IJavascriptInteractionInterceptor#printInterceptorFunction(java.lang.StringBuilder)
	 */
	@Override
	public void printInterceptorFunction(StringBuilder builder) {
		builder.append("function(f){}");
	}
	
}
