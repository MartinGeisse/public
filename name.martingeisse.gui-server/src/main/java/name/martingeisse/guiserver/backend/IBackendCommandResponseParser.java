/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.backend;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;

/**
 * Parses the JSON response from the backend into a
 * {@link BackendCommandResponse}. Custom implementations
 * allow to support new kinds of command responses.
 * 
 * Note that just supporting new commands can be done by
 * implementing {@link IBackendCommandParser}. Implementing
 * a new command response parser is only needed if the general
 * response format should be different.
 * 
 * It is recommended that the whole response object only consists
 * of immutable objects, using the immutable collections from the
 * Google Guava package and user-defined custom immutable classes.
 */
public interface IBackendCommandResponseParser {

	/**
	 * Parses a backend command response from its JSON representation.
	 * 
	 * @param json the JSON response
	 * @return the parsed backend command response
	 */
	public BackendCommandResponse parseBackendCommandResponse(JsonAnalyzer json);
	
}
