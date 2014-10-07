/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

/**
 * A single case (or "default" case) in a switch statement.
 */
public final class SwitchCase {

	/**
	 * the expression
	 */
	private final Expression expression;

	/**
	 * the statement
	 */
	private final Statement statement;

	/**
	 * Constructor.
	 * @param expression the expression to match
	 * @param statement the statement to execute on matching values
	 */
	public SwitchCase(final Expression expression, final Statement statement) {
		this.expression = expression;
		this.statement = statement;
	}
	
	/**
	 * Getter method for the expression.
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}
	
	/**
	 * Getter method for the statement.
	 * @return the statement
	 */
	public Statement getStatement() {
		return statement;
	}

	/**
	 * Dumps this case using the specified code dumper.
	 * @param dumper the code dumper
	 */
	public void dump(final CodeDumper dumper) {
		dumper.print("case ");
		expression.dump(dumper);
		dumper.println(": ");
		dumper.increaseIndentation();
		statement.dump(dumper);
		dumper.decreaseIndentation();
	}

	/**
	 * See {@link Statement#toJson(JsonValueBuilder)} for an explanation of this method.
	 * @param builder the JSON builder
	 */
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object();
		expression.toJson(sub.property("expression"));
		statement.toJson(sub.property("statement"));
		sub.end();
	}

}
