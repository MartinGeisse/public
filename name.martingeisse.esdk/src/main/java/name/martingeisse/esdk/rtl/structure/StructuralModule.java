/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.rtl.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * A module that is defined by its structure, not by behavior.
 * 
 * Components are defined by invoking helper methods of this class
 * or by instantiating them directly. There is usually no need
 * to store the components in instance variables, unless you
 * also define a behavioral part that has to access signals
 * of the structural part.
 * 
 * Interfacing with the outside world is done by creating ports
 * in the module. These are stored automatically by this base
 * class for synthesis. However, subclasses typically provide
 * typed getter methods for them as well.
 */
public class StructuralModule {

	/**
	 * the ports
	 */
	private final List<Port<?, ?>> ports = new ArrayList<Port<?, ?>>();

	/**
	 * Getter method for the ports.
	 * @return the ports
	 */
	public final List<Port<?, ?>> getPorts() {
		return ports;
	}

}
