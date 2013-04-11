/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.pixeldisplay;

import name.martingeisse.ecosim.bus.AbstractPeripheralDevice;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IInterruptLine;

/**
 * 640x480 pixel display. Each pixel is represented by a word-sized
 * address. Pixels are arranged line-by-line in the address space, without
 * any gap between lines or pixels. This makes a total local address space
 * size of 640*480*4 = 1228800 pixels. This lies between 2^20 and 2^21,
 * thus requires 21 local address bits.
 * 
 * Each pixel is written as a word whose bytes, from MSB to LSB, contain
 * the values (i, r, g, b). The MSB, i, is ignored. R, g, b are the
 * color values in the range 0..255. Pixels cannot be read back: Reading
 * from the pixel display always returns 0.
 */
public class PixelDisplay extends AbstractPeripheralDevice implements IPixelDisplayUserInterfaceSocket {

	/**
	 * the actual pixel matrix
	 */
	private int[] pixels;

	/**
	 * the userInterface
	 */
	private IPixelDisplayUserInterface userInterface;

	/**
	 * Constructor
	 */
	public PixelDisplay() {
		this.pixels = new int[640 * 480];
	}
	
	/**
	 * Ensures that the specified pixel coordinates are within the range of the
	 * pixel display, and throws an {@link IllegalArgumentException} otherwise.
	 * @param x
	 * @param y
	 */
	private void ensureWithinBounds(int x, int y) {
		if (x < 0 || y < 0 || x >= 640 || y >= 480) {
			throw new IllegalArgumentException("position not within the range of the pixel display: (" + x + ", " + y + ")");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.pixeldisplay.IPixelDisplayUserInterfaceSocket#getUserInterface()
	 */
	@Override
	public IPixelDisplayUserInterface getUserInterface() {
		return userInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.pixeldisplay.IPixelDisplayUserInterfaceSocket#setUserInterface(name.martingeisse.ecotools.simulator.devices.pixeldisplay.IPixelDisplayUserInterface)
	 */
	@Override
	public void setUserInterface(IPixelDisplayUserInterface userInterface) {
		this.userInterface = userInterface;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.pixeldisplay.IPixelDisplayUserInterfaceSocket#getPixel(int, int)
	 */
	@Override
	public int getPixel(int x, int y) {
		ensureWithinBounds(x, y);
		return pixels[y * 640 + x];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.pixeldisplay.IPixelDisplayUserInterfaceSocket#setPixel(int, int, int)
	 */
	@Override
	public void setPixel(int x, int y, int p) {
		ensureWithinBounds(x, y);
		pixels[y * 640 + x] = p;
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
		return 21;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#readWord(int)
	 */
	@Override
	protected int readWord(int localAddress) throws BusTimeoutException {
		
		/** compute word index **/
		localAddress = localAddress >> 2;
		
		/** check if in range **/
		if (localAddress >= pixels.length) {
			throw new BusTimeoutException("trying to read beyond the end of the pixel matrix");
		}
		
		/** pixels cannot be read back **/
		return 0;
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#writeWord(int, int)
	 */
	@Override
	protected void writeWord(int localAddress, int value) throws BusTimeoutException {

		/** compute word index **/
		localAddress = localAddress >> 2;
		
		/** check if in range **/
		if (localAddress >= pixels.length) {
			throw new BusTimeoutException("trying to read beyond the end of the pixel matrix");
		}
		
		/** store pixel **/
		pixels[localAddress] = value;

		/** update the GUI **/
		int x = localAddress % 640;
		int y = localAddress / 640;
		userInterface.update(this, x, y);

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
		return "pixel display";
	}

}
