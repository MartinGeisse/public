/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.jsonapi;

/**
 * This exception type can be thrown by subclasses of
 * {@link AbstractJsonApiHandler} and will be converted
 * into a proper JSON response for the client.
 */
public final class JsonApiException extends RuntimeException {

	/**
	 * the errorCode
	 */
	private final int errorCode;
	
	/**
	 * the errorMessage
	 */
	private final String errorMessage;

	/**
	 * Constructor.
	 * @param errorCode the error code (must not be 0)
	 * @param errorMessage the error message
	 */
	public JsonApiException(int errorCode, String errorMessage) {
		super("error (" + errorCode + "): " + errorMessage);
		if (errorCode == 0) {
			throw new IllegalArgumentException("invalid error code: 0");
		}
		if (errorMessage == null) {
			errorMessage = "";
		}
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	/**
	 * Getter method for the errorCode.
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Getter method for the errorMessage.
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
}
