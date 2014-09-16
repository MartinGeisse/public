/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.hdl.probe.formatter;

/**
 * This interface is able to turn a value into a string. It is
 * used to output values to a simulation log.
 *
 * @param <T> the type of value this formatter can handle
 */
public interface ValueFormatter<T> {

	/**
	 * Converts a value to a string.
	 * 
	 * @param value the value
	 * @return the formatted value
	 */
	public String formatValue(T value);
	
}
