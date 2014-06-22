/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.operator;

/**
 * This enum contains all cast operators.
 */
public enum CastOperator {

	/**
	 * Represents (int) and (integer) casts.
	 */
	INTEGER("int") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.CastOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
		}

	},
	
	/**
	 * Represents (bool) and (boolean) casts.
	 */
	BOOLEAN("bool") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.CastOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
		}
		
	},
	
	/**
	 * Represents (float), (double) and (real) casts.
	 */
	FLOAT("float") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.CastOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
		}
		
	},

	/**
	 * Represents (string) casts.
	 */
	STRING("string") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.CastOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
		}
		
	},
	
	/**
	 * Represents (array) casts.
	 */
	ARRAY("array") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.CastOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
		}
		
	},
	
	/**
	 * Represents (object) casts.
	 */
	OBJECT("object") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.CastOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
		}
		
	},
	
	/**
	 * Represents (unset) casts. Note that this turns a value
	 * to NULL; it does not unset any variable.
	 */
	NULL("unset") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.CastOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
		}
		
	};

	/**
	 * the typeName
	 */
	private final String typeName;

	/**
	 * Constructor.
	 * @param typeName the type to cast to
	 */
	private CastOperator(final String typeName) {
		this.typeName = typeName;
	}

	/**
	 * Getter method for the typeName.
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Applies this operator to the specified operand.
	 * @param operand the operand
	 * @return the resulting value
	 */
	public abstract Object apply(Object operand);

}
