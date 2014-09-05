/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema.specification;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstNull;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObject;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema;
import name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport;
import name.martingeisse.common.javascript.ownjson.schema.JsonValueOrPropertySchema;
import name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema;
import name.martingeisse.common.javascript.ownjson.schema.parts.OptionalSchema;

/**
 * Default implementation of {@link JsonSchemaCatalog}. Defines the following
 * core schemas:
 * 
 * Primitive types:
 * 
 * - 'int': arbitrary integer
 * - 'int+': arbitrary positive integer
 * - 'int+0': arbirary non-negative integer
 * - 'float', 'float+', 'float+0': analogous for float values
 * - 'string': arbitrary string
 * - 'string+': arbitrary non-empty string
 * - 'bool', 'boolean': boolean values only
 * 
 * Type Constructors:
 * 
 * - primitiveBaseType '?': indicates an optional field:
 * 		- on its own, allows the base type or null
 * 		- applied to an object property, also allows the property to be missing
 *   		and will normalize that to null
 *   
 * - primitiveBaseType '?' defaultValue: indicates an optional field with default value:
 * 		similar to an "optional field", but if the resulting value is null,
 * 		will use the default value instead. The default value must be part of
 * 		the name and must itself be a JSON-formatted value of the base type.
 * 
 * Other types:
 * 
 * - 'ignore': the value will be ignored and normalized to null. When applied
 *   to an object property, normalization removes it.
 * - 'literal': whatever the value or property, it is left "as is".
 * 
 */
public final class DefaultJsonSchemaCatalog implements JsonSchemaCatalog {

	/**
	 * the valueSchemas
	 */
	private final Map<String, JsonValueSchema> valueSchemas = new HashMap<>();
	
	/**
	 * the propertySchemas
	 */
	private final Map<String, JsonPropertySchema> propertySchemas = new HashMap<>();
	
	/**
	 * Constructor.
	 */
	public DefaultJsonSchemaCatalog() {
		
		// primitive types
		// TODO
		
		// other types
		add("ignore", IGNORE);
		add("literal", LITERAL);
		
	}
	
	/**
	 * 
	 */
	private void addPrimitive(String baseName, JsonValueOrPropertySchema schema) {
		add(baseName, schema);
		add(baseName + '?', new OptionalSchema(schema));
	}
	
	/**
	 * 
	 */
	private void add(String name, JsonValueOrPropertySchema schema) {
		valueSchemas.put(name, schema);
		propertySchemas.put(name, schema);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.specification.JsonSchemaCatalog#getValueSchema(java.lang.String)
	 */
	@Override
	public JsonValueSchema getValueSchema(final String schemaName) {
		int optionalMarkIndex = schemaName.indexOf('?');
		if (optionalMarkIndex == -1 || optionalMarkIndex == schemaName.length() - 1) {
			return valueSchemas.get(schemaName);
		}
		// TODO schema with default value
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.specification.JsonSchemaCatalog#getPropertySchema(java.lang.String)
	 */
	@Override
	public JsonPropertySchema getPropertySchema(final String schemaName) {
		int optionalMarkIndex = schemaName.indexOf('?');
		if (optionalMarkIndex == -1 || optionalMarkIndex == schemaName.length() - 1) {
			return propertySchemas.get(schemaName);
		}
		// TODO schema with default value
	}

	/**
	 * A schema that ignores all values and properties. Values get normalized to JSON
	 * null and properties get removed.
	 */
	private static final JsonValueOrPropertySchema IGNORE = new JsonValueOrPropertySchema() {

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
	private static final JsonValueOrPropertySchema LITERAL = new JsonValueOrPropertySchema() {

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
	
}
