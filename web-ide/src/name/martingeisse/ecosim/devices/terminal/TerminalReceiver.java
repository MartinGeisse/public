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
 * The receiver module of the serial line terminal.
 */
public class TerminalReceiver implements ITickable {

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
	 * the data
	 */
	private byte data;

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
	public TerminalReceiver() {
		this.ready = false;
		this.interruptEnable = false;
		this.data = 0;
		this.serialLineDelay = new MySerialLineDelay();
		this.interruptLine = null;
		this.terminal = null;
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
	 * @return Returns the data.
	 */
	public byte getData() {
		return data;
	}

	/**
	 * Sets the data.
	 * @param data the new value to set
	 */
	public void setData(byte data) {
		this.data = data;
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
			ready = false;
			updateInterrupt();
			return data & 0xff;
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
			throw new BusTimeoutException("Trying to write into terminal receiver data register");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
		serialLineDelay.tick();
	}

	/**
	 * This method is called when output data is available.
	 */
	public void onInputAvailable() {
		
		/**
		 * If the serial line is already busy receiving a byte, we will just do nothing
		 * here. The byte we have been notified about will be detected as soon as the
		 * byte being received has arrived.
		 */
		if (!serialLineDelay.isActive()) {
			startReceiving();
		}
		
	}
	
	/**
	 * This method starts receiving a byte from the user interface.
	 * The user interface must have available input and this device
	 * must be inactive (not currently receiving any input).
	 */
	private void startReceiving() throws IllegalStateException {
		
		/** sanity check: the user interface must have available input **/
		if (!terminal.hasInput()) {
			throw new IllegalStateException("no user interface input available");
		}

		/** sanity check: this receiver must not currently be receiving a byte **/
		if (serialLineDelay.isActive()) {
			throw new IllegalStateException("already receiving a byte");
		}
		
		/** put the byte into the serial line delay **/
		serialLineDelay.send(terminal.receiveByte());

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
			
			/** store the received byte so it can be fetched by the CPU **/
			data = b;
			ready = true;
			updateInterrupt();
			
			/** look if any further input is available **/
			if (terminal.hasInput()) {
				startReceiving();
			}
			
		}

	}
}
