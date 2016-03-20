/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.operator;

import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

import org.apache.commons.lang3.NotImplementedException;

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
			return TypeConversionUtil.convertToInteger(operand);
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
			return TypeConversionUtil.convertToBoolean(operand);
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
			return TypeConversionUtil.convertToDouble(operand);
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
			return TypeConversionUtil.convertToString(operand);
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
			throw new NotImplementedException("");
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
			throw new NotImplementedException("");
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
