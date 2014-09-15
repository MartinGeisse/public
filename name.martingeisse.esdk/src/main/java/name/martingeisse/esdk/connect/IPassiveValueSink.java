/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.connect;

/**
 * This interface is implemented by components that consume a value.
 * It is the high-level equivalent of an HDL signal consumer.
 * 
 * This class is passive (push-oriented) in the sense that the corresponding
 * value source must initiate a value transfer.
 * 
 * Pushing the same value multiple times should not have any side-effects.
 * 
 * @param <T> the value type
 */
public interface IPassiveValueSink<T> extends IConnectable {

	/**
	 * Sets the new value.
	 * @param value the value to set
	 */
	public void setValue(T value);
	
}
