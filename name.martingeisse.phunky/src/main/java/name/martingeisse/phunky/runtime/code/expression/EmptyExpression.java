/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * Represents the empty(...) special form.
 */
public class EmptyExpression extends AbstractCallExpression {

	/**
	 * Constructor.
	 * @param parameters the parameter expressions
	 */
	public EmptyExpression(final Expression[] parameters) {
		super(parameters);
		if (parameters.length != 1) {
			throw new IllegalArgumentException("empty() requires exactly one parameter");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		return TypeConversionUtil.empty(getParameter(0).evaluateForEmptyCheck(environment));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("empty(");
		getParameter(0).dump(dumper);
		dumper.print(")");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("empty");
		getParameter(0).toJson(sub.property("expression"));
		sub.end();
	}

}
