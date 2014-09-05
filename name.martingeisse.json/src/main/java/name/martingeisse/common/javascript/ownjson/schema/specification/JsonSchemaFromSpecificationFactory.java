/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema.specification;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstObject;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstString;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema;
import name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema;

/**
 * Builds a {@link JsonValueSchema} or {@link JsonPropertySchema} from
 * its specification.
 */
public final class JsonSchemaFromSpecificationFactory {

	/**
	 * the SCHEMA_TYPE_PROPERTY_NAME
	 */
	public static final String SCHEMA_TYPE_PROPERTY_NAME = "###schema";
	
	/**
	 * the SCHEMA_TYPE_GROUP
	 */
	public static final String SCHEMA_TYPE_GROUP = "group";
	
	/**
	 * the catalog
	 */
	private final JsonSchemaCatalog catalog;

	/**
	 * Constructor.
	 * @param catalog the schema catalog
	 */
	private JsonSchemaFromSpecificationFactory(JsonSchemaCatalog catalog) {
		this.catalog = catalog;
	}

	/**
	 * Builds a JSON value schema from its specification.
	 * 
	 * The specification should be valid according to {@link JsonValueSchemaSpecificationSchema},
	 * but it need not be normalized.
	 * 
	 * @param specification the specification
	 * @return the JSON value schema
	 * @throws InvalidSchemaSpecificationException if an error is found in the specification
	 */
	public JsonValueSchema buildValueSchema(JsonAstValue specification) throws InvalidSchemaSpecificationException {
		if (specification instanceof JsonAstString) {
			JsonAstString specificationString = (JsonAstString)specification;
			return catalog.getValueSchema(specificationString.getValue());
		} else if (specification instanceof JsonAstObject) {
			JsonAstObject specificationObject = (JsonAstObject)specification;
			JsonSchemaFromSpecificationFactoryHelper helper = new JsonSchemaFromSpecificationFactoryHelper(specificationObject);
			return helper.buildValueSchema();
		} else {
			throw new InvalidSchemaSpecificationException();
		}
	}

	/**
	 * Builds a JSON property schema from its specification.
	 * 
	 * The specification should be valid according to {@link JsonPropertySchemaSpecificationSchema},
	 * but it need not be normalized.
	 * 
	 * @param specification the specification
	 * @return the JSON property schema
	 * @throws InvalidSchemaSpecificationException if an error is found in the specification
	 */
	public JsonPropertySchema buildPropertySchema(JsonAstValue specification) throws InvalidSchemaSpecificationException {
		if (specification instanceof JsonAstString) {
			JsonAstString specificationString = (JsonAstString)specification;
			return catalog.getPropertySchema(specificationString.getValue());
		} else if (specification instanceof JsonAstObject) {
			JsonAstObject specificationObject = (JsonAstObject)specification;
			JsonSchemaFromSpecificationFactoryHelper helper = new JsonSchemaFromSpecificationFactoryHelper(specificationObject);
			return helper.buildPropertySchema();
		} else {
			throw new InvalidSchemaSpecificationException();
		}
	}
	
}
