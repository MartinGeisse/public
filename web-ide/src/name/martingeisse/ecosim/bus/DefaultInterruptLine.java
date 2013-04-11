/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

/**
 * Implementation class for interrupt lines
 */
public class DefaultInterruptLine implements IInterruptLine {

	/**
	 * the active
	 */
	private boolean active;

	/**
	 * Constructor
	 */
	public DefaultInterruptLine() {
		this.active = false;
	}

	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 * @param active the new value to set
	 */
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

}
