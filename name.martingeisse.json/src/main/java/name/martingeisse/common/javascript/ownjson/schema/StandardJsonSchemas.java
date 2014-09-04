/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstNull;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;

/**
 * Holds standard schema implementations as flyweight singletons.
 */
public final class StandardJsonSchemas {

	/**
	 * Prevent instantiation.
	 */
	private StandardJsonSchemas() {
	}

	// TODO

	/**
	 * A value schema that just normalizes all values to null.
	 */
	public static final JsonValueSchema IGNORED_VALUE = new JsonValueSchema() {
		@Override
		public JsonAstValue validateAndNormalize(final JsonAstValue value, final JsonValidationReport validationReport) {
			return new JsonAstNull(value);
		}
	};
	
	/**
	 * A property schema that just normalizes all properties away.
	 */
	public static final JsonValueSchema IGNORED_PROPERTY = new JsonValueSchema() {
		@Override
		public JsonAstValue validateAndNormalize(final JsonAstValue value, final JsonValidationReport validationReport) {
			return null;
		}
	};
	
	/**
	 * A value schema that accepts any value and leaves it "as is".
	 */
	public static final JsonValueSchema ANY_VALUE = new JsonValueSchema() {
		@Override
		public JsonAstValue validateAndNormalize(final JsonAstValue value, final JsonValidationReport validationReport) {
			return value;
		}
	};

	/**
	 * A value schema that accepts any 
	 */
	public static final JsonValueSchema BOOLEAN = new JsonValueSchema() {
		@Override
		public JsonAstValue validateAndNormalize(final JsonAstValue value, final JsonValidationReport validationReport) {
			return value;
		}
	};

	
	
}
