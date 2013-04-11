/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

/**
 * This class wraps a set of 16 interrupt lines.
 */
class InterruptBus {

	/**
	 * the lines
	 */
	private DefaultInterruptLine[] lines;
	
	/**
	 * Constructor
	 */
	public InterruptBus() {
		this.lines = new DefaultInterruptLine[16];
		for (int i=0; i<16; i++) {
			lines[i] = new DefaultInterruptLine();
		}
	}
	
	/**
	 * @param index the line index
	 * @return Returns the line with that index.
	 */
	public DefaultInterruptLine getLine(int index) {
		return lines[index];
	}
	
	/**
	 * @param mask the interrupt mask. 0-bits indicate interrupt lines that are invisible even when
	 * active. 1-bits indicate visible interrupt lines whose activity can be observed normally.
	 * @return Returns the index of the smallest active interrupt line that is visible according
	 * to the specified interrupt mask, or -1 if no such line is active.
	 */
	public int getFirstActiveIndex(int mask) {
		for (int i=0; i<16; i++) {
			if ((mask & (1 << i)) != 0) {
				if (lines[i].isActive()) {
					return i;
				}
			}
		}
		return -1;
	}
	
}
