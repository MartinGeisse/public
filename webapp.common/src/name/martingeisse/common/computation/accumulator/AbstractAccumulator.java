/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.accumulator;

import java.util.Iterator;

/**
 * Base implementation for {@link IAccumulator}.
 * @param <IN> see {@link IAccumulator}
 * @param <OUT> see {@link IAccumulator}
 */
public abstract class AbstractAccumulator<IN, OUT> implements IAccumulator<IN, OUT> {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.accumulator.IAccumulator#consumeAll(java.lang.Iterable)
	 */
	@Override
	public void consumeAll(Iterable<IN> values) {
		consumeAll(values.iterator());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.accumulator.IAccumulator#consumeAll(java.util.Iterator)
	 */
	@Override
	public void consumeAll(Iterator<IN> values) {
		while (values.hasNext()) {
			consume(values.next());
		}
	}
	
}
