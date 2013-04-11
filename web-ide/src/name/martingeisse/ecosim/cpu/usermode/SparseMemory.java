/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu.usermode;

import name.martingeisse.ecosim.bus.BusAccessSize;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IInterruptLine;
import name.martingeisse.ecosim.bus.IPeripheralDevice;

/**
 * A 4GB memory with a "sparse", page-granular backing array.
 * 
 * Memory locations are not backed by default. Backing must be
 * requested explicitly, which is usually done using a syscall.
 * The exception is the memory range between STACK_GROW_BOUNDARY
 * (inclusive) and 0x80000000 (exclusive) -- pages in this region
 * are backed on-demand to allow a growing stack.
 */
public class SparseMemory implements IPeripheralDevice, Cloneable {

	/**
	 * the STACK_GROW_BOUNDARY
	 */
	public static final int STACK_GROW_BOUNDARY = 0x70000000;
	
	/**
	 * the contents
	 */
	private byte[][][] contents;
	
	/**
	 * the allocationBaseAddress
	 */
	private int allocationBaseAddress;

	/**
	 * Constructor
	 */
	public SparseMemory() {
		this.contents = new byte[1024][][];
		this.allocationBaseAddress = 0;
	}

	/**
	 * Getter method for the contents.
	 * @return the contents
	 */
	public byte[][][] getContents() {
		return contents;
	}
	
	/**
	 * Getter method for the allocationBaseAddress.
	 * @return the allocationBaseAddress
	 */
	public int getAllocationBaseAddress() {
		return allocationBaseAddress;
	}
	
	/**
	 * Returns the backing array for the specified address.
	 * Returns null if the address is currently not backed.
	 * @param address the address
	 * @return the page or null
	 */
	public byte[] getPage(int address) {
		int index1 = (address >> 22) & 0x3FF;
		byte[][] directory = contents[index1];
		if (directory == null) {
			return null;
		}
		int index2 = (address >> 12) & 0x3FF;
		byte[] page = directory[index2];
		return page;
	}

	/**
	 * Returns the backing array for the specified address, creating
	 * it if not yet present. Missing pages up to the requested
	 * address will not be created.
	 * 
	 * @param address the address
	 * @return the page
	 */
	public byte[] getOrCreatePage(int address) {
		
		int index1 = (address >> 22) & 0x3FF;
		byte[][] directory = contents[index1];
		if (directory == null) {
			directory = contents[index1] = new byte[1024][];
		}
		
		int index2 = (address >> 12) & 0x3FF;
		byte[] page = directory[index2];
		if (page == null) {
			page = directory[index2] = new byte[4096];
		}
		
		return page;
	}
	
	/**
	 * Returns a new page at the end of the range of already
	 * backed pages and returns its base address.
	 * @return the base address
	 */
	public int allocatePage() {
		int result = allocationBaseAddress;
		allocationBaseAddress += 4096;
		getOrCreatePage(result);
		return result;
	}

	/**
	 * Returns the value of a single byte-sized cell as an
	 * integer in the range 0-255.
	 * @param localAddress the local address of the byte cell to read.
	 * @return Returns the value of the specified cell.
	 */
	public int readByte(int localAddress) {
		byte[] page = (localAddress < STACK_GROW_BOUNDARY ? getPage(localAddress) : getOrCreatePage(localAddress));
		if (page == null) {
			throw new RuntimeException("memory fault, address = " + localAddress);
		}
		int value = page[localAddress & 4095];
		return value & 0xff;
	}
	
	/**
	 * Writes to a single byte-sized cell.
	 * @param localAddress the local address of the cell to write.
	 * @param value the value to write into the cell.
	 */
	public void writeByte(int localAddress, int value) {
		byte[] page = (localAddress < STACK_GROW_BOUNDARY ? getPage(localAddress) : getOrCreatePage(localAddress));
		if (page == null) {
			throw new RuntimeException("memory fault, address = " + localAddress);
		}
		page[localAddress & 4095] = (byte)value;
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
		return 32;
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

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#write(int, name.martingeisse.ecotools.simulator.bus.BusAccessSize, int)
	 */
	@Override
	public void write(int localAddress, BusAccessSize size, int value) throws BusTimeoutException {
		switch (size) {

		case BYTE:
			writeByte(localAddress, value);
			break;
			
		case HALFWORD:
			writeByte(localAddress, value >> 8);
			writeByte(localAddress + 1, value);
			break;
			
		case WORD:
			writeByte(localAddress, value >> 24);
			writeByte(localAddress + 1, value >> 16);
			writeByte(localAddress + 2, value >> 8);
			writeByte(localAddress + 3, value);
			break;
			
		default:
			throw new IllegalArgumentException("invalid bus access size: " + size);
			
		}		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "memory";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SparseMemory clone() {
		try {
			SparseMemory clone = (SparseMemory)super.clone();
			clone.contents = clone.contents.clone();
			for (int i=0; i<clone.contents.length; i++) {
				if (clone.contents[i] == null) {
					continue;
				}
				clone.contents[i] = clone.contents[i].clone();
				for (int j=0; j<clone.contents[i].length; j++) {
					if (clone.contents[i][j] != null) {
						clone.contents[i][j] = clone.contents[i][j].clone();
					}
				}
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
}
