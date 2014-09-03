/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema.specification;

import name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema;
import name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema;


/**
 * Implements a catalog of named JSON schemas that is used to define
 * a set of core schemas. The core schemas can be used to express a
 * schema in a more compact way.
 * 
 * A catalog can contain a value schema and a property schema with the
 * same name.
 */
public interface JsonSchemaCatalog {

	/**
	 * Obtains a value schema from this catalog.
	 * 
	 * @param schemaName the name of the schema to get
	 * @return the schema, or null if this catalog doesn't contain a value
	 * schema with this name
	 */
	public JsonValueSchema getValueSchema(String schemaName);
	
	/**
	 * Obtains a property schema from this catalog.
	 * 
	 * @param schemaName the name of the schema to get
	 * @return the schema, or null if this catalog doesn't contain a property
	 * schema with this name
	 */
	public JsonPropertySchema getPropertySchema(String schemaName);
	
}
