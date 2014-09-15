/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.connect;

/**
 * This interface is implemented by components that produce a value.
 * It is the high-level equivalent of an HDL signal.
 * 
 * This class is passive (pull-oriented) in the sense that the corresponding
 * value sink must initiate a value transfer.
 * 
 * Pulling a value multiple times should not have any side-effects.
 * 
 * @param <T> the value type
 */
public interface IPassiveValueSource<T> extends IConnectable {

	/**
	 * @return Returns the current value.
	 */
	public T getValue();
	
}
