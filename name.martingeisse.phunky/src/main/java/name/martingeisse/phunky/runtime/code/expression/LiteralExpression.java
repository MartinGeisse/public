/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
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
		if (value == null) {
			dumper.print("null");
		} else if (value instanceof Boolean || value instanceof Integer || value instanceof Float || value instanceof Double) {
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
			// PHP syntax cannot build an array literal directly
			// (instead it uses an array construction expression on
			// literal primitive values), but for generated code
			// there's no problem with that. dumping that could
			// just write down an array construction expression.
			throw new NotImplementedException("");
		} else {
			dumper.print("(!literal: " + value + " !)");
		}
	}
	
}
