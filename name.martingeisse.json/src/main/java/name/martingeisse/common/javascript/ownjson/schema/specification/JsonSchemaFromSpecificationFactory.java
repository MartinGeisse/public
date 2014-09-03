/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema.specification;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.schema.JsonPropertySchema;
import name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema;

/**
 * Builds a {@link JsonValueSchema} or {@link JsonPropertySchema} from
 * its specification.
 */
public final class JsonSchemaFromSpecificationFactory {

	/**
	 * Builds a JSON value schema from its specification.
	 * 
	 * The specification should be valid according to {@link JsonValueSchemaSpecificationSchema},
	 * but it need not be normalized.
	 * 
	 * @param specification the specification
	 * @return the JSON value schema
	 */
	public JsonValueSchema buildValueSchema(JsonAstValue specification) {
		// TODO

	}

	/**
	 * Builds a JSON property schema from its specification.
	 * 
	 * The specification should be valid according to {@link JsonPropertySchemaSpecificationSchema},
	 * but it need not be normalized.
	 * 
	 * @param specification the specification
	 * @return the JSON property schema
	 */
	public JsonPropertySchema buildPropertySchema(JsonAstValue specification) {
		// TODO
		
	}

}
