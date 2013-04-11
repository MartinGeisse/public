/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.chardisplay;

import name.martingeisse.ecosim.bus.AbstractPeripheralDevice;
import name.martingeisse.ecosim.bus.IInterruptLine;

/**
 * 80x30 character display. Each character is represented by a word-sized
 * address. This makes an address range of 32x128x4 (= 2^5*2^7*2^2 = 2^14) bytes
 * and 14 local address bits. The display memory is represented by two planes of
 * 32*128 = 4096 bytes each, representing characters and attributes, respectively.
 */
public class CharacterDisplay extends AbstractPeripheralDevice implements ICharacterDisplayUserInterfaceSocket {

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
	 * the userInterface
	 */
	private ICharacterDisplayUserInterface userInterface;

	/**
	 * Constructor
	 */
	public CharacterDisplay() {
		this.characterPlane = new byte[PLANE_SIZE];
		this.attributePlane = new byte[PLANE_SIZE];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.chardisplay.ICharacterDisplayUserInterfaceSocket#getUserInterface()
	 */
	@Override
	public ICharacterDisplayUserInterface getUserInterface() {
		return userInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.chardisplay.ICharacterDisplayUserInterfaceSocket#setUserInterface(name.martingeisse.ecotools.simulator.devices.chardisplay.ICharacterDisplayUserInterface)
	 */
	@Override
	public void setUserInterface(ICharacterDisplayUserInterface userInterface) {
		this.userInterface = userInterface;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.chardisplay.ICharacterDisplayUserInterfaceSocket#getCharacter(int, int)
	 */
	@Override
	public int getCharacter(int x, int y) {
		x = x % 128;
		y = y % 32;
		return characterPlane[y * 128 + x];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.chardisplay.ICharacterDisplayUserInterfaceSocket#setCharacter(int, int, int)
	 */
	@Override
	public void setCharacter(int x, int y, int c) {
		x = x % 128;
		y = y % 32;
		characterPlane[y * 128 + x] = (byte)c;
		userInterface.update(this, x, y);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.chardisplay.ICharacterDisplayUserInterfaceSocket#getAttribute(int, int)
	 */
	@Override
	public int getAttribute(int x, int y) {
		x = x % 128;
		y = y % 32;
		return attributePlane[y * 128 + x];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.chardisplay.ICharacterDisplayUserInterfaceSocket#setAttribute(int, int, int)
	 */
	@Override
	public void setAttribute(int x, int y, int a) {
		x = x % 128;
		y = y % 32;
		attributePlane[y * 128 + x] = (byte)a;
		userInterface.update(this, x, y);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#connectInterruptLines(name.martingeisse.ecotools.simulator.bus.InterruptLine[])
	 */
	@Override
	public void connectInterruptLines(IInterruptLine[] interruptLines) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#getInterruptLineCount()
	 */
	@Override
	public int getInterruptLineCount() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#getLocalAddressBitCount()
	 */
	@Override
	public int getLocalAddressBitCount() {
		return 14;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#readWord(int)
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
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#writeWord(int, int)
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
			userInterface.update(this, x, y);
		}

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
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
