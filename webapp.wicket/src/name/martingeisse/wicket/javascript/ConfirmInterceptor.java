/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.javascript;

/**
 * This interceptor opens a confirmation dialog and calls the original
 * function if and only if the user confirmed.
 * 
 * @param <T> the parameter type
 */
public final class ConfirmInterceptor<T> extends AbstractVoidInterceptor<T> {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.javascript.IJavascriptInteractionInterceptor#printInterceptorFunction(java.lang.StringBuilder)
	 */
	@Override
	public void printInterceptorFunction(StringBuilder builder) {
		builder.append("function(f){if(confirm('Are you sure?'))f();}");
	}
	
}
