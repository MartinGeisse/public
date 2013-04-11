/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.ecosim.timer.ITickable;

/**
 * This class represents the bus that connects the CPU to peripheral devices.
 */
public class Bus implements IBusMasterAccess, ITickable {

	/**
	 * the peripheralDevices
	 */
	private List<BusMapEntry> mapEntries;

	/**
	 * the bus map. This is simply an array-ized version of the above list for faster access.
	 */
	private BusMapEntry[] map;

	/**
	 * the interruptBus
	 */
	private InterruptBus interruptBus;

	/**
	 * Constructor
	 */
	public Bus() {
		this.mapEntries = new ArrayList<BusMapEntry>();
		this.interruptBus = new InterruptBus();
	}

	/**
	 * Adds a peripheral device.
	 * @param baseAddress the base address at which the device shall be attached
	 * @param device the device to add
	 * @param interruptIndices the interrupt indices used by this device
	 */
	public void add(int baseAddress, IPeripheralDevice device, int[] interruptIndices) {
		if (map != null) {
			throw new IllegalStateException("bus map has already been inialized");
		}
		mapEntries.add(new BusMapEntry(baseAddress, device, interruptIndices));
	}

	/**
	 * This method must be invoked after all devices have been added to
	 * initialize the bus map.
	 */
	public void buildBusMap() {

		/** ensure that the bus map has not yet been built **/
		if (map != null) {
			throw new IllegalStateException("bus map has already been inialized");
		}

		/** create the bus map **/
		map = mapEntries.toArray(new BusMapEntry[mapEntries.size()]);

		/** ensure that the map entries are non-overlapping **/
		for (BusMapEntry e1 : map) {
			for (BusMapEntry e2 : map) {
				if (e1 != e2) {
					if (e1.overlapsAddressRange(e2)) {
						map = null;
						throw new BusConfigurationException("bus map entries have overlapping addresses");
					}
					if (e1.overlapsInterruptIndices(e2)) {
						map = null;
						throw new BusConfigurationException("bus map entries have overlapping interrupt indices");
					}
				}
			}
		}

		/** connect interrupt lines **/
		for (BusMapEntry e : map) {
			e.connectInterrupts(interruptBus);
		}

	}

	/**
	 * Returns the bus map entry for the specified bus address,
	 * or null if no device is attached to that address.
	 * @param address the address to check
	 * @return Returns the bus map entry for that address, or null if no such entry exists.
	 */
	public BusMapEntry getBusMapEntryForAddress(int address) {
		for (BusMapEntry e : map) {
			if (e.maps(address)) {
				return e;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IBusMasterAccess#read(int, name.martingeisse.ecotools.simulator.bus.BusAccessSize)
	 */
	@Override
	public int read(int address, BusAccessSize size) throws BusErrorException {

		/** ensure that the address is aligned **/
		if (!size.isAligned(address)) {
			throw new MisalignedBusAccessException(address, size);
		}

		/** look for a device that is attached to this address **/
		BusMapEntry e = getBusMapEntryForAddress(address);
		if (e == null) {
			throw new BusTimeoutException(address);
		} else {
			return e.read(address, size);
		}

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IBusMasterAccess#write(int, name.martingeisse.ecotools.simulator.bus.BusAccessSize, int)
	 */
	@Override
	public void write(int address, BusAccessSize size, int value) throws BusErrorException {

		/** ensure that the address is aligned **/
		if (!size.isAligned(address)) {
			throw new MisalignedBusAccessException(address, size);
		}

		/** look for a device that is attached to this address **/
		BusMapEntry e = getBusMapEntryForAddress(address);
		if (e == null) {
			throw new BusTimeoutException(address);
		} else {
			e.write(address, size, value);
		}

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IBusMasterAccess#getActiveInterrupt()
	 */
	@Override
	public int getActiveInterrupt(int mask) {
		return interruptBus.getFirstActiveIndex(mask);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
		for (BusMapEntry entry : map) {
			entry.getDevice().tick();
		}
	}

}
