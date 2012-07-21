/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.accumulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Accumulates consumed elements in a collection.
 * @param <IN> see {@link IAccumulator}
 * @param <OUT> see {@link IAccumulator}
 */
public final class CollectionAccumulator<IN, OUT extends Collection<IN>>  extends AbstractAccumulator<IN, OUT> {

	/**
	 * the out
	 */
	private final OUT out;
	
	/**
	 * Constructor.
	 * @param out the collection
	 */
	public CollectionAccumulator(OUT out) {
		this.out = out;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.accumulator.IAccumulator#consume(java.lang.Object)
	 */
	@Override
	public void consume(IN value) {
		out.add(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.accumulator.IAccumulator#getResult()
	 */
	@Override
	public OUT getResult() {
		return out;
	}

	/**
	 * Creates an accumulator that creates a list of the values passed to it.
	 * @param <IN> the type of values to accumulate
	 * @return the accumulator
	 */
	public static <IN> CollectionAccumulator<IN, List<IN>> createWithList() {
		return new CollectionAccumulator<IN, List<IN>>(new ArrayList<IN>());
	}

	/**
	 * Creates an accumulator that creates a list of the values passed to it.
	 * @param <IN> the type of values to accumulate
	 * @return the accumulator
	 */
	public static <IN> CollectionAccumulator<IN, Set<IN>> createWithSet() {
		return new CollectionAccumulator<IN, Set<IN>>(new HashSet<IN>());
	}
	
}
