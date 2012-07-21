/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.accumulator;

import java.util.Iterator;

/**
 * This interface defines an object that accumulates values passed to
 * it and produces a result from them.
 * 
 * Calling code should be aware that passing elements to this accumulator
 * after calling getResult() might invalidate the earlier result. Client
 * should create a copy of the result before passing additional elements
 * if they need it later.
 *  
 * @param <IN> the element type passed to this accumulator
 * @param <OUT> the result type produced by this accumulator
 */
public interface IAccumulator<IN, OUT> {

	/**
	 * Consumes the specified value.
	 * @param value the value to consume
	 */
	public void consume(IN value);

	/**
	 * Consumes the specified values.
	 * @param values the values to consume
	 */
	public void consumeAll(Iterable<IN> values);

	/**
	 * Consumes the specified values. Note that this method
	 * leaves the iterator at the end of its life cycle.
	 * 
	 * @param values the values to consume
	 */
	public void consumeAll(Iterator<IN> values);
	
	/**
	 * Returns the result of this accumulator.
	 * @return the result
	 */
	public OUT getResult();
	
}
