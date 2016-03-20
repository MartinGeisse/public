/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;
import name.martingeisse.phunky.runtime.variable.PhpValueArray;

import org.apache.commons.lang3.NotImplementedException;

/**
 * An expression that just returns a fixed value.
 */
public final class LiteralExpression extends AbstractComputeExpression {

	/**
	 * the value
	 */
	private final Object value;

	/**
	 * Constructor.
	 * @param value the value of this literal
	 */
	public LiteralExpression(Object value) {
		this.value = value;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumpLiteral(value, dumper);
	}
	
	/**
	 * Dumps a literal value using the specified code dumper, just as it is done for the value of a LiteralExpression.
	 * 
	 * @param value the value
	 * @param dumper the code dumper to use
	 */
	public static void dumpLiteral(Object value, CodeDumper dumper) {
		if (value == null) {
			dumper.print("null");
		} else if (value instanceof Boolean || value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double) {
			dumper.print(value.toString());
		} else if (value instanceof String) {
			String v = (String)value;
			v = v.replace("\\", "\\\\");
			v = v.replace("\"", "\\\"");
			v = v.replace("$", "\\$");
			v = v.replace("\t", "\\t");
			v = v.replace("\r", "\\r");
			v = v.replace("\n", "\\n");
			dumper.print('\"');
			dumper.print(v);
			dumper.print('\"');
		} else if (value instanceof PhpValueArray) {
			// TODO: PHP syntax cannot build an array literal directly
			// (instead it uses an array construction expression on
			// literal primitive values), but for generated code
			// there's no problem with that. dumping that could
			// just write down an array construction expression.
			// ...
			// Also needed for parameter default values and class constant initializers.
			throw new NotImplementedException("");
		} else {
			dumper.print("(!literal: " + value + " !)");
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("literal");
		if (!literalToJsonValue(value, sub.property("value"))) {
			sub.property("status").string("UNKNOWN_TYPE");
		}
		sub.end();
	}
	
	/**
	 * Converts a literal value to a JSON value, just as it is done for the value of a LiteralExpression.
	 * 
	 * This function may fail if the value is of unknown type. This is reported via the return value.
	 * 
	 * @param value the value
	 * @param builder the JSON builder to use
	 * @return true if successful, false if the value was of unknown type
	 */
	public static boolean literalToJsonValue(Object value, JsonValueBuilder<?> builder) {
		if (value == null) {
			builder.nullLiteral();
		} else if (value instanceof Boolean) {
			builder.bool((Boolean)value);
		} else if (value instanceof Integer) {
			builder.number((Integer)value);
		} else if (value instanceof Long) {
			builder.number((Long)value);
		} else if (value instanceof Float) {
			builder.number((Float)value);
		} else if (value instanceof Double) {
			builder.number((Double)value);
		} else if (value instanceof String) {
			builder.string((String)value);
		} else {
			return false;
		}
		return true;
	}

}
