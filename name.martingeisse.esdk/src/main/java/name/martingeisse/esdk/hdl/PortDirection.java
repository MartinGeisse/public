/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl;

/**
 * The signal flow direction of a module port.
 */
public enum PortDirection {

	/**
	 * indicates that the signal flows into the module.
	 */
	IN,
	
	/**
	 * indicates that the signal flows out of the module.
	 */
	OUT,
	
	/**
	 * indicates that the signal can flow either way.
	 */
	BIDIRECTIONAL;
	
}
