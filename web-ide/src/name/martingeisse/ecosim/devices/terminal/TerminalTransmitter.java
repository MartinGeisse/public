/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.terminal;

import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IInterruptLine;
import name.martingeisse.ecosim.timer.ITickable;
import name.martingeisse.ecosim.util.AbstractValueTransportDelay;

/**
 * The transmitter module of the serial line terminal.
 */
public class TerminalTransmitter implements ITickable {

	/**
	 * The number of ticks needed to receive a byte.
	 */
	public static final int TICKS_PER_BYTE = 3;

	/**
	 * the ready
	 */
	private boolean ready;

	/**
	 * the interruptEnable
	 */
	private boolean interruptEnable;

	/**
	 * the serialLineDelay
	 */
	private MySerialLineDelay serialLineDelay;

	/**
	 * the interruptLine
	 */
	private IInterruptLine interruptLine;

	/**
	 * the terminal
	 */
	private ITerminal terminal;

	/**
	 * Constructor
	 */
	public TerminalTransmitter() {
		this.ready = true;
		this.interruptEnable = false;
		this.serialLineDelay = new MySerialLineDelay();
		this.interruptLine = null;
	}

	/**
	 * 
	 */
	private void updateInterrupt() {
		interruptLine.setActive(ready & interruptEnable);
	}

	/**
	 * @return Returns the ready.
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * Sets the ready.
	 * @param ready the new value to set
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
		updateInterrupt();
	}

	/**
	 * @return Returns the interruptEnable.
	 */
	public boolean isInterruptEnable() {
		return interruptEnable;
	}

	/**
	 * Sets the interruptEnable.
	 * @param interruptEnable the new value to set
	 */
	public void setInterruptEnable(boolean interruptEnable) {
		this.interruptEnable = interruptEnable;
		updateInterrupt();
	}

	/**
	 * Getter method for the terminal.
	 * @return the terminal
	 */
	public ITerminal getTerminal() {
		return terminal;
	}
	
	/**
	 * Setter method for the terminal.
	 * @param terminal the terminal to set
	 */
	public void setTerminal(ITerminal terminal) {
		this.terminal = terminal;
	}

	/**
	 * Connects the interrupt line to this device.
	 * @param interruptLine the interrupt line to connect.
	 */
	public void connectInterruptLine(IInterruptLine interruptLine) {
		this.interruptLine = interruptLine;
	}

	/**
	 * Reads a word from this device.
	 * @param localAddress the receiver-local address
	 * @return Returns the word read from the specified address.
	 * @throws BusTimeoutException on bus timeout
	 */
	public int readWord(int localAddress) throws BusTimeoutException {
		if (localAddress == 0) {
			return (ready ? 1 : 0) | (interruptEnable ? 2 : 0);
		} else {
			throw new BusTimeoutException("Trying to read from terminal transmitter data register");
		}
	}

	/**
	 * Writes a word to this device.
	 * @param localAddress the receiver-local address
	 * @param value the value to write
	 * @throws BusTimeoutException on bus timeout
	 */
	public void writeWord(int localAddress, int value) throws BusTimeoutException {
		if (localAddress == 0) {
			ready = (value & 1) != 0;
			interruptEnable = (value & 2) != 0;
			updateInterrupt();
		} else {
			ready = false;
			updateInterrupt();
			startTransmission((byte)value);
		}
	}
	
	/**
	 * Starts transmitting a byte. The user interface must accept
	 * bytes at any time, so it cannot pose a problem here. If the
	 * serial line is still busy sending a byte, it will be delivered
	 * to the user interface as a corrupted byte.
	 */
	private void startTransmission(byte data) {
		if (serialLineDelay.isActive()) {
			terminal.sendCorruptedByte();
			serialLineDelay.cancel();
		}
		serialLineDelay.send(data);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
		serialLineDelay.tick();
	}

	/**
	 * The serial line delay implementation for this class.
	 */
	private class MySerialLineDelay extends AbstractValueTransportDelay<Byte> {

		/**
		 * Constructor
		 */
		public MySerialLineDelay() {
			super(TICKS_PER_BYTE);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.devices.terminal.AbstractSerialLineDelay#onArrive(byte)
		 */
		@Override
		protected void onArrive(Byte b) {
			ready = true;
			updateInterrupt();
			terminal.sendByte(b);
		}

	}

}
