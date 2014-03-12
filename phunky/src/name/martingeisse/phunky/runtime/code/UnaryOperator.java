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
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(Object operand) {
			return null;
		}
		
	},
	
	/**
	 * Logically inverts the values.
	 */
	LOGICAL_NOT {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(Object operand) {
			return null;
		}
		
	};
	
	/**
	 * Applies this operator to the specified operand.
	 * @param operand the operand
	 * @return the resulting value
	 */
	public abstract Object apply(Object operand);

}
