/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

/**
 * This exception type indicates unexpected data returned from a sub-state.
 */
public class UnexpectedReturnDataException extends ParserException {

	/**
	 * the data
	 */
	private final Object data;
	
	/**
	 * the info
	 */
	private final String info;
	
	/**
	 * Constructor.
	 * @param data the unexpected return data
	 * @param info an informational string about what was expected instead
	 */
	public UnexpectedReturnDataException(Object data, String info) {
		super(createMessage(data, info));
		this.data = data;
		this.info = info;
	}
	
	/**
	 * 
	 */
	private static String createMessage(Object data, String info) {
		return "unexpected data returned from sub-state: " + data + ", class: " + data.getClass().getCanonicalName() + " (" + info + ")";
	}

	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Getter method for the info.
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}
	
}
