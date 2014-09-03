/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstObject;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty;

/**
 * Base class that can act both as a value schema and a property schema.
 * When used in the latter case, the property must exist, its name is
 * ignored, and this schema acts on the property value as if it were a
 * standalone value.
 */
public abstract class JsonValueOrPropertySchema implements JsonValueSchema, JsonPropertySchema {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema#validateAndNormalizeProperty(name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
	 */
	@Override
	public final JsonAstObjectProperty validateAndNormalizeProperty(final JsonAstObjectProperty property, final JsonValidationReport validationReport) {
		// TODO
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema#validateAndNormalizeMissingProperty(name.martingeisse.common.javascript.ownjson.ast.JsonAstObject, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
	 */
	@Override
	public final JsonAstObjectProperty validateAndNormalizeMissingProperty(final JsonAstObject parentObject, final JsonValidationReport validationReport) {
		// TODO
		return null;
	}

}
