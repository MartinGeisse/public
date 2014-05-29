/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

/**
 * This exception type indicates that an actual passed parameter was not
 * expected by the handler.
 */
public class UnexpectedRequestParameterException extends RequestParametersException {

	/**
	 * the parameterName
	 */
	private final String parameterName;
	
	/**
	 * Constructor.
	 * @param parameterName the name of the missing parameter
	 */
	public UnexpectedRequestParameterException(String parameterName) {
		super("unexpected parameter: " + parameterName);
		this.parameterName = parameterName;
	}

	/**
	 * Getter method for the parameterName.
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}
	
}
