/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.array;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.assignment.ArrayElementAssignmentTarget;
import name.martingeisse.phunky.runtime.assignment.AssignmentTarget;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractComputeExpression;
import name.martingeisse.phunky.runtime.code.expression.AbstractExpression;
import name.martingeisse.phunky.runtime.code.expression.AbstractVariableExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;
import name.martingeisse.phunky.runtime.variable.PhpValueArray;
import name.martingeisse.phunky.runtime.variable.PhpVariableArray;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;
import name.martingeisse.phunky.runtime.variable.Variable;

import org.apache.commons.lang3.tuple.Pair;

/**
 * This expression selects one element of an array.
 * 
 * This class does not inherit from either {@link AbstractVariableExpression} or
 * {@link AbstractComputeExpression} because it can behave like either
 * one, depending on the context in which it is used. 
 */
public final class ArrayElementExpression extends AbstractExpression {

	/**
	 * the arrayExpression
	 */
	private final Expression arrayExpression;

	/**
	 * the keyExpression
	 */
	private final Expression keyExpression;

	/**
	 * Constructor.
	 * @param arrayExpression the expression that determines the array
	 * @param keyExpression the expression that determines the key
	 */
	public ArrayElementExpression(final Expression arrayExpression, final Expression keyExpression) {
		this.arrayExpression = arrayExpression;
		this.keyExpression = keyExpression;
	}

	/**
	 * Getter method for the arrayExpression.
	 * @return the arrayExpression
	 */
	public Expression getArrayExpression() {
		return arrayExpression;
	}

	/**
	 * Getter method for the keyExpression.
	 * @return the keyExpression
	 */
	public Expression getKeyExpression() {
		return keyExpression;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.AbstractVariableExpression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		Object arrayCandidate = arrayExpression.evaluate(environment);
		String key = TypeConversionUtil.convertToString(keyExpression.evaluate(environment));
		if (arrayCandidate instanceof PhpValueArray) {
			PhpValueArray array = (PhpValueArray)arrayCandidate;
			return array.getValueOrError(environment.getRuntime(), getLocation(), key);
		} else if (arrayCandidate instanceof String) {
			String s = (String)arrayCandidate;
			long index;
			try {
				index = Long.parseLong(key);
			} catch (NumberFormatException e) {
				environment.getRuntime().triggerError("Illegal string offset '" + key + "'", keyExpression.getLocation());
				return null;
			}
			if (index < 0 || index >= s.length()) {
				return "";
			} else {
				int intIndex = (int)index;
				return s.substring(intIndex, intIndex + 1);
			}
		} else {
			environment.getRuntime().triggerError("trying to get element of non-array value", getLocation());
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluateForEmptyCheck(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluateForEmptyCheck(Environment environment) {
		Object arrayCandidate = arrayExpression.evaluateForEmptyCheck(environment);
		String key = TypeConversionUtil.convertToString(keyExpression.evaluate(environment));
		if (arrayCandidate instanceof PhpValueArray) {
			PhpValueArray array = (PhpValueArray)arrayCandidate;
			return array.getValue(key);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#resolveAssignmentTarget(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public AssignmentTarget resolveAssignmentTarget(Environment environment) {
		Pair<Variable, String> variableArrayAndKey = obtainArrayVariableAndKey(environment);
		if (variableArrayAndKey == null) {
			environment.getRuntime().triggerError("trying to get element of non-array value", getLocation());
			return null;
		} else {
			Variable arrayVariable = variableArrayAndKey.getLeft();
			String key = variableArrayAndKey.getRight();
			return new ArrayElementAssignmentTarget(array, key);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable resolveOrCreateVariable(final Environment environment) {
		Pair<Variable, String> variableArrayAndKey = obtainArrayVariableAndKey(environment);
		if (variableArrayAndKey == null) {
			return null;
		} else {
			PhpVariableArray variableArray = variableArrayAndKey.getLeft().getVariableArray(environment.getRuntime(), getLocation());
			String key = variableArrayAndKey.getRight();
			return variableArray.getOrCreateVariable(key);
		}
	}

	/**
	 * 
	 */
	private Pair<Variable, String> obtainArrayVariableAndKey(Environment environment) {
		// note that arrays are a value type, so getting the variable for an element also gets the variable for the array
		Variable arrayVariable = arrayExpression.resolveOrCreateVariable(environment);
		String key = TypeConversionUtil.convertToString(keyExpression.evaluate(environment));
		if (arrayVariable == null) {
			environment.getRuntime().triggerError("cannot use this expression as an array: " + arrayExpression, arrayExpression.getLocation());
			return null;
		}
		return Pair.of(arrayVariable, key);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(final CodeDumper dumper) {
		arrayExpression.dump(dumper);
		dumper.print('[');
		keyExpression.dump(dumper);
		dumper.print(']');
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("arrayElement");
		arrayExpression.toJson(sub.property("array"));
		keyExpression.toJson(sub.property("key"));
		sub.end();
	}

}
