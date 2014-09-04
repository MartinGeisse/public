/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstBoolean;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstFloat;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstInteger;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstNull;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObject;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstString;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;

/**
 * Holds standard schema implementations as flyweight singletons.
 * 
 * TODO
 */
public final class StandardJsonSchemas {

	/**
	 * Prevent instantiation.
	 */
	private StandardJsonSchemas() {
	}

	/**
	 * A schema that ignores all values and properties. Values get normalized to JSON
	 * null and properties get removed.
	 */
	public static final JsonValueOrPropertySchema IGNORED = new JsonValueOrPropertySchema() {

		@Override
		public JsonAstObjectProperty validateAndNormalizeProperty(JsonAstObjectProperty property, JsonValidationReport validationReport) {
			return null;
		}

		@Override
		public JsonAstObjectProperty validateAndNormalizeMissingProperty(JsonAstObject parentObject, String propertyName, JsonValidationReport validationReport) {
			return null;
		}

		@Override
		public JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport) {
			return new JsonAstNull(value);
		}

	};

	/**
	 * A schema that accepts any value or property and leaves it "as is".
	 */
	public static final JsonValueOrPropertySchema LITERAL = new JsonValueOrPropertySchema() {

		/* (non-Javadoc)
		 * @see name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema#validateAndNormalizeProperty(name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
		 */
		@Override
		public JsonAstObjectProperty validateAndNormalizeProperty(JsonAstObjectProperty property, JsonValidationReport validationReport) {
			return property;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema#validateAndNormalizeMissingProperty(name.martingeisse.common.javascript.ownjson.ast.JsonAstObject, java.lang.String, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
		 */
		@Override
		public JsonAstObjectProperty validateAndNormalizeMissingProperty(JsonAstObject parentObject, String propertyName, JsonValidationReport validationReport) {
			return null;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema#validateAndNormalizeValue(name.martingeisse.common.javascript.ownjson.ast.JsonAstValue, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
		 */
		@Override
		public JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport) {
			return value;
		}

	};

	/**
	 * A schema that accepts any boolean value.
	 */
	public static final JsonValueOrPropertySchema BOOLEAN = new AbstractJsonValueOrPropertySchema() {
		@Override
		public JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport) {
			if (value instanceof JsonAstBoolean) {
				return value;
			} else {
				validationReport.addWrongTypeError(value, "boolean");
				return new JsonAstBoolean(value, false);
			}
		}
	};
	
	/**
	 * A schema that accepts any boolean value or null. Any missing property is accepted and
	 * normalized to null value.
	 */
	public static final JsonValueOrPropertySchema OPTIONAL_BOOLEAN = new OptionalSchema(BOOLEAN);

	/**
	 * A schema that accepts any integer value.
	 */
	public static final JsonValueOrPropertySchema INTEGER = new AbstractJsonValueOrPropertySchema() {
		@Override
		public JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport) {
			if (value instanceof JsonAstInteger) {
				return value;
			} else {
				validationReport.addWrongTypeError(value, "integer");
				return new JsonAstInteger(value, 0);
			}
		}
	};
	
	/**
	 * A schema that accepts any integer value or null. Any missing property is accepted and
	 * normalized to null value.
	 */
	public static final JsonValueOrPropertySchema OPTIONAL_INTEGER = new OptionalSchema(INTEGER);

	/**
	 * A schema that accepts any numeric value and converts it to a double-precision
	 * floating-point ({@link Double}) object.
	 */
	public static final JsonValueOrPropertySchema FLOAT = new AbstractJsonValueOrPropertySchema() {
		@Override
		public JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport) {
			if (value instanceof JsonAstFloat) {
				return value;
			} else {
				validationReport.addWrongTypeError(value, "float");
				return new JsonAstFloat(value, 0);
			}
		}
	};

	/**
	 * A schema that accepts any float value or null. Any missing property is accepted and
	 * normalized to null value.
	 */
	public static final JsonValueOrPropertySchema OPTIONAL_FLOAT = new OptionalSchema(FLOAT);
	
	/**
	 * A schema that accepts any string value.
	 */
	public static final JsonValueOrPropertySchema STRING = new AbstractJsonValueOrPropertySchema() {
		@Override
		public JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport) {
			if (value instanceof JsonAstString) {
				return value;
			} else {
				validationReport.addWrongTypeError(value, "string");
				return new JsonAstString(value, "");
			}
		}
	};

	/**
	 * A schema that accepts any string value or null. Any missing property is accepted and
	 * normalized to null value.
	 */
	public static final JsonValueOrPropertySchema OPTIONAL_STRING = new OptionalSchema(STRING);
	
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
	public static final class OptionalSchema implements JsonValueOrPropertySchema {

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
		private OptionalSchema(JsonValueOrPropertySchema baseSchema) {
			this(baseSchema, null);
		}

		/**
		 * Constructor.
		 * @param baseSchema the base schema
		 * @param defaultValue the default value
		 */
		private OptionalSchema(JsonValueOrPropertySchema baseSchema, JsonAstValue defaultValue) {
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
	
}
