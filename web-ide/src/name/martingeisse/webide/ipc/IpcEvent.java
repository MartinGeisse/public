/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ipc;

/**
 * Generic IPC event consisting of a type, source object and
 * arbitrary data.
 * 
 * Note: The source and data fields do not use type parameters
 * because the decoupled nature of IPC events makes (compile-time)
 * type parameters pretty useless.
 */
public final class IpcEvent {

	/**
	 * the type
	 */
	private final String type;

	/**
	 * the source
	 */
	private final Object source;
	
	/**
	 * the data
	 */
	private final Object data;

	/**
	 * Constructor.
	 * @param type the type
	 * @param source the source
	 * @param data the data
	 */
	public IpcEvent(String type, Object source, Object data) {
		this.type = type;
		this.source = source;
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
	 * Getter method for the source.
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}
	
	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public final Object getData() {
		return data;
	}
	
}
