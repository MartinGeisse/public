/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.application.wicket;

import name.martingeisse.wicket.application.MyWicketSession;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * The session implementation.
 */
public class SlaveServicesWicketSession extends MyWicketSession {

	/**
	 * Getter method for the current session.
	 * @return the current session
	 */
	public static SlaveServicesWicketSession get() {
		return (SlaveServicesWicketSession)WebSession.get();
	}
	
	/**
	 * Constructor.
	 * @param request the request that creates this session
	 */
	public SlaveServicesWicketSession(Request request) {
		super(request);
	}
	
}
