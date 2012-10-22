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
 * The request may be null; in this case, this parameter set
 * simply behaves as for a request without any parameters.
 * 
 * All getter methods have a flag parameter called 'required'. If the
 * parameter is missing, then this flag decides whether the method
 * returns null (required is false) or throws a
 * {@link MissingRequestParameterException} (required is true).
 * 
 * This class allows to add custom parameters not present in the
 * request. These cannot be distinguished from "real" parameters
 * through this class unless the caller explicitly asks either
 * the request or the custom parameter set directly for parameter
 * values.
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
	 * @param request the request, or null for an empty parameter set
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
		if (value == null && request != null) {
			value = request.getParameter(name);;
		}
		if (required && value == null) {
			throw new MissingRequestParameterException(name);
		}
		return value;
	}

	/**
	 * This method is similar to {@link #getString(String, boolean)} with
	 * the 'required' flag set to false, except that this method returns
	 * the specified default value instead of null if the parameter is
	 * missing.
	 * 
	 * @param name the parameter name
	 * @param defaultValue the default value in case the parameter is missing
	 * @return the parameter value or the default value
	 */
	public String getStringOrDefault(String name, String defaultValue) {
		String value = getString(name, false);
		return (value == null ? defaultValue : value);
	}

	/**
	 * Obtains the integer value of a request parameter.
	 * 
	 * @param name the parameter name
	 * @param required whether the parameter is required
	 * @return the parameter value, or null if the parameter is
	 * missing and the 'required' argument is false.
	 */
	public Integer getInteger(String name, boolean required) {
		String value = getString(name, required);
		try {
			return (value == null ? null : Integer.parseInt(value));
		} catch (NumberFormatException e) {
			throw new InvalidRequestParameterException(name, "could not parse integer");
		}
	}

	/**
	 * This method is similar to {@link #getInteger(String, boolean)} with
	 * the 'required' flag set to false, except that this method returns
	 * the specified default value instead of null if the parameter is
	 * missing.
	 * 
	 * @param name the parameter name
	 * @param defaultValue the default value in case the parameter is missing
	 * @return the parameter value or the default value
	 */
	public int getIntegerOrDefault(String name, int defaultValue) {
		Integer value = getInteger(name, false);
		return (value == null ? defaultValue : value);
	}
	
	/**
	 * Obtains the boolean value of a request parameter. Allowed values
	 * are (case-insensitive): ("true", "yes", "on", "1") for true,
	 * ("false", "no", "off", "0") for false.
	 * 
	 * @param name the parameter name
	 * @param required whether the parameter is required
	 * @return the parameter value, or null if the parameter is
	 * missing and the 'required' argument is false.
	 */
	public Boolean getBoolean(String name, boolean required) {
		String value = getString(name, required);
		if (value == null) {
			return null;
		}
		switch (value.length()) {
		
		case 1:
			if (value.equals("1")) {
				return true;
			} else if (value.equals("0")) {
				return false;
			}
			break;
			
		case 2:
			if (value.equals("on")) {
				return true;
			} else if (value.equals("no")) {
				return false;
			}
			break;
			
		case 3:
			if (value.equals("yes")) {
				return true;
			} else if (value.equals("off")) {
				return false;
			}
			break;
			
		case 4:
			if (value.equals("true")) {
				return true;
			}
			break;
			
		case 5:
			if (value.equals("false")) {
				return false;
			}
			break;
		
		}
		
		throw new InvalidRequestParameterException(name, "could not parse boolean value: '" + value + "'");
	}

	/**
	 * This method is similar to {@link #getBoolean(String, boolean)} with
	 * the 'required' flag set to false, except that this method returns
	 * the specified default value instead of null if the parameter is
	 * missing.
	 * 
	 * @param name the parameter name
	 * @param defaultValue the default value in case the parameter is missing
	 * @return the parameter value or the default value
	 */
	public boolean getBooleanOrDefault(String name, boolean defaultValue) {
		Boolean value = getBoolean(name, false);
		return (value == null ? defaultValue : value);
	}
	
}
