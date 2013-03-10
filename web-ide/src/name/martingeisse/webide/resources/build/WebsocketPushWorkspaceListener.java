/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.build;

import java.util.Collection;

import name.martingeisse.wicket.websockets.WebSocketConnectionIdentifier;

/**
 * This listener fires a {@link ResourceChangePushMessage} for every workspace
 * modification, using a websocket connection specified at construction.
 * 
 * This listener is able to register / unregister itself. It uses the
 * websocket connection identifier as the uniqueness key when doing so.
 * This makes sure that opening another connection with the same connection
 * identifier (such as when re-loading a page) replaces the previous listener.
 * 
 * It will also de-register as a workspace listener automatically when
 * the websocket connection is closed. TODO: this isn't done yet.
 * We also cannot just listen to lose events because Wicket currently
 * misses out on close events when a page with an existing connection
 * gets re-rendered -- this produces a second open event but no close
 * event for the first connection. Solution: Probably just check on
 * push if the connection is still live, and also listen for close
 * events. This will leave stale listeners only for unused workspaces.
 * 
 * Listeners can be registered for multiple workspaces.
 */
public final class WebsocketPushWorkspaceListener implements IWorkspaceListener {

	/**
	 * the connectionIdentifier
	 */
	private final WebSocketConnectionIdentifier connectionIdentifier;

	/**
	 * Constructor.
	 * @param connectionIdentifier the identifier for the connection
	 * to push through
	 */
	public WebsocketPushWorkspaceListener(final WebSocketConnectionIdentifier connectionIdentifier) {
		this.connectionIdentifier = connectionIdentifier;
	}

	/**
	 * Getter method for the connectionIdentifier.
	 * @return the connectionIdentifier
	 */
	public WebSocketConnectionIdentifier getConnectionIdentifier() {
		return connectionIdentifier;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.build.IWorkspaceListener#onWorkspaceChange(java.util.Collection)
	 */
	@Override
	public void onWorkspaceChange(final Collection<BuilderResourceDelta> deltas) {
		ResourceChangePushMessage message = new ResourceChangePushMessage();
		connectionIdentifier.push(message);
	}
	
	/**
	 * Registers this listener with the {@link WorkspaceListenerRegistry} for the specified workspace.
	 * @param workspaceId the ID of the workspace to register for
	 */
	public void register(long workspaceId) {
		WorkspaceListenerRegistry.register(workspaceId, connectionIdentifier, this);
	}
	
	/**
	 * Unregisters this listener with the {@link WorkspaceListenerRegistry} for the specified workspace.
	 * @param workspaceId the ID of the workspace to register for
	 */
	public void unregister(long workspaceId) {
		WorkspaceListenerRegistry.unregister(workspaceId, connectionIdentifier);
	}

}
