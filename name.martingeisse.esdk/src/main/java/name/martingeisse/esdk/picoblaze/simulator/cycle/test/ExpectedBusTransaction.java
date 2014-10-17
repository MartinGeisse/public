/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;

import org.junit.Assert;

/**
 * A single expected bus transaction during a simulation. This
 * milestone expects a transaction of the specified type (read or
 * write) to be issued for the specified address. For write
 * transactions, the data written by the program under test is also
 * checked to be the expected value. For read operations, the
 * data specified in this object is returned to the simulation.
 */
public final class ExpectedBusTransaction implements SimulationMilestone {

	/**
	 * the write
	 */
	private final boolean write;

	/**
	 * the address
	 */
	private final int address;

	/**
	 * the data
	 */
	private final int data;

	/**
	 * Constructor.
	 * @param write whether the transaction is expected to be a write
	 * @param address the expected address
	 * @param data the expected data (for writes) or the data to return
	 * to the simulation (for reads)
	 */
	public ExpectedBusTransaction(boolean write, int address, int data) {
		this.write = write;
		this.address = expectByte(address, "address");
		this.data = expectByte(data, "data");
	}

	/**
	 * 
	 */
	private static int expectByte(int value, String name) {
		if ((value & 0xff) != value) {
			throw new IllegalArgumentException("invalid byte value for " + name + ": " + value);
		}
		return value;
	}

	/**
	 * Getter method for the write.
	 * @return the write
	 */
	public boolean isWrite() {
		return write;
	}

	/**
	 * Getter method for the address.
	 * @return the address
	 */
	public int getAddress() {
		return address;
	}

	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public int getData() {
		return data;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.cycle.test.SimulationMilestone#notifyVisibleAsNextMilestone()
	 */
	@Override
	public boolean notifyVisibleAsNextMilestone() {
		return false;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.cycle.test.SimulationMilestone#handleBusTransaction(boolean, int, int)
	 */
	@Override
	public int handleBusTransaction(boolean write, int address, int data) {
		if (this.write != write || this.address != address || (write && this.data != data)) {
			ExpectedBusTransaction actual = new ExpectedBusTransaction(write, address, data);
			Assert.fail("expected bus transaction [" + this + "], actual: [" + actual + "]");
		}
		return (write ? 0 : this.data);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (write) {
			return String.format("write 0x%2h to 0x%2h", data, address); 
		} else {
			return String.format("read 0x%2h", address);
		}
	}
	
}
