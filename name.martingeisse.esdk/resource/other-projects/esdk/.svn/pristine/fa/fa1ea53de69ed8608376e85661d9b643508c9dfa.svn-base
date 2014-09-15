/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.connect;

/**
 * This interface is implemented by components that produce a value.
 * It is the high-level equivalent of an HDL signal.
 * 
 * This class is active (push-oriented) in the sense that it initiates
 * a value transfer on its own behalf. The corresponding value sink
 * must be set first, and this class will then push a value to it
 * when needed.
 * 
 * @param <T> the value type
 */
public interface IActiveValueSource<T> extends IConnectable {

	/**
	 * Getter method for the valueSink.
	 * @return the valueSink
	 */
	public IPassiveValueSink<T> getValueSink();

	/**
	 * Setter method for the valueSink.
	 * @param valueSink the valueSink to set
	 */
	public void setValueSink(IPassiveValueSink<T> valueSink);
	
}
