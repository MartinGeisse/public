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

	/**
	 * the negativeAllowed
	 */
	private final boolean negativeAllowed;
	
	/**
	 * the zeroAllowed
	 */
	private final boolean zeroAllowed;
	
	/**
	 * Constructor.
	 * @param negativeAllowed whether negative numbers are allowed by this schema
	 * @param zeroAllowed whether zero is allowed by this schema
	 */
	public JsonIntegerSchema(boolean negativeAllowed, boolean zeroAllowed) {
		this.negativeAllowed = negativeAllowed;
		this.zeroAllowed = zeroAllowed;
	}

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
		if (!negativeAllowed && value.getValue() < 0) {
			validationReport.addError(value, "negative value not allowed here");
			return value;
		} else if (!zeroAllowed && value.getValue() == 0) {
			validationReport.addError(value, "zero not allowed here");
			return value;
		} else {
			return handleInteger(value, validationReport);
		}
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
