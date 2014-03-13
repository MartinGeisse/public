/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.common.util.NotImplementedException;
import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.value.PhpArray;
import name.martingeisse.phunky.runtime.value.TypeConversionUtil;

/**
 * This enum contains all binary operators.
 */
public enum BinaryOperator {

	/**
	 * Adds the values.
	 */
	ADD {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(Object leftHandSide, Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof PhpArray) || (rightHandSide instanceof PhpArray)) {
				// TODO similar to array merge
				throw new NotImplementedException();
			} else if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				double x = TypeConversionUtil.convertToDouble(leftHandSide);
				double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x + y;
			} else {
				int x = TypeConversionUtil.convertToInt(leftHandSide);
				int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x + y;
			}
		}
		
	},
	
	/**
	 * Subtracts the values.
	 */
	SUBTRACT {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(Object leftHandSide, Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				double x = TypeConversionUtil.convertToDouble(leftHandSide);
				double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x - y;
			} else {
				int x = TypeConversionUtil.convertToInt(leftHandSide);
				int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x - y;
			}
		}
		
	},
	
	/**
	 * Multiplies the values.
	 */
	MULTIPLY {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(Object leftHandSide, Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				double x = TypeConversionUtil.convertToDouble(leftHandSide);
				double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x * y;
			} else {
				int x = TypeConversionUtil.convertToInt(leftHandSide);
				int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x * y;
			}
		}
		
	},
	
	/**
	 * Divides the values.
	 */
	DIVIDE {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(Object leftHandSide, Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				double x = TypeConversionUtil.convertToDouble(leftHandSide);
				double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x / y;
			} else {
				int x = TypeConversionUtil.convertToInt(leftHandSide);
				int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x / y;
			}
		}
		
	},
	
	/**
	 * Computes the remainder of the values.
	 */
	REMAINDER {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(Object leftHandSide, Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				double x = TypeConversionUtil.convertToDouble(leftHandSide);
				double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x % y;
			} else {
				int x = TypeConversionUtil.convertToInt(leftHandSide);
				int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x % y;
			}
		}
		
	};
	
	/**
	 * Applies this operator to the specified sub-expressions.
	 * 
	 * @param environment the environment
	 * @param leftHandSide the left-hand side expression
	 * @param rightHandSide the right-hand side expression
	 * @return the resulting value
	 */
	public Object applyToExpressions(Environment environment, Expression leftHandSide, Expression rightHandSide) {
		return applyToValues(leftHandSide.evaluate(environment), rightHandSide.evaluate(environment));
	}

	/**
	 * Applies this operator to the specified values.
	 * 
	 * Note that not all operators support this; some require to be used on expressions.
	 * Assignment operators are an example for this.
	 * 
	 * Others can be applied to values, but with different semantics than applying them
	 * to the corresponding expressions. The shortcut boolean operators are an example for
	 * this; they do not evaluate the right-hand side expression in all cases.
	 * 
	 * @param leftHandSide the left-hand side value
	 * @param rightHandSide the right-hand side value
	 * @return the resulting value
	 * @throws UnsupportedOperationException if this operator cannot be applied to values
	 */
	public Object applyToValues(Object leftHandSide, Object rightHandSide) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("operator " + this + " cannot be applied to values");
	}
	
}
