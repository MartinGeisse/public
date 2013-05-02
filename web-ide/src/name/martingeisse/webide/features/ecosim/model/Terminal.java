/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.model;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

import name.martingeisse.common.util.concurrency.DelayBundledWorkQueue;
import name.martingeisse.ecosim.devices.terminal.ITerminal;
import name.martingeisse.ecosim.devices.terminal.ITerminalHost;
import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.ipc.IIpcEventOutbox;
import name.martingeisse.webide.ipc.IpcEvent;

/**
 * Implementation for {@link ITerminal}.
 */
public class Terminal implements ITerminal, Serializable {
	
	/**
	 * the eventOutbox
	 */
	private final IIpcEventOutbox eventOutbox;
	
	/**
	 * the outputEventThrottlingQueue
	 */
	private final DelayBundledWorkQueue<Void> outputEventThrottlingQueue;

	/**
	 * the host
	 */
	private ITerminalHost host;
	
	/**
	 * the outputBuilder
	 */
	private final StringBuilder outputBuilder;

	/**
	 * the inputQueue
	 */
	private final ConcurrentLinkedQueue<Byte> inputQueue;
	
	/**
	 * Constructor.
	 * @param eventOutbox the event outbox
	 */
	public Terminal(final IIpcEventOutbox eventOutbox) {
		this.eventOutbox = eventOutbox;
		this.outputEventThrottlingQueue = new DelayBundledWorkQueue<Void>(100) {
			@Override
			protected void perform(Object[] workUnits) {
				sendOutputEvent();
			}
		};
		this.host = null;
		this.outputBuilder = new StringBuilder();
		this.inputQueue = new ConcurrentLinkedQueue<Byte>();
	}
	
	/**
	 * Getter method for the host.
	 * @return the host
	 */
	public ITerminalHost getHost() {
		return host;
	}
	
	/**
	 * Setter method for the host.
	 * @param host the host to set
	 */
	public void setHost(ITerminalHost host) {
		this.host = host;
	}

	/**
	 * Sends a character to the output buffer and informs listeners.
	 * @param c the character
	 */
	public void sendCharacter(char c) {
		if (c >= 32 || c == 9 || c == 10) {
			outputBuilder.append(c);
		} else if (c == 8) {
			if (outputBuilder.length() > 0) {
				outputBuilder.setLength(outputBuilder.length() - 1);
			}
		}
		outputEventThrottlingQueue.add(null);
	}
	
	/**
	 * Publishes output data via an event.
	 */
	private void sendOutputEvent() {
		eventOutbox.sendEvent(new IpcEvent(EcosimEvents.TERMINAL_OUTPUT, this, null));
	}
	
	/**
	 * Clears all previous output.
	 */
	public void clearOutput() {
		outputBuilder.setLength(0);
	}

	/**
	 * Returns the buffered output.
	 * @return the output
	 */
	public String getOutput() {
		return outputBuilder.toString();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.terminal.ITerminal#sendByte(byte)
	 */
	@Override
	public void sendByte(final byte b) {
		sendCharacter((char)b);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.terminal.ITerminal#sendCorruptedByte()
	 */
	@Override
	public void sendCorruptedByte() {
		sendCharacter('?');
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.terminal.ITerminal#hasInput()
	 */
	@Override
	public boolean hasInput() {
		return !inputQueue.isEmpty();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.terminal.ITerminal#receiveByte()
	 */
	@Override
	public byte receiveByte() throws IllegalStateException {
		return inputQueue.remove();
	}

	/**
	 * Notifies this terminal about user input.
	 * @param input the user input
	 */
	public void notifyUserInput(String input) {
		for (int i=0; i<input.length(); i++) {
			notifyUserInput(input.charAt(i));
		}
	}
	
	/**
	 * Notifies this terminal about user input.
	 * @param input the user input
	 */
	public void notifyUserInput(char input) {
		notifyUserInput((byte)input);
	}
	
	/**
	 * Notifies this terminal about user input.
	 * @param input the user input
	 */
	public void notifyUserInput(byte input) {
		inputQueue.add(input);
		if (host != null) {
			host.onInputAvailable();
		}
	}
	
}
