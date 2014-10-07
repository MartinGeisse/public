/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression;

import org.apache.commons.lang3.StringUtils;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * An expression that refers to a local variable, such as $foo.
 * Can handle multiple indirections, such as $$foo.
 */
public final class LocalVariableExpression extends AbstractVariableExpression {

	/**
	 * the name
	 */
	private final String name;
	
	/**
	 * the indirections
	 */
	private final int indirections;

	/**
	 * Constructor.
	 * @param name the name of the variable
	 * @param indirections the number of indirections
	 */
	public LocalVariableExpression(String name, int indirections) {
		this.name = name;
		this.indirections = indirections;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter method for the indirections.
	 * @return the indirections
	 */
	public int getIndirections() {
		return indirections;
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
		return environment.getOrCreate(name);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtils.repeat('$', indirections) + name;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print(toString());
	}
	
	/**
	 * Parses a local variable specification.
	 * @param specification the specification, such as "$$foo"
	 * @return the local variable expression
	 */
	public static LocalVariableExpression parse(String specification) {
		int indirections = 0;
		while (specification.charAt(indirections) == '$') {
			indirections++;
		}
		return new LocalVariableExpression(specification.substring(indirections), indirections);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		if (indirections == 1) {
			builder.object().property("type").string("localVariable").property("name").string(name).end();
		} else {
			builder.object().property("type").string("multiIndirectionLocalVariable").property("name").string(name).property("indirections").number(indirections).end();
		}
	}

}
