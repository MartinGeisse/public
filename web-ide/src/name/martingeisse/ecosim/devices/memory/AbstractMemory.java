/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import name.martingeisse.ecosim.bus.BusAccessSize;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IInterruptLine;
import name.martingeisse.ecosim.bus.IPeripheralDevice;

/**
 * Common superclass for RAM and ROM devices.
 */
public abstract class AbstractMemory implements IPeripheralDevice {

	/**
	 * the localAddressBits
	 */
	private int localAddressBits;
	
	/**
	 * the contents
	 */
	private byte[] contents;

	/**
	 * Constructor
	 * @param localAddressBits the number of memory-local address bits. This
	 * automatically determines the memory's size.
	 */
	public AbstractMemory(int localAddressBits) {
		this.localAddressBits = localAddressBits;
		this.contents = new byte[1 << localAddressBits];
	}

	/**
	 * @return Returns the contents.
	 */
	public byte[] getContents() {
		return contents;
	}
	
	/**
	 * Returns the value of a single byte-sized cell as an
	 * integer in the range 0-255.
	 * @param localAddress the local address of the byte cell to read.
	 * @return Returns the value of the specified cell.
	 */
	public int readByte(int localAddress) {
		int value = contents[localAddress];
		return value & 0xff;
	}
	
	/**
	 * Writes to a single byte-sized cell.
	 * @param localAddress the local address of the cell to write.
	 * @param value the value to write into the cell.
	 */
	public void writeByte(int localAddress, int value) {
		contents[localAddress] = (byte)value;
	}

	/**
	 * Clears the whole memory to zero-bytes.
	 */
	public void clearContents() {
		Arrays.fill(contents, (byte) 0);
	}

	/**
	 * Initializes the whole memory to random values.
	 */
	public void randomizeContents() {
		Random random = new Random();
		random.nextBytes(contents);
	}

	/**
	 * Initializes the memory contents from the specified file.
	 * @param file the file to load.
	 * @throws IOException on I/O errors
	 */
	public void readContentsFromFile(File file) throws IOException {
		FileInputStream is = new FileInputStream(file);
		int position = 0;
		while (position < contents.length) {
			int count = is.read(contents, position, contents.length - position);
			if (count == -1) {
				break;
			}
			position += count;
		}
		is.close();
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
		return localAddressBits;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#read(int, name.martingeisse.ecotools.simulator.bus.BusAccessSize)
	 */
	@Override
	public int read(int localAddress, BusAccessSize size) throws BusTimeoutException {
		switch (size) {

		case BYTE:
			return readByte(localAddress);
			
		case HALFWORD:
			return (readByte(localAddress) << 8) + readByte(localAddress + 1);
			
		case WORD:
			return (readByte(localAddress) << 24) + (readByte(localAddress + 1) << 16) + (readByte(localAddress + 2) << 8) + readByte(localAddress + 3);
			
		default:
			throw new IllegalArgumentException("invalid bus access size: " + size);
			
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#dispose()
	 */
	@Override
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
	}

}
