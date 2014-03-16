/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.Variable;

/**
 * An expression that refers to a local variable, such as $foo.
 */
public final class LocalVariableExpression extends AbstractVariableExpression {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * Constructor.
	 * @param name the name of the variable
	 */
	public LocalVariableExpression(String name) {
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
	 * @see name.martingeisse.phunky.runtime.code.Expression#getVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getVariable(Environment environment) {
		return environment.get(name);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getOrCreateVariable(Environment environment) {
		Variable variable = environment.get(name);
		if (variable == null) {
			variable = new Variable();
			environment.put(name, variable);
		}
		return variable;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "$" + name;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print('$');
		dumper.print(name);
	}
	
}
