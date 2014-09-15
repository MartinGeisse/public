/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.genericbus;

import name.martingeisse.swtlib.panel.prototyping.Light;
import name.martingeisse.swtlib.panel.prototyping.PrototypingPanel;

/**
 * A {@link Light} on a prototyping panel that acts as
 * an {@link IGenericBusSlave}.
 */
public class GenericBusSlaveLight extends Light implements IGenericBusSlave {

	/**
	 * Constructor.
	 * @param prototypingPanel the prototyping panel to put this light on
	 */
	public GenericBusSlaveLight(PrototypingPanel prototypingPanel) {
		super(prototypingPanel);
		setIconState(IconState.RED);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.genericbus.IGenericBusSlave#getAddressBitCount()
	 */
	@Override
	public int getAddressBitCount() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.genericbus.IGenericBusSlave#read(int)
	 */
	@Override
	public int read(int address) {
		return (getIconState() == IconState.GREEN) ? 1 : 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.genericbus.IGenericBusSlave#write(int, int)
	 */
	@Override
	public void write(int address, int value) {
		setIconState(value == 0 ? IconState.RED : IconState.GREEN);
	}

}
