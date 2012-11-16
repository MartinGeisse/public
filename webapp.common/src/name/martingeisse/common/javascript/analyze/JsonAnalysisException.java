/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.analyze;

/**
 * This exception type is thrown when a {@link JsonAnalyzer}
 * encounters an unexpected value.
 */
public class JsonAnalysisException extends RuntimeException {

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public JsonAnalysisException(String message) {
		super(message);
	}
	
}
