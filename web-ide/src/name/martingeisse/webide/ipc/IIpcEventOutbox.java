/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ipc;


/**
 * This interface is passed to event sources at initialization.
 * Whenever the source produces an event, it uses this outbox
 * for dispatch.
 */
public interface IIpcEventOutbox {

	/**
	 * Sends an event.
	 * @param event the event to send
	 */
	public void sendEvent(IpcEvent event);
	
}
