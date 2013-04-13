/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;


/**
 * This class represents the way a device is attached to the bus
 */
public class BusMapEntry {

	/**
	 * the baseAddress
	 */
	private int baseAddress;

	/**
	 * the selectorAddressMask
	 */
	private int selectorAddressMask;

	/**
	 * the localAddressMask
	 */
	private int localAddressMask;

	/**
	 * the device
	 */
	private IPeripheralDevice device;

	/**
	 * the interruptIndices
	 */
	private int[] interruptIndices;

	/**
	 * Constructor
	 * @param baseAddress the base address of this entry
	 * @param device the device that is attached at that address
	 * @param interruptIndices the interrupt indices used by this device
	 */
	BusMapEntry(int baseAddress, IPeripheralDevice device, int[] interruptIndices) {
		this.baseAddress = baseAddress;
		int localBits = device.getLocalAddressBitCount();
		this.localAddressMask = ((localBits == 32) ? 0 : (1 << localBits)) - 1;
		this.selectorAddressMask = ~localAddressMask;
		this.device = device;
		this.interruptIndices = interruptIndices;

		if ((baseAddress & localAddressMask) != 0) {
			throw new BusConfigurationException("base address contains local address bits");
		}
		if (interruptIndices.length != device.getInterruptLineCount()) {
			throw new BusConfigurationException("number of attached interrupt lines incorrect");
		}
	}

	/**
	 * @return Returns the baseAddress.
	 */
	public int getBaseAddress() {
		return baseAddress;
	}

	/**
	 * @return Returns the selectorAddressMask.
	 */
	public int getSelectorAddressMask() {
		return selectorAddressMask;
	}

	/**
	 * @return Returns the localAddressMask.
	 */
	public int getLocalAddressMask() {
		return localAddressMask;
	}

	/**
	 * @return Returns the device.
	 */
	public IPeripheralDevice getDevice() {
		return device;
	}

	/**
	 * @return Returns the interruptIndices.
	 */
	public int[] getInterruptIndices() {
		return interruptIndices;
	}

	/**
	 * @return Returns the highest address that is mapped by this entry.
	 */
	public int getAddressRangeEnd() {
		return baseAddress | localAddressMask;
	}

	/**
	 * Determines whether the specified address selects this bus map entry.
	 * @param address the address to check
	 * @return Returns true if this bus map entry maps the specified address.
	 */
	public boolean maps(int address) {
		return (address & selectorAddressMask) == baseAddress;
	}
	
	/**
	 * Extracts the device-local address for this bus map entry from the
	 * specified global address. This method simply sets all device selector
	 * bits to 0, whether the device selector part of the address selects
	 * this bus entry or not.
	 * @param address the global address for which the local address shall be extracted
	 * @return Returns the local address contained in the argument.
	 */
	public int extractLocalAddress(int address) {
		return address & localAddressMask;
	}

	/**
	 * Reads a value from the attached device. This method ignores the selector bits
	 * and assumes that this device is selected.
	 * @param address the address to read from.
	 * @param size the access size
	 * @throws BusTimeoutException if the specified local address is not connected
	 * @return Returns the value read from the device.
	 */
	public int read(int address, BusAccessSize size) throws BusTimeoutException {
		return device.read(address & localAddressMask, size);
	}

	/**
	 * Writes a value to the attached device. This method ignores the selector bits
	 * and assumes that this device is selected.
	 * @param address the address to write to.
	 * @param size the access size
	 * @param value the value to write
	 * @throws BusTimeoutException if the specified local address is not connected
	 */
	public void write(int address, BusAccessSize size, int value) throws BusTimeoutException {
		device.write(address & localAddressMask, size, value);
	}

	/**
	 * Checks whether the address range of this bus map entry overlaps the address
	 * range of the specified other entry.
	 * @param other the other entry to check
	 * @return Returns true if the ranges overlap, false if not.
	 */
	public boolean overlapsAddressRange(BusMapEntry other) {
		return getBaseAddress() <= other.getAddressRangeEnd() && other.getBaseAddress() <= getAddressRangeEnd();
	}
	
	/**
	 * Checks whether the interrupt indices of this bus map entry overlap the interrupt
	 * indices of the specified other entry.
	 * @param other the other entry to check
	 * @return Returns true if the interrupt indices overlap, false if not.
	 */
	public boolean overlapsInterruptIndices(BusMapEntry other) {
		for (int thisIndex : interruptIndices) {
			for (int otherIndex : other.getInterruptIndices()) {
				if (thisIndex == otherIndex) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Connects interrupt lines to the attached device.
	 * @param interruptBus the interrupt bus that contains the interrupt lines to connect
	 */
	void connectInterrupts(InterruptBus interruptBus) {
		IInterruptLine[] localLines = new IInterruptLine[interruptIndices.length];
		for (int i=0; i<interruptIndices.length; i++) {
			localLines[i] = interruptBus.getLine(interruptIndices[i]);
		}
		device.connectInterruptLines(localLines);
	}

}
