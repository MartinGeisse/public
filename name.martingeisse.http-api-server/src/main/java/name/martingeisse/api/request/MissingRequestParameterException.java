/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

/**
 * This exception type indicates that a required parameter is missing.
 */
public class MissingRequestParameterException extends RequestParametersException {

	/**
	 * the parameterName
	 */
	private final String parameterName;
	
	/**
	 * Constructor.
	 * @param parameterName the name of the missing parameter
	 */
	public MissingRequestParameterException(String parameterName) {
		super("missing parameter: " + parameterName);
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
