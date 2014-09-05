/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.schema.specification;

import org.apache.commons.lang3.NotImplementedException;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstObject;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstString;
import name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema;
import name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema;

/**
 * Temporary helper class that is used to build a schema from a specification.
 * It implements the common behavior for value schemas and property schemas
 * when parsing from a specification.
 */
class JsonSchemaFromSpecificationFactoryHelper {

	/**
	 * the schemaType
	 */
	private final String schemaType;
	
	/**
	 * the specificationObject
	 */
	private final JsonAstObject specificationObject;
	
	/**
	 * Constructor.
	 */
	public JsonSchemaFromSpecificationFactoryHelper(JsonAstObject specificationObject) throws InvalidSchemaSpecificationException {
		JsonAstObjectProperty schemaProperty = specificationObject.getProperty(JsonSchemaFromSpecificationFactory.SCHEMA_TYPE_PROPERTY_NAME);
		if (schemaProperty == null) {
			this.schemaType = JsonSchemaFromSpecificationFactory.SCHEMA_TYPE_GROUP;
		} else if (schemaProperty.getValue() instanceof JsonAstString) {
			this.schemaType = ((JsonAstString)schemaProperty.getValue()).getValue();
		} else {
			throw new InvalidSchemaSpecificationException();
		}
		this.specificationObject = specificationObject;
	}
	
	/**
	 * 
	 */
	public JsonValueSchema buildValueSchema() {
		// TODO
		throw new NotImplementedException("");
	}

	/**
	 * 
	 */
	public JsonPropertySchema buildPropertySchema() {
		// TODO
		throw new NotImplementedException("");
	}
	
}
