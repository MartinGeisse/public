/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema;

/**
 * This interface defines the structure of a JSON input value
 * with regards to normalization and validation.
 */
public interface JsonValueSchema {

	public Object process(JsonValidationReport validationReport);
	
}
