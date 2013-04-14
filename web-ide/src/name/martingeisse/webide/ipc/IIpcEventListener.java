/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ipc;

/**
 * This interface is implemented by code that wants to be
 * notified about events.
 *
 */
public interface IIpcEventListener {

	/**
	 * Notifies this event listener about an incoming event.
	 * @param event the event
	 */
	public void handleEvent(IpcEvent event);
	
	/**
	 * Provides meta-data about this listener. This method can
	 * be used by dispatcher logic to handle the listener
	 * properly.
	 * 
	 * This method may return null for listeners that do not
	 * provide meta-data. This triggers default behavior of
	 * the dispatcher code.
	 * 
	 * @return the meta-data or null
	 */
	public EventListenerMetadata getMetadata();
	
}
