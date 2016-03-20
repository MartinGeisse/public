/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema.parts;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstBoolean;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.schema.AbstractJsonValueOrPropertySchema;
import name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport;

/**
 * Checks that a value is of boolean type.
 */
public final class JsonBooleanSchema extends AbstractJsonValueOrPropertySchema {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema#validateAndNormalizeValue(name.martingeisse.common.javascript.ownjson.ast.JsonAstValue, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
	 */
	@Override
	public JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport) {
		if (value instanceof JsonAstBoolean) {
			return value;
		} else {
			validationReport.addWrongTypeError(value, "boolean");
			return new JsonAstBoolean(value, false);
		}
	}

}
