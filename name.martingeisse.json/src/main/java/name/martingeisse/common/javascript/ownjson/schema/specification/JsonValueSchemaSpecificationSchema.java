/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema.specification;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport;
import name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema;

/**
 * This is a meta-schema that validates the JSON structure
 * which represents a JSON schema.
 */
public final class JsonValueSchemaSpecificationSchema implements JsonValueSchema {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema#validateAndNormalize(name.martingeisse.common.javascript.ownjson.ast.JsonAstValue, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
	 */
	@Override
	public JsonAstValue validateAndNormalize(final JsonAstValue value, final JsonValidationReport validationReport) {
		// TODO
		return value;
	}

}
