/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.schema.parts;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstNull;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObject;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstString;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport;
import name.martingeisse.common.javascript.ownjson.schema.JsonValueOrPropertySchema;

/**
 * Implements "optional" schemas. These accept null values and missing
 * properties in addition to the values accepted by the base schema.
 * 
 * Any null values will be normalized to the default value, if any, or
 * left alone if there is no default value. 
 * 
 * Any missing property will be normalized to a property with the default
 * value, if any, or to a property with null value if there is no default
 * value.
 */
public final class OptionalSchema implements JsonValueOrPropertySchema {

	/**
	 * the baseSchema
	 */
	private final JsonValueOrPropertySchema baseSchema;
	
	/**
	 * the defaultValue
	 */
	private final JsonAstValue defaultValue;

	/**
	 * Constructor.
	 * @param baseSchema the base schema
	 */
	public OptionalSchema(JsonValueOrPropertySchema baseSchema) {
		this(baseSchema, null);
	}

	/**
	 * Constructor.
	 * @param baseSchema the base schema
	 * @param defaultValue the default value (the location of this AST node is ignored)
	 */
	public OptionalSchema(JsonValueOrPropertySchema baseSchema, JsonAstValue defaultValue) {
		this.baseSchema = baseSchema;
		this.defaultValue = defaultValue;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema#validateAndNormalizeValue(name.martingeisse.common.javascript.ownjson.ast.JsonAstValue, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
	 */
	@Override
	public JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport) {
		if (value instanceof JsonAstNull) {
			return (defaultValue == null ? value : defaultValue.withLocation(value));
		} else {
			return baseSchema.validateAndNormalizeValue(value, validationReport);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema#validateAndNormalizeProperty(name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
	 */
	@Override
	public JsonAstObjectProperty validateAndNormalizeProperty(JsonAstObjectProperty property, JsonValidationReport validationReport) {
		JsonAstValue value = property.getValue();
		if (value instanceof JsonAstNull) {
			return (defaultValue == null ? property : new JsonAstObjectProperty(property.getName(), defaultValue.withLocation(property.getValue())));
		} else {
			return baseSchema.validateAndNormalizeProperty(property, validationReport);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema#validateAndNormalizeMissingProperty(name.martingeisse.common.javascript.ownjson.ast.JsonAstObject, java.lang.String, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
	 */
	@Override
	public JsonAstObjectProperty validateAndNormalizeMissingProperty(JsonAstObject parentObject, String propertyName, JsonValidationReport validationReport) {
		JsonAstString name = new JsonAstString(parentObject, propertyName);
		JsonAstValue value = (defaultValue == null ? new JsonAstNull(parentObject) : defaultValue.withLocation(parentObject));
		return new JsonAstObjectProperty(name, value);
	}

}