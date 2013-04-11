/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.keyboard;

import name.martingeisse.ecosim.bus.AbstractPeripheralDevice;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IInterruptLine;
import name.martingeisse.ecosim.util.AbstractValueTransportDelay;

/**
 * Keyboard device. To simulate delayed arrival of multiple scan codes,
 * this device works very similar to the serial terminal receiver.
 */
public class Keyboard extends AbstractPeripheralDevice implements IKeyboardUserInterfaceSocket {

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
	 * the receiveDelay
	 */
	private MyReceiveDelay receiveDelay;

	/**
	 * the interruptLine
	 */
	private IInterruptLine interruptLine;

	/**
	 * the userInterface
	 */
	private IKeyboardUserInterface userInterface;

	/**
	 * Constructor
	 */
	public Keyboard() {
		this.ready = false;
		this.interruptEnable = false;
		this.data = 0;
		this.receiveDelay = new MyReceiveDelay();
		this.interruptLine = null;
		this.userInterface = null;
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
	 * @return Returns the userInterface.
	 */
	@Override
	public IKeyboardUserInterface getUserInterface() {
		return userInterface;
	}

	/**
	 * Sets the userInterface.
	 * @param userInterface the new value to set
	 */
	@Override
	public void setUserInterface(IKeyboardUserInterface userInterface) {
		this.userInterface = userInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#readWord(int)
	 */
	@Override
	public int readWord(int localAddress) throws BusTimeoutException {
		if (localAddress == 0) {
			return (ready ? 1 : 0) | (interruptEnable ? 2 : 0);
		} else {
			ready = false;
			updateInterrupt();
			return data & 0xff;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#writeWord(int, int)
	 */
	@Override
	public void writeWord(int localAddress, int value) throws BusTimeoutException {
		if (localAddress == 0) {
			ready = (value & 1) != 0;
			interruptEnable = (value & 2) != 0;
			updateInterrupt();
		} else {
			throw new BusTimeoutException("Trying to write to the keyboard data register");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#connectInterruptLines(name.martingeisse.ecotools.simulator.bus.InterruptLine[])
	 */
	@Override
	public void connectInterruptLines(IInterruptLine[] interruptLines) {
		this.interruptLine = interruptLines[0];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#getInterruptLineCount()
	 */
	@Override
	public int getInterruptLineCount() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#getLocalAddressBitCount()
	 */
	@Override
	public int getLocalAddressBitCount() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
		receiveDelay.tick();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "keyboard";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.keyboard.IKeyboardUserInterfaceSocket#onInputAvailable()
	 */
	@Override
	public void onInputAvailable() {
		
		/**
		 * If the keyboard is already busy receiving a byte, we will just do nothing
		 * here. The byte we have been notified about will be detected as soon as the
		 * byte being received has arrived.
		 */
		if (!receiveDelay.isActive()) {
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
		if (!userInterface.hasInput()) {
			throw new IllegalStateException("no user interface input available");
		}

		/** sanity check: this keyboard must not currently be receiving a byte **/
		if (receiveDelay.isActive()) {
			throw new IllegalStateException("already receiving a byte");
		}
		
		/** put the byte into the receive delay **/
		receiveDelay.send(userInterface.receiveByte());

	}
	
	/**
	 * The receive delay implementation for this class.
	 */
	private class MyReceiveDelay extends AbstractValueTransportDelay<Byte> {

		/**
		 * Constructor
		 */
		public MyReceiveDelay() {
			super(TICKS_PER_BYTE);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.AbstractValueTransportDelay#onArrive(java.lang.Object)
		 */
		@Override
		protected void onArrive(Byte b) {
			
			/** store the received byte so it can be fetched by the CPU **/
			data = b;
			ready = true;
			updateInterrupt();
			
			/** look if any further input is available **/
			if (userInterface.hasInput()) {
				startReceiving();
			}
			
		}

	}
}
