/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code;

/**
 * This enum contains all binary operators.
 */
public enum BinaryOperator {

	/**
	 * Adds the values.
	 */
	ADD {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#apply(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object apply(Object leftHandSide, Object rightHandSide) {
			return null;
		}
		
	},
	
	/**
	 * Subtracts the values.
	 */
	SUBTRACT {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#apply(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object apply(Object leftHandSide, Object rightHandSide) {
			return null;
		}
		
	},
	
	/**
	 * Multiplies the values.
	 */
	MULTIPLY {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#apply(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object apply(Object leftHandSide, Object rightHandSide) {
			return null;
		}
		
	},
	
	/**
	 * Divides the values.
	 */
	DIVIDE {

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
