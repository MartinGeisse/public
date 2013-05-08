/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.webide.features.ecosim.debugout;

import name.martingeisse.common.util.concurrency.DelayBundledWorkQueue;
import name.martingeisse.ecosim.bus.AbstractPeripheralDevice;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IInterruptLine;
import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.ipc.IIpcEventOutbox;
import name.martingeisse.webide.ipc.IpcEvent;

/**
 * A simple device that writes the lowest 8 bits of each
 * word-sized bus write to a file on the simulator host system
 * or any other output stream.
 */
public class DebugOutputController extends AbstractPeripheralDevice {

	/**
	 * the eventOutbox
	 */
	private final IIpcEventOutbox eventOutbox;
	
	/**
	 * the outputEventThrottlingQueue
	 */
	private final DelayBundledWorkQueue<Void> outputEventThrottlingQueue;
	
	/**
	 * the builder
	 */
	private final StringBuilder builder;
	
	/**
	 * Constructor
	 * @param eventOutbox the event outbox
	 */
	public DebugOutputController(final IIpcEventOutbox eventOutbox) {
		this.eventOutbox = eventOutbox;
		this.outputEventThrottlingQueue = new DelayBundledWorkQueue<Void>(100) {
			@Override
			protected void perform(Object[] workUnits) {
				sendOutputEvent();
			}
		};
		this.builder = new StringBuilder();
	}
	
	/**
	 * Getter method for the builder.
	 * @return the builder
	 */
	public StringBuilder getBuilder() {
		return builder;
	}

	/**
	 * Returns the buffered output.
	 * @return the output
	 */
	public String getOutput() {
		return builder.toString();
	}
	
	/**
	 * Publishes output data via an event.
	 */
	private void sendOutputEvent() {
		eventOutbox.sendEvent(new IpcEvent(EcosimEvents.DEBUG_OUTPUT, this, null));
	}
	
	/**
	 * Clears all previous output.
	 */
	public void clearOutput() {
		builder.setLength(0);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#readWord(int)
	 */
	@Override
	protected int readWord(int localAddress) throws BusTimeoutException {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#writeWord(int, int)
	 */
	@Override
	protected void writeWord(int localAddress, int value) throws BusTimeoutException {
		builder.append((char)value);
		outputEventThrottlingQueue.add(null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#connectInterruptLines(name.martingeisse.ecotools.simulator.bus.IInterruptLine[])
	 */
	@Override
	public void connectInterruptLines(IInterruptLine[] interruptLines) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getInterruptLineCount()
	 */
	@Override
	public int getInterruptLineCount() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getLocalAddressBitCount()
	 */
	@Override
	public int getLocalAddressBitCount() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#dispose()
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "output device";
	}

}
