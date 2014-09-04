/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstObject;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;

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
		JsonAstValue oldValue = property.getValue();
		JsonAstValue newValue = validateAndNormalizeValue(oldValue, validationReport);
		if (newValue != oldValue) {
			return new JsonAstObjectProperty(property.getName(), newValue);
		} else {
			return property;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema#validateAndNormalizeMissingProperty(name.martingeisse.common.javascript.ownjson.ast.JsonAstObject, java.lang.String, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
	 */
	@Override
	public JsonAstObjectProperty validateAndNormalizeMissingProperty(JsonAstObject parentObject, String propertyName, JsonValidationReport validationReport) {
		validationReport.addMissingPropertyError(parentObject, propertyName);
		return null;
	}

}
