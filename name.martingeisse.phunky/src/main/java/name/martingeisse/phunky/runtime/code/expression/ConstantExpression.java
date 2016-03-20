/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

/**
 * An expression that refers to a defined constant, such as FOO.
 */
public final class ConstantExpression extends AbstractComputeExpression {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * Constructor.
	 * @param name the name of the variable
	 */
	public ConstantExpression(String name) {
		this.name = name;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		
		// optimization: perform hash lookup only once, except for the special case when the value is null
		Object value = environment.getRuntime().getConstants().get(name);
		if (value == null) {
			// constant is undefined or defined with null value
			if (environment.getRuntime().getConstants().containsKey(name)) {
				// constant is defined with null value
				return null;
			} else {
				// constant is undefined
				return environment.getRuntime().onUndefinedConstant(this);
			}
		} else {
			// constant is defined with non-null value
			return value;
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print(name);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		builder.object().property("type").string("constant").property("name").string(name).end();
	}
	
}
