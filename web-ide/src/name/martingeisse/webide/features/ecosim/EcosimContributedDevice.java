/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim;

import name.martingeisse.ecosim.bus.IPeripheralDevice;

/**
 * Wraps information about a periperal device that is contributed
 * by a model element.
 */
public final class EcosimContributedDevice {

	/**
	 * the baseAddress
	 */
	private final int baseAddress;

	/**
	 * the device
	 */
	private final IPeripheralDevice device;

	/**
	 * the interruptIndices
	 */
	private final int[] interruptIndices;

	/**
	 * Constructor.
	 * @param baseAddress the base address of the device
	 * @param device the device
	 * @param interruptIndices the interrupts used by the device
	 */
	public EcosimContributedDevice(final int baseAddress, final IPeripheralDevice device, final int[] interruptIndices) {
		this.baseAddress = baseAddress;
		this.device = device;
		this.interruptIndices = interruptIndices;
	}

	/**
	 * Getter method for the baseAddress.
	 * @return the baseAddress
	 */
	public int getBaseAddress() {
		return baseAddress;
	}

	/**
	 * Getter method for the device.
	 * @return the device
	 */
	public IPeripheralDevice getDevice() {
		return device;
	}

	/**
	 * Getter method for the interruptIndices.
	 * @return the interruptIndices
	 */
	public int[] getInterruptIndices() {
		return interruptIndices;
	}

}
