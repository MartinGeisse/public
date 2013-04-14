/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ipc;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple event bus that dispatches all events to all listeners.
 */
public final class IpcEventBus implements IIpcEventOutbox, IIpcEventListener {

	/**
	 * the listeners
	 */
	private final List<IIpcEventListener> listeners = new ArrayList<IIpcEventListener>();
	
	/**
	 * Constructor.
	 */
	public IpcEventBus() {
	}
	
	/**
	 * Adds a listener to this bus.
	 * @param listener the listener to add
	 */
	public synchronized void addListener(IIpcEventListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener from this bus.
	 * @param listener the listener to remove
	 */
	public synchronized void removeListener(IIpcEventListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Dispatches the specified event to all listeners.
	 * @param event the event
	 */
	public synchronized void dispatch(IpcEvent event) {
		for (IIpcEventListener listener : listeners) {
			listener.handleEvent(event);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ipc.IIpcEventListener#handleEvent(name.martingeisse.webide.ipc.IpcEvent)
	 */
	@Override
	public void handleEvent(IpcEvent event) {
		dispatch(event);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ipc.IIpcEventListener#getMetadata()
	 */
	@Override
	public EventListenerMetadata getMetadata() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ipc.IIpcEventOutbox#sendEvent(name.martingeisse.webide.ipc.IpcEvent)
	 */
	@Override
	public void sendEvent(IpcEvent event) {
		dispatch(event);
	}
	
}
