/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.array;

import name.martingeisse.phunky.runtime.Environment;
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
			return array.getValueOrError(environment.getRuntime(), key);
		} else if (arrayCandidate instanceof String) {
			String s = (String)arrayCandidate;
			int index;
			try {
				index = Integer.parseInt(key);
			} catch (NumberFormatException e) {
				environment.getRuntime().triggerError("Illegal string offset '" + key + "'");
				return null;
			}
			if (index < 0 || index >= s.length()) {
				return "";
			} else {
				return s.substring(index, index + 1);
			}
		} else {
			environment.getRuntime().triggerError("trying to get element of non-array value");
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
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getVariable(final Environment environment) {
		// TODO there's no such thing as "get variable but don't create",
		// neither for array elements nor for local variables. Using a
		// non-existing variable as a reference target creates it.
		
		Pair<PhpVariableArray, String> variableArrayAndKey = obtainVariableArrayAndKey(environment);
		if (variableArrayAndKey == null) {
			environment.getRuntime().triggerError("trying to get element of non-array value");
			return null;
		} else {
			PhpVariableArray variableArray = variableArrayAndKey.getLeft();
			String key = variableArrayAndKey.getRight();
			return variableArray.getVariable(key);
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getOrCreateVariable(final Environment environment) {
		Pair<PhpVariableArray, String> variableArrayAndKey = obtainVariableArrayAndKey(environment);
		if (variableArrayAndKey == null) {
			return null;
		} else {
			PhpVariableArray variableArray = variableArrayAndKey.getLeft();
			String key = variableArrayAndKey.getRight();
			return variableArray.getOrCreateVariable(key);
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#bindVariableReference(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.variable.Variable)
	 */
	@Override
	public void bindVariableReference(Environment environment, Variable variable) {
		Pair<PhpVariableArray, String> variableArrayAndKey = obtainVariableArrayAndKey(environment);
		if (variableArrayAndKey != null) {
			PhpVariableArray variableArray = variableArrayAndKey.getLeft();
			String key = variableArrayAndKey.getRight();
			if (variable == null) {
				variableArray.removeVariable(key);
			} else {
				variableArray.setVariable(key, variable);
			}
		}
	}
	
	/**
	 * 
	 */
	private Pair<PhpVariableArray, String> obtainVariableArrayAndKey(Environment environment) {
		// note that arrays are a value type, so getting the variable for an element also gets the variable for the array
		Variable arrayVariable = arrayExpression.getOrCreateVariable(environment);
		String key = TypeConversionUtil.convertToString(keyExpression.evaluate(environment));
		if (arrayVariable == null) {
			environment.getRuntime().triggerError("cannot use this expression as an array: " + arrayExpression);
			return null;
		}
		return Pair.of(arrayVariable.getVariableArray(environment.getRuntime()), key);
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
