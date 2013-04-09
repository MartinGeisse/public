/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ipc;

/**
 * Generic IPC event consisting of a type and arbitrary data.
 * 
 * @param <D> the data type
 */
public class IpcEvent<D> {

	/**
	 * the type
	 */
	private final String type;
	
	/**
	 * the data
	 */
	private final D data;

	/**
	 * Constructor.
	 * @param type the type
	 * @param data the data
	 */
	public IpcEvent(String type, D data) {
		this.type = type;
		this.data = data;
	}

	/**
	 * Getter method for the type.
	 * @return the type
	 */
	public final String getType() {
		return type;
	}
	
	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public final D getData() {
		return data;
	}
	
}
