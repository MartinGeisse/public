/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.value;

/**
 * This parser parses a textual representation of a value of
 * type T and creates an instance of that type from it.
 *
 * @param <T> the type of parsed values
 */
public interface ValueParser<T> {

	/**
	 * Parses the specified value.
	 * 
	 * @param value the textual value
	 * @return the parsed value
	 */
	public T parse(String value);

}
