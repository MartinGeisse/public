/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.array;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.Variable;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractVariableExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.value.PhpArray;
import name.martingeisse.phunky.runtime.value.TypeConversionUtil;

/**
 * This expression selects one element of an array.
 */
public final class ArrayElementExpression extends AbstractVariableExpression {

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
		if (arrayCandidate instanceof PhpArray) {
			PhpArray array = (PhpArray)arrayCandidate;
			return array.getVariable(key).getValue();
		} else {
			environment.getRuntime().triggerError("trying to get element of non-array value");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getVariable(final Environment environment) {

		// TODO this wont support empty() correctly
		// -> new interface EmptyExpressionAwareExpression
		
		// note that arrays are a value type, so getting the variable for an element also gets the variable for the array
		Variable arrayVariable = arrayExpression.getVariable(environment);
		String key = TypeConversionUtil.convertToString(keyExpression.evaluate(environment));
		if (arrayVariable == null) {
			return null;
		}
		Object arrayCandidate = arrayVariable.getValue();
		if (arrayCandidate instanceof PhpArray) {
			PhpArray array = (PhpArray)arrayCandidate;
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
			CREATE ARRAY IMPLICITLY
			return null;
		}
		Object arrayCandidate = arrayVariable.getValue();
		if (arrayCandidate instanceof PhpArray) {
			PhpArray array = (PhpArray)arrayCandidate;
			CREATE ELEMENT
			return array.getVariable(key);
		}
		if (valueCanBeOverwrittenByImplicitArrayConstruction(arrayCandidate)) {
			CREATE ARRAY IMPLICITLY

		} else {
			environment.getRuntime().triggerError("cannot use a scalar value as an array");
			return null;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#isEmpty(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public boolean isEmpty(Environment environment) {
		TODO
		return false;
	}

	/**
	 * This method is used when creating an array by setting an element in a non-array
	 * variable. It takes the value currently stored in the variable, and checks
	 * whether that value allows creating an array this way.
	 */
	private boolean valueCanBeOverwrittenByImplicitArrayConstruction(Object value) {
		if (value == null) {
			return true;
		}
		if (value instanceof Boolean) {
			Boolean b = (Boolean)value;
			return (b.booleanValue() == false);
		}
		return false;
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
