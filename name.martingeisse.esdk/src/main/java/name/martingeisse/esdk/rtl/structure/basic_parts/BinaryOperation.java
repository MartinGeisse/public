/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.rtl.structure.basic_parts;

import name.martingeisse.esdk.util.IValueSource;

/**
 * Base class for binary operations such as an adder, AND gate, or similar.
 * 
 * This base implementation delegates to a subclass method to compute the
 * operation if both operands are provided. If either operand is null,
 * this base class by default returns null too (subclasses can override
 * this behavior).
 *
 * @param <L> the type of the left operand
 * @param <R> the type of the right operand
 * @param <V> the type of the resulting value
 */
public abstract class BinaryOperation<L, R, V> implements IValueSource<V> {

	/**
	 * the leftOperand
	 */
	private IValueSource<L> leftOperand;

	/**
	 * the rightOperand
	 */
	private IValueSource<R> rightOperand;

	/**
	 * Constructor.
	 * @param leftOperand the left operand
	 * @param rightOperand the right operand
	 */
	public BinaryOperation(IValueSource<L> leftOperand, IValueSource<R> rightOperand) {
		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
	}

	/**
	 * Getter method for the leftOperand.
	 * @return the leftOperand
	 */
	public final IValueSource<L> getLeftOperand() {
		return leftOperand;
	}

	/**
	 * Setter method for the leftOperand.
	 * @param leftOperand the leftOperand to set
	 */
	public final void setLeftOperand(IValueSource<L> leftOperand) {
		this.leftOperand = leftOperand;
	}

	/**
	 * Getter method for the rightOperand.
	 * @return the rightOperand
	 */
	public final IValueSource<R> getRightOperand() {
		return rightOperand;
	}

	/**
	 * Setter method for the rightOperand.
	 * @param rightOperand the rightOperand to set
	 */
	public final void setRightOperand(IValueSource<R> rightOperand) {
		this.rightOperand = rightOperand;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.util.IValueSource#getValue()
	 */
	@Override
	public V getValue() {
		L leftValue = leftOperand.getValue();
		R rightValue = rightOperand.getValue();
		if (leftValue == null || rightValue == null) {
			return null;
		} else {
			return compute(leftValue, rightValue);
		}
	}

	/**
	 * Computes the operation.
	 * @param leftValue the value of the left operand signal
	 * @param rightValue the value of the right operand signal
	 * @return the resulting value
	 */
	protected abstract V compute(L leftValue, R rightValue);
	
}
