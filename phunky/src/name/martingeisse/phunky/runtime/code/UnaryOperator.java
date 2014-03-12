/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code;

/**
 * This enum contains all unary operators.
 */
public enum UnaryOperator {

	/**
	 * Negates the values.
	 */
	NEGATE {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#apply(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object apply(Object leftHandSide, Object rightHandSide) {
			return null;
		}
		
	},
	
	/**
	 * Logically inverts the values.
	 */
	LOGICAL_NOT {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#apply(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object apply(Object leftHandSide, Object rightHandSide) {
			return null;
		}
		
	};
	
	/**
	 * Applies this operator to the specified values.
	 * @param leftHandSide the left-hand side
	 * @param rightHandSide the right-hand side
	 * @return the resulting value
	 */
	public abstract Object apply(Object leftHandSide, Object rightHandSide);

}
