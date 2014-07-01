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
import name.martingeisse.phunky.runtime.variable.PhpVariableArray;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;
import name.martingeisse.phunky.runtime.variable.Variable;

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
		if (arrayCandidate instanceof PhpVariableArray) {
			PhpVariableArray array = (PhpVariableArray)arrayCandidate;
			return array.getVariable(key).getValue();
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
		if (arrayCandidate instanceof PhpVariableArray) {
			PhpVariableArray array = (PhpVariableArray)arrayCandidate;
			return array.getVariable(key).getValue();
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getVariable(final Environment environment) {

		// note that arrays are a value type, so getting the variable for an element also gets the variable for the array
		Variable arrayVariable = arrayExpression.getVariable(environment);
		String key = TypeConversionUtil.convertToString(keyExpression.evaluate(environment));
		if (arrayVariable == null) {
			return null;
		}
		Object arrayCandidate = arrayVariable.getValue();
		if (arrayCandidate instanceof PhpVariableArray) {
			PhpVariableArray array = (PhpVariableArray)arrayCandidate;
			return array.getVariable(key);
		}
		environment.getRuntime().triggerError("trying to get element of non-array value");
		return null;
		
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getOrCreateVariable(final Environment environment) {
		
		// note that arrays are a value type, so getting the variable for an element also gets the variable for the array
		Variable arrayVariable = arrayExpression.getVariable(environment);
		String key = TypeConversionUtil.convertToString(keyExpression.evaluate(environment));
		if (arrayVariable == null) {
			PhpVariableArray array = new PhpVariableArray();
			return array.getOrCreateVariable(key);
		}
		Object arrayCandidate = arrayVariable.getValue();
		if (arrayCandidate instanceof PhpVariableArray) {
			PhpVariableArray array = (PhpVariableArray)arrayCandidate;
			return array.getOrCreateVariable(key);
		}
		if (TypeConversionUtil.valueCanBeOverwrittenByImplicitArrayConstruction(arrayCandidate)) {
			PhpVariableArray array = new PhpVariableArray();
			return array.getOrCreateVariable(key);
		} else {
			environment.getRuntime().triggerError("cannot use a scalar value as an array");
			return null;
		}
		
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

}
