/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.terminal;

import name.martingeisse.ecosim.bus.AbstractPeripheralDevice;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IInterruptLine;

/**
 * The terminal device.
 */
public class Terminal extends AbstractPeripheralDevice implements ITerminalUserInterfaceSocket {

	/**
	 * the userInterface
	 */
	private ITerminalUserInterface userInterface;

	/**
	 * the receiver
	 */
	private TerminalReceiver receiver;

	/**
	 * the transmitter
	 */
	private TerminalTransmitter transmitter;

	/**
	 * Constructor
	 */
	public Terminal() {
		this.userInterface = null;
		this.receiver = new TerminalReceiver();
		this.transmitter = new TerminalTransmitter();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
		receiver.tick();
		transmitter.tick();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#connectInterruptLines(name.martingeisse.ecotools.simulator.bus.IInterruptLine[])
	 */
	@Override
	public void connectInterruptLines(IInterruptLine[] interruptLines) {
		receiver.connectInterruptLine(interruptLines[0]);
		transmitter.connectInterruptLine(interruptLines[1]);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getInterruptLineCount()
	 */
	@Override
	public int getInterruptLineCount() {
		return 2;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getLocalAddressBitCount()
	 */
	@Override
	public int getLocalAddressBitCount() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.terminal.ITerminalUserInterfaceSocket#getUserInterface()
	 */
	@Override
	public ITerminalUserInterface getUserInterface() {
		return userInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.terminal.ITerminalUserInterfaceSocket#setUserInterface(name.martingeisse.ecotools.simulator.devices.terminal.ITerminalUserInterface)
	 */
	@Override
	public void setUserInterface(ITerminalUserInterface userInterface) {
		this.userInterface = userInterface;
		receiver.setUserInterface(userInterface);
		transmitter.setUserInterface(userInterface);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.devices.terminal.ITerminalUserInterfaceSocket#onInputAvailable()
	 */
	@Override
	public void onInputAvailable() {
		receiver.onInputAvailable();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#readWord(int)
	 */
	@Override
	protected int readWord(int localAddress) throws BusTimeoutException {
		if (localAddress < 8) {
			return receiver.readWord(localAddress);
		} else {
			return transmitter.readWord(localAddress & 7);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#writeWord(int, int)
	 */
	@Override
	protected void writeWord(int localAddress, int value) throws BusTimeoutException {
		if (localAddress < 8) {
			receiver.writeWord(localAddress, value);
		} else {
			transmitter.writeWord(localAddress & 7, value);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "terminal";
	}

}
