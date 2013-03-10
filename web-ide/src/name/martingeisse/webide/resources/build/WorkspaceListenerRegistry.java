/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.build;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class keeps track of {@link IWorkspaceListener}s per
 * workspace.
 * 
 * Listeners are always added using a key, replacing any previous
 * listener with the same key. To add a listener on its own, the
 * listener should simply not override its default {@link #hashCode()}
 * and {@link #equals(Object)} methods and the listener itself be
 * used as the key. 
 */
public class WorkspaceListenerRegistry {

	/**
	 * the listeners
	 */
	private static final ConcurrentHashMap<Long, ConcurrentHashMap<Object, IWorkspaceListener>> listeners = new ConcurrentHashMap<Long, ConcurrentHashMap<Object,IWorkspaceListener>>();
	
	/**
	 * Prevent instantiation.
	 */
	private WorkspaceListenerRegistry() {
	}

	/**
	 * Registers the specified listener, removing any previous listener
	 * with the same key.
	 * 
	 * @param workspaceId the ID of the workspace for which the listener shall be registered
	 * @param key the uniqueness key
	 * @param listener the listener to register with this key
	 */
	public static void register(long workspaceId, Object key, IWorkspaceListener listener) {
		ConcurrentHashMap<Object, IWorkspaceListener> workspaceListeners = listeners.get(workspaceId);
		if (workspaceListeners == null) {
			listeners.putIfAbsent(workspaceId, new ConcurrentHashMap<Object, IWorkspaceListener>());
			workspaceListeners = listeners.get(workspaceId);
		}
		workspaceListeners.put(key, listener);
	}
	
	/**
	 * Removes the listener with the specified key (if present).
	 * 
	 * @param workspaceId the ID of the workspace for which the listener shall be unregistered
	 * @param key the uniqueness key
	 */
	public static void unregister(long workspaceId, Object key) {
		ConcurrentHashMap<Object, IWorkspaceListener> workspaceListeners = listeners.get(workspaceId);
		if (workspaceListeners != null) {
			workspaceListeners.remove(key);
		}
	}
	
	static void onWorkspaceChange(long workspaceId, Collection<BuilderResourceDelta> deltas) {
		ConcurrentHashMap<Object, IWorkspaceListener> workspaceListeners = listeners.get(workspaceId);
		if (workspaceListeners != null) {
			for (IWorkspaceListener listener : workspaceListeners.values()) {
				listener.onWorkspaceChange(deltas);
			}
		}
	}
	
}
