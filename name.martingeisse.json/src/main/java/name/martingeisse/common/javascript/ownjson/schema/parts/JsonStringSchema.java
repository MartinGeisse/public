/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.schema.parts;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstString;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.schema.AbstractJsonValueOrPropertySchema;
import name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport;

/**
 * Checks that a value is of string type. Also allows some
 * additional string-related checks. Subclasses may define
 * further rules.
 */
public class JsonStringSchema extends AbstractJsonValueOrPropertySchema {

	/**
	 * the emptyAllowed
	 */
	private final boolean emptyAllowed;
	
	/**
	 * Constructor.
	 * @param emptyAllowed whether the empty string is allowed as a value
	 */
	public JsonStringSchema(boolean emptyAllowed) {
		this.emptyAllowed = emptyAllowed;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.schema.JsonValueSchema#validateAndNormalizeValue(name.martingeisse.common.javascript.ownjson.ast.JsonAstValue, name.martingeisse.common.javascript.ownjson.schema.JsonValidationReport)
	 */
	@Override
	public final JsonAstValue validateAndNormalizeValue(JsonAstValue value, JsonValidationReport validationReport) {
		if (value instanceof JsonAstString) {
			return handleStringInternal((JsonAstString)value, validationReport);
		} else {
			validationReport.addWrongTypeError(value, "string");
			return new JsonAstString(value, "");
		}
	}

	/**
	 * 
	 */
	private final JsonAstValue handleStringInternal(JsonAstString value, JsonValidationReport validationReport) {
		if (!emptyAllowed && value.getValue().isEmpty()) {
			validationReport.addError(value, "empty string not allowed here");
			return value;
		} else {
			return handleString(value, validationReport);
		}
	}
	
	/**
	 * This method allows subclasses to define additional schema rules. The default implementation
	 * just returns the value.
	 * 
	 * This method is only called when the basic rules defined for this base class have
	 * already been validated successfully.
	 * 
	 * @param value the string value
	 * @param validationReport the validation report
	 * @return the value to use
	 */
	protected JsonAstValue handleString(JsonAstString value, JsonValidationReport validationReport) {
		return value;
	}
	
}
