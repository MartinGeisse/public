/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import org.apache.commons.lang3.StringUtils;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.assignment.AssignmentTarget;
import name.martingeisse.phunky.runtime.assignment.LocalVariableAssignmentTarget;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;
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
	
	/**
	 * Determines the effective name by resolving the indirections except
	 * the last one.
	 * 
	 * @param environment the environment
	 * @return the effective name
	 */
	private String getEffectiveName(Environment environment) {
		String effectiveName = name;
		for (int i=1; i<indirections; i++) {
			Variable variable = environment.get(effectiveName);
			if (variable == null) {
				environment.getRuntime().triggerError("no such local variable in variable indirection: " + effectiveName, getLocation());
				return null;
			}
			effectiveName = TypeConversionUtil.convertToString(variable.getValue());
		}
		return effectiveName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#getVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable resolveVariable(Environment environment) {
		// TODO there's no such thing as "get variable but don't create",
		// neither for array elements nor for local variables. Using a
		// non-existing variable as a reference target creates it.
		return environment.get(getEffectiveName(environment));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable resolveOrCreateVariable(Environment environment) {
		return environment.getOrCreate(getEffectiveName(environment));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#resolveAssignmentTarget(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public AssignmentTarget resolveAssignmentTarget(Environment environment) {
		return new LocalVariableAssignmentTarget(environment, getEffectiveName(environment));
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
