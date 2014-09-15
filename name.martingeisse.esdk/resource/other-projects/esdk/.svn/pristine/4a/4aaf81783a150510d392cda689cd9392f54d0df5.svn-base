/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.connect;

/**
 * A specialized {@link IPassiveValueSource} that never changes its
 * value on its own. Instead, the produceValue() method must be
 * called to change the current value to the next value.
 * 
 * Cycle order: Clients should call produceValue() first, then
 * getValue(). Clients may call getValue() again and should expect
 * the same value to re-appear. This well-defined order helps
 * both clients and implementations to maintain proper semantics.
 * 
 * This rule also implies that the initial value has no useful
 * meaning. Clients should not read it. Implementations should
 * return a valid value without any further meaning to satisfy
 * consumers that routinely call getValue() (such as RTL logic).
 * 
 * @param <T> the value type
 */
public interface IPassiveValueStreamSource<T> extends IPassiveValueSource<T> {

	/**
	 * Produces the next value.
	 */
	public void produceValue();
	
}
