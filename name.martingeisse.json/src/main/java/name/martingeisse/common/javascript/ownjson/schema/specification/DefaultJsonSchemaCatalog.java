/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema.specification;

import name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema;
import name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema;

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
 * - 'ignore': the field will be ignored. When applied to an object property,
 *   the value may be missing.
 * 
 */
public final class DefaultJsonSchemaCatalog implements JsonSchemaCatalog {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.specification.JsonSchemaCatalog#getValueSchema(java.lang.String)
	 */
	@Override
	public JsonValueSchema getValueSchema(final String schemaName) {
		// TODO
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.specification.JsonSchemaCatalog#getPropertySchema(java.lang.String)
	 */
	@Override
	public JsonPropertySchema getPropertySchema(final String schemaName) {
		// TODO
		return null;
	}

}
