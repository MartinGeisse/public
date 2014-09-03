/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstObject;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty;

/**
 * This interface defines the structure of a JSON object property
 * with regards to normalization and validation.
 */
public interface JsonPropertySchema {

	/**
	 * Validates an existing JSON object property according to this schema and writes
	 * validation messages into the specified validation report. Returns either the
	 * normalized property or null to remove the property during normalization,
	 * taking the specified property, default value from the schema, and normalization
	 * rules into account.
	 * 
	 * @param property the property to validate
	 * @param validationReport the validation report to write results into
	 * @return the normalized property, or null to indicate that normalization
	 * has removed the property
	 */
	public JsonAstObjectProperty validateAndNormalizeProperty(JsonAstObjectProperty property, JsonValidationReport validationReport);
	
	/**
	 * Validates a missing JSON object property according to this schema and writes
	 * validation messages into the specified validation report. Returns either the
	 * normalized property or null to indicate that the property is still absent after
	 * normalization, taking the specified property, default value from the schema,
	 * and normalization rules into account.
	 * 
	 * @param parentObject the parent object from which the property is missing (used
	 * to obtain the "location" of the missing property)
	 * @param validationReport the validation report to write results into
	 * @return the normalized property, or null to indicate that the property is
	 * still absent after normalization
	 */
	public JsonAstObjectProperty validateAndNormalizeMissingProperty(JsonAstObject parentObject, JsonValidationReport validationReport);

}
