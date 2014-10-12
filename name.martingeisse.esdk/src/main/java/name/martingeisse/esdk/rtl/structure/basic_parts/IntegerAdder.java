/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.rtl.structure.basic_parts;

import name.martingeisse.esdk.util.IValueSource;

/**
 * Adds integer values.
 */
public final class IntegerAdder extends BinaryOperation<Integer, Integer, Integer> {

	/**
	 * Constructor.
	 * @param leftOperand the left operand
	 * @param rightOperand the right operand
	 */
	public IntegerAdder(IValueSource<Integer> leftOperand, IValueSource<Integer> rightOperand) {
		super(leftOperand, rightOperand);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.rtl.structure.basic_parts.BinaryOperation#compute(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Integer compute(Integer leftValue, Integer rightValue) {
		return leftValue + rightValue;
	}

}
