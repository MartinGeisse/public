/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.connect;

/**
 * This interface is implemented by components that consume a value.
 * It is the high-level equivalent of an HDL signal consumer.
 * 
 * This class is active (pull-oriented) in the sense that it initiates
 * a value transfer on its own behalf. The corresponding value source
 * must be set first, and this class will then pull a value from it
 * when needed.
 * 
 * @param <T> the value type
 */
public interface IActiveValueSink<T> extends IConnectable {

	/**
	 * Getter method for the valueSource.
	 * @return the valueSource
	 */
	public IPassiveValueSource<T> getValueSource();

	/**
	 * Setter method for the valueSource.
	 * @param valueSource the valueSource to set
	 */
	public void setValueSource(IPassiveValueSource<T> valueSource);
	
}
