/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.blockdisplay;

import name.martingeisse.ecosim.bus.AbstractPeripheralDevice;
import name.martingeisse.ecosim.bus.IInterruptLine;

/**
 * Block display. The whole display is 640x480 pixels in size.
 * Each block is 8x8 texels in size. Each texel is 2x2 pixels in size.
 * The display therefore uses a matrix of 40x30 blocks.
 * 
 * Each block is represented by a word-sized address. Only the lowest 6 bits
 * are used to select one of 64 blocks. This makes an address range of 32x64x4
 * (= 2^5*2^6*2^2 = 2^13) bytes and 13 local address bits.
 */
public class BlockDisplay extends AbstractPeripheralDevice implements IBlockDisplayUserInterfaceSocket {

	/**
	 * the number of cells in the display
	 */
	private static final int PLANE_SIZE = 64 * 32;

	/**
	 * the matrix
	 */
	private byte[] matrix;

	/**
	 * the userInterface
	 */
	private IBlockDisplayUserInterface userInterface;

	/**
	 * Constructor
	 */
	public BlockDisplay() {
		this.matrix = new byte[PLANE_SIZE];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.blockdisplay.IBlockDisplayUserInterfaceSocket#getUserInterface()
	 */
	@Override
	public IBlockDisplayUserInterface getUserInterface() {
		return userInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.blockdisplay.IBlockDisplayUserInterfaceSocket#setUserInterface(name.martingeisse.ecotools.simulator.devices.blockdisplay.IBlockDisplayUserInterface)
	 */
	@Override
	public void setUserInterface(IBlockDisplayUserInterface userInterface) {
		this.userInterface = userInterface;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.blockdisplay.IBlockDisplayUserInterfaceSocket#getBlock(int, int)
	 */
	@Override
	public int getBlock(int x, int y) {
		x = x % 64;
		y = y % 32;
		return matrix[y * 64 + x];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.blockdisplay.IBlockDisplayUserInterfaceSocket#setBlock(int, int, int)
	 */
	@Override
	public void setBlock(int x, int y, int b) {
		x = x % 64;
		y = y % 32;
		matrix[y * 64 + x] = (byte)(b % 64);
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
//		return 13; TODO for Blocks-in-a-Row debugging
		return 20;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#readWord(int)
	 */
	@Override
	protected int readWord(int localAddress) {
		
		/** compute word index **/
		localAddress = localAddress >> 2;
		
		/** read block **/
		return matrix[localAddress % PLANE_SIZE] & 0xff;
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#writeWord(int, int)
	 */
	@Override
	protected void writeWord(int localAddress, int value) {
	
		/** compute word index **/
		localAddress = localAddress >> 2;
		
		/** write block **/
		matrix[localAddress % PLANE_SIZE] = (byte) (value % 64);

		/** update the GUI **/
		int x = localAddress % 64;
		int y = (localAddress >> 6) % 32;
		if (x < 40 && y < 30) {
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
		return "block display";
	}

}
