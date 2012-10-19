/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class wraps the {@link HttpServletRequest} from the
 * request cycle and provides easy access to parameters.
 * 
 * All getter methods have a flag parameter called 'required'. If the
 * parameter is missing, then this flag decides whether the method
 * returns null (required is false) or throws a
 * {@link MissingRequestParameterException} (required is true).
 */
public final class RequestParameters {

	/**
	 * the request
	 */
	private final HttpServletRequest request;
	
	/**
	 * the customParameters
	 */
	private final Map<String, String> customParameters;
	
	/**
	 * Constructor.
	 * @param request the request
	 */
	public RequestParameters(final HttpServletRequest request) {
		this.request = request;
		this.customParameters = new HashMap<String, String>();
	}

	/**
	 * Getter method for the request.
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}
	
	/**
	 * Getter method for the customParameters.
	 * @return the customParameters
	 */
	public Map<String, String> getCustomParameters() {
		return customParameters;
	}
	
	/**
	 * Sets a custom parameter, possibly overriding a request parameter.
	 * @param name the name of the parameter to set
	 * @param value the value to set
	 */
	public void set(String name, String value) {
		customParameters.put(name, value);
	}

	/**
	 * Obtains the string value of a request parameter.
	 *  
	 * @param name the parameter name
	 * @param required whether the parameter is required
	 * @return the parameter value
	 */
	public String getString(String name, boolean required) {
		String value = customParameters.get(name);
		if (value == null) {
			value = request.getParameter(name);;
		}
		if (required && value == null) {
			throw new MissingRequestParameterException(name);
		}
		return value;
	}

	/**
	 * Obtains the integer value of a request parameter.
	 * 
	 * @param name the parameter name
	 * @param required whether the parameter is required
	 * @return the parameter value
	 */
	public Integer getInteger(String name, boolean required) {
		String value = getString(name, required);
		try {
			return (value == null ? null : Integer.parseInt(value));
		} catch (NumberFormatException e) {
			throw new InvalidRequestParameterException(name, "could not parse integer");
		}
	}
	
}
