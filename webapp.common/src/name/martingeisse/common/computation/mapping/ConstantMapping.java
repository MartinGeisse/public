/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.common.computation.mapping;

/**
 * This mapping always returns the same result.
 */
public final class ConstantMapping<IN, OUT> implements IMapping<IN, OUT> {

	/**
	 * the value
	 */
	private final OUT value;

	/**
	 * Constructor.
	 * @param value the value to return
	 */
	public ConstantMapping(OUT value) {
		this.value = value;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public OUT getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.mapping.IMapping#map(java.lang.Object)
	 */
	@Override
	public OUT map(IN inputValue) {
		return value;
	}
	
	/**
	 * Factory method that allows to create an instance of this class without
	 * specifying type parameters explicitly.
	 * 
	 * @param value the constant value to return
	 */
	public static <IN, OUT> IMapping<IN, OUT> of(OUT value) {
		return new ConstantMapping<IN, OUT>(value);
	}

}
