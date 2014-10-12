/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.rtl.structure.basic_parts;

import name.martingeisse.esdk.util.BitVector;
import name.martingeisse.esdk.util.IValueSource;

/**
 * Adds {@link BitVector} values.
 */
public final class BitVectorAdder extends BinaryOperation<BitVector, BitVector, BitVector> {

	/**
	 * Constructor.
	 * @param leftOperand the left operand
	 * @param rightOperand the right operand
	 */
	public BitVectorAdder(IValueSource<BitVector> leftOperand, IValueSource<BitVector> rightOperand) {
		super(leftOperand, rightOperand);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.rtl.structure.basic_parts.BinaryOperation#compute(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected BitVector compute(BitVector leftValue, BitVector rightValue) {
		if (leftValue.getSize() != rightValue.getSize()) {
			throw new RuntimeException("trying to add bit vectors of different size");
		}
		return new BitVector(leftValue.getSize(), leftValue.getLongValueSigned() + rightValue.getLongValueSigned());
	}

}
