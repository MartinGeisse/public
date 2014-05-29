/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.chardisplay;

import java.util.Arrays;

import name.martingeisse.ecosim.bus.AbstractPeripheralDevice;
import name.martingeisse.ecosim.bus.IInterruptLine;

/**
 * 80x30 character display. Each character is represented by a word-sized
 * address. This makes an address range of 32x128x4 (= 2^5*2^7*2^2 = 2^14) bytes
 * and 14 local address bits. The display memory is represented by two planes of
 * 32*128 = 4096 bytes each, representing characters and attributes, respectively.
 */
public class CharacterDisplayController extends AbstractPeripheralDevice implements ICharacterDisplayHost {

	/**
	 * the number of cells in the display
	 */
	private static final int PLANE_SIZE = 128 * 32;

	/**
	 * the characterPlane
	 */
	private byte[] characterPlane;

	/**
	 * the attributePlane
	 */
	private byte[] attributePlane;

	/**
	 * the display
	 */
	private ICharacterDisplay display;

	/**
	 * Constructor
	 */
	public CharacterDisplayController() {
		this.characterPlane = new byte[PLANE_SIZE];
		this.attributePlane = new byte[PLANE_SIZE];
	}

	/**
	 * Getter method for the display.
	 * @return the display
	 */
	public ICharacterDisplay getDisplay() {
		return display;
	}
	
	/**
	 * Setter method for the display.
	 * @param display the display to set
	 */
	public void setDisplay(ICharacterDisplay display) {
		this.display = display;
	}
	
	/**
	 * Clears the display memory. Note: The display is currently
	 * not notified by this method!
	 */
	public void clear() {
		Arrays.fill(characterPlane, (byte)0);
		Arrays.fill(attributePlane, (byte)0);
	}
	
	/**
	 * Initializes the display contents to demo values that show
	 * every character (using default attributes) and every attribute
	 * (using a capital 'A').
	 */
	public void initDemoValues() {
		clear();
		for (int i=0; i<16; i++) {
			for (int j=0; j<16; j++) {
				int code = j * 16 + i;
				setCharacter(i, j, code);
				setAttribute(i, j, 0x0f);
				setCharacter(i + 17, j, 'A');
				setAttribute(i + 17, j, code);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.chardisplay.ICharacterDisplayHost#getCharacter(int, int)
	 */
	@Override
	public int getCharacter(int x, int y) {
		x = x % 128;
		y = y % 32;
		return characterPlane[y * 128 + x];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.chardisplay.ICharacterDisplayHost#setCharacter(int, int, int)
	 */
	@Override
	public void setCharacter(int x, int y, int c) {
		x = x % 128;
		y = y % 32;
		characterPlane[y * 128 + x] = (byte)c;
		display.update(this, x, y);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.chardisplay.ICharacterDisplayHost#getAttribute(int, int)
	 */
	@Override
	public int getAttribute(int x, int y) {
		x = x % 128;
		y = y % 32;
		return attributePlane[y * 128 + x];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.chardisplay.ICharacterDisplayHost#setAttribute(int, int, int)
	 */
	@Override
	public void setAttribute(int x, int y, int a) {
		x = x % 128;
		y = y % 32;
		attributePlane[y * 128 + x] = (byte)a;
		display.update(this, x, y);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.bus.IPeripheralDevice#connectInterruptLines(name.martingeisse.ecosim.bus.IInterruptLine[])
	 */
	@Override
	public void connectInterruptLines(IInterruptLine[] interruptLines) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.bus.IPeripheralDevice#getInterruptLineCount()
	 */
	@Override
	public int getInterruptLineCount() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.bus.IPeripheralDevice#getLocalAddressBitCount()
	 */
	@Override
	public int getLocalAddressBitCount() {
		return 14;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.bus.AbstractPeripheralDevice#readWord(int)
	 */
	@Override
	protected int readWord(int localAddress) {
		
		/** compute word index **/
		localAddress = localAddress >> 2;
		
		/** read character **/
		int character = characterPlane[localAddress % PLANE_SIZE];
		character = character & 0xff;
		
		/** read attribute **/
		int attribute = attributePlane[localAddress % PLANE_SIZE];
		attribute = attribute & 0xff;
		
		/** pack character and attribute into the returned word **/
		return (attribute << 8) | character;
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.bus.AbstractPeripheralDevice#writeWord(int, int)
	 */
	@Override
	protected void writeWord(int localAddress, int value) {
	
		/** compute word index **/
		localAddress = localAddress >> 2;
		
		/** write character **/
		byte character = (byte) value;
		characterPlane[localAddress % PLANE_SIZE] = character;

		/** write attribute **/
		byte attribute = (byte) (value >> 8);
		attributePlane[localAddress % PLANE_SIZE] = attribute;

		/** update the GUI **/
		int x = localAddress % 128;
		int y = (localAddress >> 7) % 32;
		if (x < 80 && y < 30) {
			display.update(this, x, y);
		}

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "character display";
	}

}
