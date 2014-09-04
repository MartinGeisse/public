/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstNull;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;

/**
 * This interface defines the structure of a JSON input value
 * with regards to normalization and validation.
 */
public interface JsonValueSchema {

	/**
	 * Validates a JSON value according to this schema and writes validation
	 * messages into the specified validation report. Returns the normalized
	 * value, taking the specified value, default value from the schema, and
	 * normalization rules into account.
	 * 
	 * @param value the value to validate
	 * @param validationReport the validation report to write results into
	 * @return the normalized value; never returns null, but may return
	 * {@link JsonAstNull}.
	 */
	public JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport);

}
