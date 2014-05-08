/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonschema;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;

/**
 * This interface defines a JSON "schema" -- an object that is able to validate
 * the data from a {@link JsonAnalyzer}.
 */
public interface IJsonSchema {

	/**
	 * Validates the specified JSON data.
	 * @param json the analyzer for the JSON data
	 * @return true if valid, false if not
	 */
	public boolean validate(JsonAnalyzer json);
	
}
