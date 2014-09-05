/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.schema.parts;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstInteger;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.schema.AbstractJsonValueOrPropertySchema;
import name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport;

/**
 * Checks that a value is of integer type. Also allows some
 * additional integer-related checks. Subclasses may define
 * further rules.
 */
public class JsonIntegerSchema extends AbstractJsonValueOrPropertySchema {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema#validateAndNormalizeValue(name.martingeisse.common.javascript.ownjson.ast.JsonAstValue, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
	 */
	@Override
	public final JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport) {
		if (value instanceof JsonAstInteger) {
			return handleIntegerInternal((JsonAstInteger)value, validationReport);
		} else {
			validationReport.addWrongTypeError(value, "integer");
			return new JsonAstInteger(value, 0);
		}
	}

	/**
	 * 
	 */
	private final JsonAstValue handleIntegerInternal(JsonAstInteger value, JsonValidationReport validationReport) {
		return handleInteger(value, validationReport);
	}
	
	/**
	 * This method allows subclasses to define additional schema rules. The default implementation
	 * just returns the value.
	 * 
	 * This method is only called when the basic rules defined for this base class have
	 * already been validated successfully.
	 * 
	 * @param value the integer value
	 * @param validationReport the validation report
	 * @return the value to use
	 */
	protected JsonAstValue handleInteger(JsonAstInteger value, JsonValidationReport validationReport) {
		return value;
	}
	
}
