/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.accumulator;

/**
 * Ignores consumed values and returns null.
 * 
 * @param <IN> see {@link IAccumulator}
 * @param <OUT> see {@link IAccumulator}
 */
public final class NullAccumulator<IN, OUT> extends AbstractAccumulator<IN, OUT> {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.accumulator.IAccumulator#consume(java.lang.Object)
	 */
	@Override
	public void consume(IN value) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.accumulator.IAccumulator#getResult()
	 */
	@Override
	public OUT getResult() {
		return null;
	}

}
