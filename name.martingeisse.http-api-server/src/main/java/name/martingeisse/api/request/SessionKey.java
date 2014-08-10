/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

import java.io.Serializable;


/**
 * Identifies a field in the session.
 * For each key, at most one value can be stored.
 * 
 * Session keys are compared based on their identity.
 * That is, two session keys can only be equal if they
 * are the same object. Therefore, session keys are
 * typically created and stored as class constants.
 * 
 * Access to a session requires the current request cycle
 * to identify the session.
 * 
 * @param <T> the type of session object
 */
public final class SessionKey<T extends Serializable> {

	/**
	 * TODO: use something that is consistent across code changes!
	 */
	private static int instanceCounter = 0;
	
	/**
	 * the internalKey
	 */
	private final String internalKey;
	
	/**
	 * Constructor.
	 */
	public SessionKey() {
		int counterValue;
		synchronized(SessionKey.class) {
			counterValue = instanceCounter;
			instanceCounter++;
		}
		this.internalKey = ("api_" + counterValue);
	}

	/**
	 * Getter method for the internalKey.
	 * @return the internalKey
	 */
	public String getInternalKey() {
		return internalKey;
	}
	
	/**
	 * Sets the parameter object for this key.
	 * @param requestCycle the current request cycle
	 * @param value the parameter object to set
	 */
	public void set(ApiRequestCycle requestCycle, T value) {
		requestCycle.setSessionValue(this, value);
	}

	/**
	 * Obtains the parameter object for this key
	 * @param requestCycle the current request cycle
	 * @return the parameter object
	 */
	public T get(ApiRequestCycle requestCycle) {
		return requestCycle.getSessionValue(this);
	}
	
}
