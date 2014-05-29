/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.console;

import java.util.concurrent.ConcurrentLinkedQueue;

import name.martingeisse.common.util.concurrency.DelayBundledWorkQueue;
import name.martingeisse.ecosim.devices.chardisplay.ICharacterDisplay;
import name.martingeisse.ecosim.devices.chardisplay.ICharacterDisplayHost;
import name.martingeisse.ecosim.devices.keyboard.IKeyboard;
import name.martingeisse.ecosim.devices.keyboard.IKeyboardHost;
import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.ipc.IIpcEventOutbox;
import name.martingeisse.webide.ipc.IpcEvent;

/**
 * The UI model for the console.
 */
public class Console implements IKeyboard, ICharacterDisplay {

	/**
	 * the eventOutbox
	 */
	private final IIpcEventOutbox eventOutbox;

	/**
	 * the outputEventThrottlingQueue
	 */
	private final DelayBundledWorkQueue<Void> outputEventThrottlingQueue;

	/**
	 * the keyboardHost
	 */
	private final IKeyboardHost keyboardHost;

	/**
	 * the displayHost
	 */
	private final ICharacterDisplayHost displayHost;

	/**
	 * the inputQueue
	 */
	private final ConcurrentLinkedQueue<Byte> inputQueue;

	/**
	 * Constructor.
	 * @param eventOutbox the event outbox
	 * @param keyboardHost the host (controller) for the keyboard
	 * @param displayHost the host (controller) for the display
	 */
	public Console(final IIpcEventOutbox eventOutbox, final IKeyboardHost keyboardHost, final ICharacterDisplayHost displayHost) {
		this.eventOutbox = eventOutbox;
		this.outputEventThrottlingQueue = new DelayBundledWorkQueue<Void>(100) {
			@Override
			protected void perform(final Object[] workUnits) {
				sendOutputEvent();
			}
		};
		this.keyboardHost = keyboardHost;
		this.displayHost = displayHost;
		this.inputQueue = new ConcurrentLinkedQueue<Byte>();
	}

	/**
	 * Publishes output data via an event.
	 */
	private void sendOutputEvent() {
		eventOutbox.sendEvent(new IpcEvent(EcosimEvents.CHARACTER_DISPLAY_OUTPUT, displayHost, null));
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.chardisplay.ICharacterDisplay#update(name.martingeisse.ecosim.devices.chardisplay.ICharacterDisplayHost, int, int)
	 */
	@Override
	public void update(final ICharacterDisplayHost host, final int x, final int y) {
		outputEventThrottlingQueue.add(null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.keyboard.IKeyboard#hasInput()
	 */
	@Override
	public boolean hasInput() {
		return false;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.keyboard.IKeyboard#receiveByte()
	 */
	@Override
	public byte receiveByte() throws IllegalStateException {
		return 0;
	}

}
