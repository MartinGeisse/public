/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

/**
 * This exception type indicates that a request parameter has an invalid value.
 */
public class InvalidRequestParameterException extends ApiRequestParametersException {

	/**
	 * the parameterName
	 */
	private final String parameterName;
	
	/**
	 * the description
	 */
	private final String description;
	
	/**
	 * Constructor.
	 * @param parameterName the parameter name
	 * @param description a description of the error
	 */
	public InvalidRequestParameterException(String parameterName, String description) {
		super(createMessage(parameterName, description));
		this.parameterName = parameterName;
		this.description = description;
	}

	/**
	 * 
	 */
	private static String createMessage(String parameterName, String description) {
		return "invalid value for request parameter " + parameterName + ": " + description;
	}

	/**
	 * Getter method for the parameterName.
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}
	
	/**
	 * Getter method for the description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
}
